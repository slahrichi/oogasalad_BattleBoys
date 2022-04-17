package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.Scene;
import oogasalad.GameData;
import oogasalad.PropertyObservable;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.EngineRecord;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.WinConditions.WinCondition;
import oogasalad.model.utilities.WinConditions.WinState;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.Info;
import oogasalad.view.GameView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameManager extends PropertyObservable implements PropertyChangeListener {

  private List<Player> playerList;
  private List<WinCondition> winConditionsList;
  private Map<Integer, Player> idMap;
  private Map<Player, DecisionEngine> engineMap;
  private GameView view;
  //current player, separate from ID
  private int playerIndex;
  private int numShots;
  private int allowedShots;
  private static final String INVALID_METHOD = "Invalid method name given";
  private static final Logger LOG = LogManager.getLogger(GameView.class);

  public GameManager(GameData data) {
    initialize(data);
    setupViewElements(data);

  }

  private void setupViewElements(GameData data) {
    List<CellState[][]> boards = createFirstPlayerBoards(data);
    Collection<Collection<Coordinate>> coords = createInitialPieces(data.pieces());
    view = new GameView(boards, coords);
    view.setPlayerIDToNames(generateIDToNames());
    view.addObserver(this);
  }

  private Map<Integer, String> generateIDToNames() {
    Map<Integer, String> idToName = new HashMap<>();

    for(Player p : playerList) {
      idToName.put(p.getID(), p.getName());
    }

    return idToName;
  }

  private List<CellState[][]> createFirstPlayerBoards(GameData data) {
    List<CellState[][]> boards = new ArrayList<>();
    Player firstPlayer = data.players().get(0);
    boards.add(firstPlayer.getBoard().getCurrentBoardState());
    for (int i = 0; i < firstPlayer.getEnemyMap().size(); i++) {
      boards.add(data.board());
    }
    return boards;
  }

  private Collection<Collection<Coordinate>> createInitialPieces(List<Piece> pieces) {
    Collection<Collection<Coordinate>> pieceCoords = new ArrayList<>();
    for (Piece piece : pieces) {
      pieceCoords.add(piece.getRelativeCoords());
    }
    return pieceCoords;
  }

  public Scene createScene() {
    return view.createScene();
  }


  private void initialize(GameData data) {
    this.playerList = data.players();
    playerIndex = 0;
    numShots = 0;
    allowedShots = 1;
    createIDMap();
    winConditionsList = data.winConditions();
    engineMap = data.engineMap();
  }

  private void createIDMap() {
    idMap = new HashMap<>();
    for (Player player : playerList) {
      idMap.put(player.getID(), player);
    }
  }


  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    int id = ((Info)evt.getNewValue()).ID();
    int row = ((Info)evt.getNewValue()).row();
    int col = ((Info)evt.getNewValue()).col();
    Info info = new Info(row, col, id);
    try {
      Method m = this.getClass().getDeclaredMethod(evt.getPropertyName(), Info.class);
      m.invoke(this, info);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
        NullPointerException e) {
      throw new NullPointerException(INVALID_METHOD);
    }
  }

  private void selfBoardClicked(Info info) {
    LOG.info("Self board clicked at "+info.row()+", "+info.col());
  }

  private void handleShot(Info info) {
    if (makeShot(new Coordinate(info.row(), info.col()), info.ID())) {
      updateConditions(info.ID());
    }
  }

  private void updateConditions(int id) {
    applyWinConditions();
    if(idMap.containsKey(id)){
      List<Piece> piecesLeft = idMap.get(id).getBoard().listPieces();
      Collection<Collection<Coordinate>> coords = convertPiecesToCoords(piecesLeft);
      view.updatePiecesLeft(coords);
    }
    numShots++;
    checkIfMoveToNextToPlayer();
  }

  private Collection<Collection<Coordinate>> convertPiecesToCoords(List<Piece> piecesLeft) {
    Collection<Collection<Coordinate>> coords = new ArrayList<>();
    for (Piece piece : piecesLeft) {
      coords.add(piece.getRelativeCoords());
    }
    return coords;
  }

  private void checkIfMoveToNextToPlayer() {
    if (numShots == allowedShots) {
      playerIndex = (playerIndex + 1) % playerList.size();
      numShots = 0;
      sendUpdatedBoardsToView();
      //handleAI();
    }
  }

  private void handleAI() {
    Player player = playerList.get(playerIndex);
    if (engineMap.containsKey(player)) {
      DecisionEngine engine = engineMap.get(player);
      EngineRecord move = engine.makeMove();
      LOG.info(move);
      makeShot(move.shot(), move.enemyID());
      updateConditions(player.getID());
    }
  }

  public List<Player> getPlayerList() {
    return playerList;
  }

  private boolean makeShot(Coordinate c, int id) {
    Player currentPlayer = playerList.get(playerIndex);
    Player enemy = idMap.get(id);
    if (currentPlayer.getEnemyMap().get(id).canPlaceAt(c)) {
      CellState result = enemy.getBoard().hit(c);
      adjustStrategy(currentPlayer, result);
      currentPlayer.updateEnemyBoard(c, id, result);
      view.displayShotAt(c.getRow(), c.getColumn(), result);
      applyModifiers(currentPlayer, enemy);
      return true;
    }
    return false;
  }

  private void adjustStrategy(Player player, CellState result) {
    if (engineMap.containsKey(player)) {
      DecisionEngine engine = engineMap.get(player);
      engine.adjustStrategy(result);
    }
  }

  private void applyModifiers(Player currPlayer, Player enemyPlayer){
    List<Modifiers> mods = enemyPlayer.getBoard().update();
    for(Modifiers mod :mods){
      if(mod.getClass().getSimpleName().equals("PlayerModifier")){
        Player[] players = {currPlayer, enemyPlayer};
        try{
          mod.modifierFunction().accept(players);
        }catch(Exception e){}
      }
    }
  }


  public void applyWinConditions() {
    for (WinCondition condition: winConditionsList) {
      checkCondition(condition);
    }
    if(playerList.size()==1) {
      moveToWinGame(playerList.get(0));
    }
  }

  private void checkCondition(WinCondition condition) {
    Set<Integer> playerIds = new HashSet<>(idMap.keySet());
    for(int id: playerIds) {
      Player currPlayer = idMap.get(id);
      WinState currPlayerWinState = condition.updateWinner(currPlayer);
      System.out.format("Player %d's WinState %s\n", id, currPlayerWinState);
      checkWinState(currPlayer, currPlayerWinState, id);
    }
  }

  private void checkWinState(Player player, WinState state, int id) {
    if (state.equals(WinState.LOSE)) {
      removePlayer(player, id);
      view.displayLosingMessage(id);
    } else if (state.equals(WinState.WIN)) {
      System.out.format("Player %d wins!", id);
      moveToWinGame(player);
    }
  }

  private void moveToWinGame(Player player) {
    int id = player.getID();
    view.displayWinningMessage(id);
  }

  private void removePlayer(Player player, int id) {
    System.out.println("player " + id + " lost");
    playerList.remove(player);
    idMap.remove(id);
    for (Player p : playerList) {
      p.getEnemyMap().remove(id);
    }
  }

  private void sendUpdatedBoardsToView() {
    List<CellState[][]> boardList = new ArrayList<>();
    List<Integer> idList = new ArrayList<>();
    List<Collection<Collection<Coordinate>>> pieceList = new ArrayList<>();
    Player currentPlayer = playerList.get(playerIndex);
    addToBoardElements(currentPlayer.getBoard().getCurrentBoardState(), currentPlayer.getID(),
        currentPlayer, boardList, idList, pieceList);
    Map<Integer, MarkerBoard> enemyMap = currentPlayer.getEnemyMap();
    for (int id : currentPlayer.getEnemyMap().keySet()) {
      addToBoardElements(enemyMap.get(id).getBoard(), id, idMap.get(id), boardList, idList, pieceList);
    }
    view.moveToNextPlayer(boardList, idList, pieceList);
  }

  private void addToBoardElements(CellState[][] board, int id, Player player, List<CellState[][]>
      boardList, List<Integer> idList, List<Collection<Collection<Coordinate>>> pieceList) {
    boardList.add(board);
    idList.add(id);
    pieceList.add(convertPiecesToCoords(player.getBoard().listPieces()));
  }
}

package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.Usables.Weapons.Weapon;
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
  private GameViewManager gameViewManager;
  private int playerIndex;
  private int numShots;
  private int allowedShots;
  private List<Info> AIShots;
  private static final String INVALID_METHOD = "Invalid method name given";
  private static final Logger LOG = LogManager.getLogger(GameManager.class);

  public GameManager(GameData data) {
    initialize(data);
    view = gameViewManager.getView();
    view.updateLabels(allowedShots, playerList.get(0).getNumPieces(), playerList.get(0).getMyCurrency());
    view.addObserver(this);
  }

  public Scene createScene() {
    return view.createScene();
  }


  private void initialize(GameData data) {
    this.playerList = data.players();
    playerIndex = 0;
    numShots = 0;
    allowedShots = 2;
    createIDMap();
    winConditionsList = data.winConditions();
    engineMap = data.engineMap();
    gameViewManager = new GameViewManager(data, idMap, allowedShots);
  }

  private void createIDMap() {
    idMap = new HashMap<>();
    for (Player player : playerList) {
      idMap.put(player.getID(), player);
      player.determineHealth();
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
      e.printStackTrace();
      throw new NullPointerException(INVALID_METHOD);
    }
  }

  private void selfBoardClicked(Info info) {
    LOG.info("Self board clicked at "+info.row()+", "+info.col());
  }

  private void handleShot(Info info) {
    Coordinate coordinate = new Coordinate(info.row(), info.col());
    if (numShots < allowedShots && makeShot(coordinate, info.ID())) {
      Player player = playerList.get(playerIndex);
      view.updateLabels(allowedShots-numShots, player.getNumPieces(), player.getMyCurrency());
      view.displayShotAnimation(coordinate.getRow(), coordinate.getColumn(), e -> updateConditions(info.ID()),
          info.ID());
//      PauseTransition pt = new PauseTransition(new Duration(1000));
//      pt.setOnFinished(e -> updateConditions(info.ID()));
//      pt.play();
    }
  }

  private void updateConditions(int id) {
    applyWinConditions();
    if(idMap.containsKey(id)){
      List<Piece> piecesLeft = idMap.get(id).getBoard().listPieces();
      gameViewManager.updatePiecesLeft(piecesLeft);
    }
    checkIfEndTurn();
  }

  private void checkIfEndTurn() {
    if (numShots == allowedShots) {
      if (engineMap.containsKey(playerList.get(playerIndex))) {
        view.displayAIMove(playerList.get(playerIndex).getID(), AIShots);
        endTurn(new Info(0, 0, 0));
      } else {
        view.allowEndTurn();
      }
    }
  }

  private void endTurn(Info info) {
    playerIndex = (playerIndex + 1) % playerList.size();
    Player player = playerList.get(playerIndex);
    view.updateLabels(allowedShots, player.getNumPieces(), player.getMyCurrency());
    numShots = 0;
    gameViewManager.sendUpdatedBoardsToView(playerIndex);
    handleAI();
  }

  private void handleAI() {
    Player player = playerList.get(playerIndex);
    if (engineMap.containsKey(player)) {
      AIShots = new ArrayList<>();
      DecisionEngine engine = engineMap.get(player);
      for (int i = 0; i < allowedShots; i++) {
        EngineRecord move = engine.makeMove();
        LOG.info(move);
        AIShots.add(new Info(move.shot().getRow(), move.shot().getColumn(), move.enemyID()));
        makeShot(move.shot(), move.enemyID());
        updateConditions(player.getID());
      }
    }
  }

  private boolean makeShot(Coordinate c, int id) {
    // check for whether a player lost in a previous shot
    // THROWS AN ERROR when playerList becomes smaller, but we are still trying to grab the same index
    Player currentPlayer = playerList.get(playerIndex);
    Player enemy = idMap.get(id);
    if (currentPlayer.getEnemyMap().get(id).canPlaceAt(c)) {
      CellState result = enemy.getBoard().hit(c,1);
      adjustStrategy(currentPlayer, result);
      currentPlayer.updateEnemyBoard(c, id, result);
      view.displayShotAt(c.getRow(), c.getColumn(), result);
      applyModifiers(currentPlayer, enemy);
      numShots++;
      return true;
    }
    return false;
  }

  private boolean makeShot(Coordinate c, int id, Weapon weaponUsed) {
    Player currentPlayer = playerList.get(playerIndex);
    Player enemy = idMap.get(id);
    try{
      Map<Coordinate, CellState> hitResults = weaponUsed.getFunction().apply(c, enemy.getBoard());
      for(Coordinate hitCoord: hitResults.keySet()){
        adjustStrategy(currentPlayer, hitResults.get(hitCoord));
        currentPlayer.updateEnemyBoard(hitCoord, id, hitResults.get(hitCoord));
        view.displayShotAt(hitCoord.getRow(), hitCoord.getColumn(), hitResults.get(hitCoord));
      }
      applyModifiers(currentPlayer, enemy);
      return true;
    }catch (Exception e){
      return false;
    }
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

  private void applyWinConditions() {
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
}

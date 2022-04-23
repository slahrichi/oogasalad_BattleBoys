package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.scene.Scene;
import oogasalad.GameData;
import oogasalad.PropertyObservable;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.EngineRecord;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.weapons.BasicShot;
import oogasalad.model.utilities.usables.weapons.EmpoweredShot;
import oogasalad.view.Info;
import oogasalad.view.GameView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller module for managing the game's resources and regulations. The class mainly focuses on
 * the turn-based logic of the game and relaying the results of a given player's move. The class
 * utilizes auxiliary classes to handle checking win conditions and updating view elements.
 *
 * @author Matthew Giglio
 */
public class GameManager extends PropertyObservable implements PropertyChangeListener {

  private Queue<Player> playerQueue;
  private ConditionHandler conditionHandler;
  private Map<Integer, Player> idMap;
  private Map<Player, DecisionEngine> engineMap;
  private Map<String, Usable> usablesIDMap;
  private GameView view;
  private GameViewManager gameViewManager;
  private int numShots;
  private int allowedShots;
  private int whenToMovePieces;
  private List<Info> AIShots;
  private Usable currentUsable;
  private static final String INVALID_METHOD = "Invalid method name given";
  private static final String DUMMY_INFO = "";
  private static final Logger LOG = LogManager.getLogger(GameManager.class);
  private ResourceBundle myResources;

  public GameManager(GameData data, ResourceBundle resourceBundle) {
  /**
   * @param data GameData object storing all pertinent resources for managing the game such as the
   *             players and the decision engines for the AI
   */
    initialize(data, resourceBundle);
    view = gameViewManager.getView();
    view.updateLabels(allowedShots, playerQueue.peek().getNumPieces(),
        playerQueue.peek().getMyCurrency());
    view.addObserver(this);

    conditionHandler = new ConditionHandler(playerQueue, idMap, data.winConditions(), view, gameViewManager,
        whenToMovePieces);
  }

  /**
   * @return Scene object storing the elements of the game view
   */
  public Scene createScene() {
    return view.createScene();
  }

  public int getCurrentPlayer() {
    return playerQueue.peek().getID();
  }


  private void initialize(GameData data, ResourceBundle resources) {
    currentUsable = new BasicShot();
    myResources = resources;
    this.playerQueue = new LinkedList<>();
    playerQueue.addAll(data.players());
    numShots = 0;
    whenToMovePieces = 1; //should change this to use gamedata from parser
    allowedShots = 2;
    createIDMap(data.players());
    engineMap = data.engineMap();

    //this should be replaced by gameData
    List<Usable> dummyUsables = new ArrayList<Usable>();
    dummyUsables.add(new BasicShot());
    dummyUsables.add(new EmpoweredShot("Double Damage", 1, 2));

    usablesIDMap = new HashMap<String, Usable>();
    for(Usable currUsable: dummyUsables) {
      usablesIDMap.put(currUsable.getMyID(), currUsable);
    }
    gameViewManager = new GameViewManager(data, idMap, allowedShots, myResources);
  }

  private void createIDMap(List<Player> playerList) {
    idMap = new HashMap<>();
    for (Player player : playerList) {
      idMap.put(player.getID(), player);
      player.determineHealth();
    }
  }

  /**
   * Listener method for executing handling of game moves/shots
   *
   * @param evt Event from the GameView such as a cell having been clicked
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    try {
      Method m = this.getClass().getDeclaredMethod(evt.getPropertyName(), String.class);
      m.invoke(this, evt.getNewValue());
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
        NullPointerException e) {
      e.printStackTrace();
      throw new NullPointerException(INVALID_METHOD);
    }
  }

  private void equipUsable(String id) {
    // set currentUsable equal to map.get(info.getID());
    currentUsable = usablesIDMap.get(id);
    LOG.info(String.format("Current Weapon: %s"), id);
  }

  private void buyItem(String id) {
    playerQueue.peek().makePurchase(usablesIDMap.get(id).getPrice(), id);
  }

  private void applyUsable(String clickInfo) {
    try {
      currentUsable.handleUsage().accept(clickInfo, this);
    }
    catch (Exception e) {
      view.showError(e.getMessage());
    }
  }

  private void selfBoardClicked(String clickInfo) {
    int row = Integer.parseInt(clickInfo.substring(0, clickInfo.indexOf(" ")));
    int col = Integer.parseInt(clickInfo.substring(clickInfo.indexOf(" ") + 1, clickInfo.lastIndexOf(" ")));
    int id = Integer.parseInt(clickInfo.substring(clickInfo.lastIndexOf(" ") + 1));
    LOG.info("Board " + id + " clicked at " + row + ", " + col);
  }

  public void handleShot(String clickInfo) {
    int row = Integer.parseInt(clickInfo.substring(0, clickInfo.indexOf(" ")));
    int col = Integer.parseInt(clickInfo.substring(clickInfo.indexOf(" ") + 1, clickInfo.lastIndexOf(" ")));
    int id = Integer.parseInt(clickInfo.substring(clickInfo.lastIndexOf(" ") + 1));
    if (numShots < allowedShots && makeShot(new Coordinate(row, col), id, currentUsable)) {
      Player player = playerQueue.peek();
      view.updateLabels(allowedShots - numShots, player.getNumPieces(), player.getMyCurrency());
      view.displayShotAnimation(row, col, e ->
          updateConditions(id), id);
    }
  }

  private void updateConditions(int id) {
    conditionHandler.applyWinConditions();
    if (idMap.containsKey(id)) {
      List<Piece> piecesLeft = idMap.get(id).getBoard().listPieces();
      gameViewManager.updatePiecesLeft(piecesLeft);
    }
    checkIfEndTurn();
  }

  private void checkIfEndTurn() {
    if (numShots == allowedShots) {
      // if AI has finished firing their shots
      if (engineMap.containsKey(playerQueue.peek())) {
        view.displayAIMove(playerQueue.peek().getID(), AIShots);
        endTurn(DUMMY_INFO);
      }
      // if a human has finished firing their shots
      else {
        view.allowEndTurn();
      }
    }
  }

  private void endTurn(String info) {
    Player p = playerQueue.poll();
    conditionHandler.updateTurns(p);
    playerQueue.add(p);
    checkIfMovePieces();
    Player player = playerQueue.peek();
    view.updateLabels(allowedShots, player.getNumPieces(), player.getMyCurrency());
    numShots = 0;
    gameViewManager.sendUpdatesToView(player);
    view.moveToNextPlayer(player.getName());
    handleAI();
  }

  //new private method for moving pieces
  private void checkIfMovePieces() {
    if (conditionHandler.canMovePieces()) {
      conditionHandler.resetTurnMap();
      for (Player p : playerQueue) {
        p.movePieces();
      }
      for (DecisionEngine engine : engineMap.values()) {
        engine.resetStrategy();
      }
    }
  }

  private void handleAI() {
    Player player = playerQueue.peek();
    if (engineMap.containsKey(player)) {
      AIShots = new ArrayList<>();
      DecisionEngine engine = engineMap.get(player);
      for (int i = 0; i < allowedShots; i++) {
        EngineRecord move = engine.makeMove();
        LOG.info(move);
        AIShots.add(new Info(move.shot().getRow(), move.shot().getColumn(), move.enemyID()));
        makeShot(move.shot(), move.enemyID(), currentUsable);
        updateConditions(player.getID());
      }
    }
  }

  private boolean makeShot(Coordinate c, int id, Usable weaponUsed) {
    Player currentPlayer = playerQueue.peek();
    Map<String, Integer> currentInventory = currentPlayer.getInventory();
    Player enemy = idMap.get(id);
    try {
      Map<Coordinate, CellState> hitResults = weaponUsed.getFunction().apply(c, enemy.getBoard());
      for (Coordinate hitCoord : hitResults.keySet()) {
        adjustStrategy(currentPlayer, hitResults.get(hitCoord));
        currentPlayer.updateEnemyBoard(hitCoord, id, hitResults.get(hitCoord));
        view.displayShotAt(hitCoord.getRow(), hitCoord.getColumn(), hitResults.get(hitCoord));
      }
      List<Modifiers> mods = conditionHandler.applyModifiers(currentPlayer, enemy);

      for(Modifiers mod : mods)
          mod.modifierFunction(this).accept(this);

      numShots++;

      //this removes one weapon when used and removes it from the inventory when all are used.
      //currentInventory.put(currentUsable.getMyID(),currentInventory.get(currentUsable.getMyID())-1 );
      //if(currentInventory.get(currentUsable.getMyID())<=0) {
      //  currentInventory.remove(currentUsable.getMyID());
      //}

      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private void adjustStrategy(Player player, CellState result) {
    if (engineMap.containsKey(player)) {
      DecisionEngine engine = engineMap.get(player);
      engine.adjustStrategy(result);
    }
  }

  public GameViewManager getGameViewManager() {
    return gameViewManager;
  }

  public Map<Integer, Player> getIDMap() {
    return idMap;
  }
}
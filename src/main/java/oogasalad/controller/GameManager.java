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
import oogasalad.PropertyObservable;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.EngineRecord;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.weapons.BasicShot;
import oogasalad.view.GameView;
import oogasalad.view.Info;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Controller module for managing the game's resources and regulations. The class mainly focuses on
 * the turn-based logic of the game and relaying the results of a given player's move. The class
 * utilizes auxiliary classes to handle checking win conditions and updating view elements.
 *
 * @author Matthew Giglio with assisting contributions from Minjun Kwak and Brandon Bae
 */
public class GameManager extends PropertyObservable implements PropertyChangeListener {

  private Queue<Player> playerQueue;
  private final ConditionHandler conditionHandler;
  private Map<Integer, Player> idMap;
  private Map<Player, DecisionEngine> engineMap;
  private Map<String, Usable> usablesIDMap;
  private final GameView view;
  private GameViewManager gameViewManager;
  private int numShots;
  private int allowedShots;
  private int whenToMovePieces;
  private List<Info> AIShots;
  private Usable currentUsable;
  private static final int CHEAT_GOLD_AMOUNT = 1000;
  private static final String INVALID_METHOD = "Invalid method name given";
  private static final String DUMMY_INFO = "";
  private static final String STRIPE = "stripe";
  private static final String SHOT_FAILURE = "Shot attempt failed!";
  private static final String WEAPON_LOG = "Current weapon: %s";
  private static final String BOARD_CLICKED = "Board %d clicked at row %d, col %d";
  private static final String MAIN_MENU_OPERATION = "mainMenu";
  private static final Logger LOG = LogManager.getLogger(GameManager.class);
  private ResourceBundle myResources;

  public GameManager(GameData data, ResourceBundle resourceBundle) {
    /**
     * @param data GameData object storing all pertinent resources for managing the game such as the
     *             players and the decision engines for the AI
     */
    initialize(data, resourceBundle);
    view = gameViewManager.getView();
    view.addObserver(this);
    view.updateLabels(allowedShots, playerQueue.peek().getNumPieces(),
        playerQueue.peek().getMyCurrency());
    conditionHandler = new ConditionHandler(playerQueue, idMap, data.winConditions(), view,
        gameViewManager,
        whenToMovePieces);
  }

  /**
   * @return Scene object storing the elements of the game view
   */
  public Scene createScene() {
    return view.createScene();
  }

  /**
   * @return ID of current player
   */
  public int getCurrentPlayer() {
    return playerQueue.peek().getID();
  }

  public void makeFirstAIPlayersMove() {
    while (engineMap.containsKey(playerQueue.peek())) {
      handleAI();
    }
  }

  private void initialize(GameData data, ResourceBundle resources) {
    currentUsable = new BasicShot();
    myResources = resources;
    this.playerQueue = new LinkedList<>(data.players());
    numShots = 0;
    whenToMovePieces = data.shipMovementRate();
    allowedShots = data.shotsPerTurn();
    createIDMap(data.players());
    createUsablesMap(data.allUsables());
    engineMap = data.engineMap();
    gameViewManager = new GameViewManager(data, usablesIDMap, idMap, myResources);
  }

  private void createIDMap(List<Player> playerList) {
    idMap = new HashMap<>();
    for (Player player : playerList) {
      idMap.put(player.getID(), player);
      player.determineHealth();
    }
  }

  private void createUsablesMap(List<Usable> usables) {
    usablesIDMap = new HashMap<>();
    for (Usable usable : usables) {
      usablesIDMap.put(usable.getMyID(), usable);
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
      throw new NullPointerException(INVALID_METHOD);
    }
  }

  private void equipUsable(String id) {
    currentUsable = usablesIDMap.get(id);
    LOG.info(String.format(WEAPON_LOG, id));
    view.setCurrentUsable(id, usablesIDMap.get(id).getRelativeCoordShots().keySet());
  }

  private void addGold(String param) {
    playerQueue.peek().addGold(CHEAT_GOLD_AMOUNT);
    view.updateLabels(allowedShots - numShots, playerQueue.peek().getNumPieces(), playerQueue.peek().getMyCurrency());
  }

  private void addRandomUsable(String param) {
    List<String> allUsablesList = new ArrayList<>(usablesIDMap.keySet());
    int randomUsablesIndex = (int) ((Math.random() * (usablesIDMap.keySet()).size()-1));
    playerQueue.peek().makePurchase(0, allUsablesList.get(randomUsablesIndex));
    view.updateLabels(allowedShots - numShots, playerQueue.peek().getNumPieces(),
            playerQueue.peek().getMyCurrency());
    view.updateInventory(
            gameViewManager.convertMapToUsableRecord(playerQueue.peek().getMyInventory()));
  }

  private void makeCurrPlayerWin(String param) {
    view.displayWinningScreen(playerQueue.peek().getName());
  }

  private void addAnotherShot(String param) {
    numShots--;
    view.updateLabels(allowedShots - numShots, playerQueue.peek().getNumPieces(),
            playerQueue.peek().getMyCurrency());
  }

  private void addNumShotsAllowed(String param) {
    allowedShots++;
    view.updateLabels(allowedShots - numShots, playerQueue.peek().getNumPieces(),
            playerQueue.peek().getMyCurrency());
  }

  private void decrementNumShotsAllowed(String param) {
    if(numShots>1) numShots--;
    view.updateLabels(allowedShots - numShots, playerQueue.peek().getNumPieces(),
            playerQueue.peek().getMyCurrency());
  }

  private void addWhenToMove(String param) {
    whenToMovePieces++;
  }

  private void decrementWhenToMove(String param) {
    if(whenToMovePieces>1) whenToMovePieces--;
  }

  private void buyItem(String id) {
    String genericItem = id.replace(STRIPE, "");
    playerQueue.peek().makePurchase(usablesIDMap.get(genericItem).getPrice(), id);
    view.updateLabels(allowedShots - numShots, playerQueue.peek().getNumPieces(),
        playerQueue.peek().getMyCurrency());
    view.updateInventory(
        gameViewManager.convertMapToUsableRecord(playerQueue.peek().getMyInventory()));
  }

  private void applyUsable(String clickInfo) {
    try {
      currentUsable.handleUsage().accept(clickInfo, this);
    } catch (Exception e) {
      view.showError(e.getMessage());
    }
  }

  private void selfBoardClicked(String clickInfo) {
    GameView.handleClickInfo(clickInfo, LOG, BOARD_CLICKED);
  }

  private void mainMenu(String clickInfo){
    notifyObserver(MAIN_MENU_OPERATION, null);
  }

  /**
   * @param clickInfo String containing coordinates and enemy ID of player to be attacked
   */
  public void handleShot(String clickInfo) {
    int row = Integer.parseInt(clickInfo.substring(0, clickInfo.indexOf(" ")));
    int col = Integer.parseInt(
        clickInfo.substring(clickInfo.indexOf(" ") + 1, clickInfo.lastIndexOf(" ")));
    int id = Integer.parseInt(clickInfo.substring(clickInfo.lastIndexOf(" ") + 1));
    if (numShots < allowedShots && makeShot(new Coordinate(row, col), id, currentUsable)) {
      Player player = playerQueue.peek();
      view.updateLabels(allowedShots - numShots, player.getNumPieces(),
          player.getMyCurrency());
      view.displayShotAnimation(row, col, e ->
          updateConditions(id), id);
    }
  }

  private void updateConditions(int id) {
    conditionHandler.applyWinConditions();
    view.updateInventory(
        gameViewManager.convertMapToUsableRecord(playerQueue.peek().getMyInventory()));
    if (idMap.containsKey(id)) {
      List<Piece> piecesLeft = idMap.get(id).getBoard().listPieces();
      gameViewManager.updatePiecesLeft(piecesLeft);
    }
    checkIfEndTurn();
  }

  private void checkIfEndTurn() {
    if (numShots == allowedShots) {
      if (engineMap.containsKey(playerQueue.peek())) {
        view.displayAIMove(playerQueue.peek().getID(), AIShots);
        endTurn(DUMMY_INFO);
      } else {
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
    currentUsable = new BasicShot();
    handleAI();
  }

  private void checkIfMovePieces() {
    if (conditionHandler.canMovePieces()) {
      conditionHandler.resetTurnMap();
      for (Player p : playerQueue) {
        p.movePieces();
      }
      resetDecisionEngines();
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
        makeShot(move.shot(), move.enemyID(), move.weapon());
        updateConditions(player.getID());
      }
    }
  }

  private boolean makeShot(Coordinate c, int id, Usable weaponUsed) {
    Player currentPlayer = playerQueue.peek();
    Map<String, Double> currentInventory = currentPlayer.getMyInventory();
    Player enemy = idMap.get(id);
    try {
      Map<Coordinate, CellState> hitResults = weaponUsed.getFunction().apply(c, enemy.getBoard());
      for (Coordinate hitCoord : hitResults.keySet()) {
        adjustStrategy(currentPlayer, hitResults.get(hitCoord));
        currentPlayer.updateEnemyBoard(hitCoord, id, hitResults.get(hitCoord));
        view.displayShotAt(hitCoord.getRow(), hitCoord.getColumn(), hitResults.get(hitCoord));
      }
      conditionHandler.applyModifiers(currentPlayer, enemy, this);
      numShots++;
      handleInventory(currentInventory);
      return true;
    } catch (Exception e) {
      view.showError(SHOT_FAILURE);
      return false;
    }
  }

  private void handleInventory(Map<String, Double> currentInventory) {
    currentInventory.put(currentUsable.getMyID(),
        currentInventory.get(currentUsable.getMyID()) - 1);
    if (currentInventory.get(currentUsable.getMyID()) <= 0) {
      currentInventory.remove(currentUsable.getMyID());
    }
  }

  private void adjustStrategy(Player player, CellState result) {
    if (engineMap.containsKey(player)) {
      DecisionEngine engine = engineMap.get(player);
      engine.adjustStrategy(result);
    }
  }


  private void resetDecisionEngines() {
    for (DecisionEngine engine : engineMap.values()) {
      engine.resetStrategy();
    }
  }


  /**
   * A method for updating a weapon that I genuinely had no idea about until 2 hours before the
   * project was due :|
   *
   * @param count shots to be added to current shot count
   */
  public void addRemainingShots(int count){
    numShots +=count;
  }

  /**
   * @return GameViewManager associated with class
   */
  public GameViewManager getGameViewManager() {
    return gameViewManager;
  }

  /**
   * @return map relating player IDs to player objects
   */
  public Map<Integer, Player> getIDMap() {
    return idMap;
  }
}
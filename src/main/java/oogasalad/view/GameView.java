package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.view.board.BoardView;
import oogasalad.view.board.EnemyBoardView;
import oogasalad.view.board.GameBoardView;
import oogasalad.view.board.SelfBoardView;
import oogasalad.view.interfaces.BoardVisualizer;
import oogasalad.view.interfaces.ErrorDisplayer;
import oogasalad.view.interfaces.GameDataVisualizer;
import oogasalad.view.interfaces.ShopVisualizer;
import oogasalad.view.interfaces.ShotVisualizer;
import oogasalad.view.maker.BoxMaker;
import oogasalad.view.maker.ButtonMaker;
import oogasalad.view.maker.DialogMaker;
import oogasalad.view.maker.LabelMaker;
import oogasalad.view.panes.ConfigPane;
import oogasalad.view.panes.LegendPane;
import oogasalad.view.panes.SetPiecePane;
import oogasalad.view.panels.TitlePanel;
import oogasalad.view.screens.AbstractScreen;
import oogasalad.view.screens.LoserScreen;
import oogasalad.view.screens.PassComputerScreen;
import oogasalad.view.screens.WinnerScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Spark;

/**
 * This class is the main view class for the gameplay stage. It is the top of the view hierarchy for
 * the main gameplay stage and owns all visual components that appear during that stage. One
 * GameView should be created and continuously modified throughout the duration of the game.
 *
 * @author Minjun Kwak, Eric Xie, Edison Ooi
 */
public class GameView extends PropertyObservable implements PropertyChangeListener, BoardVisualizer,
    ShopVisualizer, ShotVisualizer, GameDataVisualizer, ErrorDisplayer {

  // Visual constants
  private static final Logger LOG = LogManager.getLogger(GameView.class);
  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;

  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String DAY_STYLESHEET = "stylesheets/viewStylesheet.css";
  private static final String NIGHT_STYLESHEET = "stylesheets/nightStylesheet.css";
  private static final String IMAGES_PATH = "images/";
  private static final String EXPLOSION_IMAGE_NAME = "explosion-icon.png"; // TODO: Get explosion image from resource bundle

  private static final String CENTER_PANE_ID = "view-center-pane";
  private static final String VIEW_PANE_ID = "view-pane";
  private static final String VIEW_SHOP_BTN_ID = "view-shop-button";
  private static final String SHOTS_REMAINING_LABEL_ID = "shots-remaining-label";
  private static final String NUM_PIECES_LABEL_ID = "num-pieces-label";
  private static final String GOLD_LABEL_ID = "gold-label";
  private static final String CURRENT_USABLE_LABEL_ID = "current-usable-label";
  private static final String CONFIG_BOX_ID = "configBox";
  private static final String GAME_TITLE_ID = "game-title";
  private static final String BOARD_LABEL_ID = "board-label";
  private static final String CURRENT_BOARD_LABEL_ID = "currentBoardLabel";
  private static final String LEFT_BUTTON_ID = "left-button";
  private static final String LEFT_BUTTON_IMAGE = "arrow-left.png";
  private static final String ARROW_CLASS = "arrow-button";
  private static final String RIGHT_BUTTON_ID = "right-button";
  private static final String RIGHT_BUTTON_IMAGE = "arrow-right.png";
  private static final String END_TURN_BTN_ID = "end-turn-button";
  private static final String BUTTON_BOARD_BOX_ID = "board-button-box";
  private static final String GAMEVIEW_ALERT_ID = "gameview-alert";
  private static final String AI_ALERT_ID = "alert";

  private static final String END_TURN_OPERATION = "endTurn";
  private static final String APPLY_USABLE_OPERATION = "applyUsable";
  private static final String MAIN_MENU_OPERATION = "mainMenu";
  private static final String BASIC_SHOT_DYNAMIC_TEXT = "Basic Shot";
  private static final String INVALID_METHOD = "Invalid method name given";
  private static final double BOARD_SIZE = 30;
  private static final double SET_PIECE_PANE_SIZE = 20;
  private static final int EXPLOSION_DURATION = 1000;
  private static final double CONFIG_BOX_SIZE = 300;
  private static final double CENTER_PANE_SPACING = 20;
  private static final double ARROW_SIZE = 50;
  private static final double LOSER_SIZE = 600;


  // Text constants
  private static final String BOARD_INDEX_LOG = "Current board index: ";
  private static final String BOARD_SHOW_LOG = "Showing board ";
  private static final String BOARD_CLICKED_LOG = "Board %d was clicked at row: %d, col: %d";
  private static final String AI_SHOT_TEXT = "Player %d took a shot at row %d, column %d on player %d";

  // ResourceBundle Strings

  private static final String TURN_SUFFIX_RESOURCE = "TurnSuffix";
  private static final String YOUR_BOARD_RESOURCE = "YourBoard";
  private static final String SHOTS_AGAINST_RESOURCE = "YourShotsAgainst";
  private static final String PLAYER_PREFIX_RESOURCE = "PlayerPrefix";
  private static final String OPEN_SHOP_RESOURCE = "OpenShop";
  private static final String SHIPS_REMAINING_RESOURCE = "ShipsRemaining";
  private static final String CONFIG_TEXT_RESOURCE = "ConfigText";
  private static final String SHOTS_REMAINING_RESOURCE = "ShotsRemainingText";
  private static final String END_TURN_RESOURCE = "EndTurn";
  private static final String CURRENT_USABLE_RESOURCE = "CurrentUsable";
  private static final String PIECES_LEFT_RESOURCE = "PiecesLeft";
  private static final String GOLD_LEFT_RESOURCE = "GoldLeft";

  // JavaFX components
  private TitlePanel myTitle;
  private final BorderPane myPane;
  private VBox myCenterPane;
  private Label currentBoardLabel;
  private HBox boardButtonBox;
  private Button endTurnButton;
  private Button shopButton;
  private SetPiecePane piecesRemainingPane;
  private LegendPane pieceLegendPane;
  private DynamicLabel currentUsableLabel;
  private DynamicLabel shotsRemainingLabel;
  private DynamicLabel numPiecesLabel;
  private DynamicLabel goldLabel;
  private AbstractScreen passComputerMessageView;
  private final ResourceBundle myResources;
  private InventoryView inventory;

  // Custom windows
  private Stage loserStage;
  private Stage shopStage;

  // View metadata
  private final List<BoardView> myBoards;
  private int currentBoardIndex;
  private List<Collection<Collection<Coordinate>>> myPiecesLeft;
  private final Map<Integer, String> playerIDToNames;
  private List<Usable> shopUsables;
  private Collection<Coordinate> currentUsableRelativeCoords;
  private boolean nightMode;
  private Scene myScene;
  private final Map<CellState, Color> myColorMap;

  /**
   * Class constructor.
   *
   * @param firstPlayerBoards  List of rectangular representations of the first player's own board
   *                           and their opponent shots boards
   * @param initialPiecesLeft  Pieces they own that are still alive
   * @param idToNames          Map of player ID's to their name
   * @param firstPlayerUsables List of UsableRecords representing all the items in the first
   *                           player's inventory
   * @param resourceBundle     ResourceBundle for this GameView
   */
  public GameView(List<CellState[][]> firstPlayerBoards,
      Collection<Collection<Coordinate>> initialPiecesLeft, Map<Integer, String> idToNames,
      List<UsableRecord> firstPlayerUsables, Map<CellState, Color> colorMap,
      ResourceBundle resourceBundle) {
    myPane = new BorderPane();
    myPane.setId(VIEW_PANE_ID);
    nightMode = false;
    myColorMap = colorMap;
    myBoards = new ArrayList<>();
    myPiecesLeft = new ArrayList<>();
    currentBoardIndex = 0;
    currentUsableRelativeCoords = new ArrayList<>(List.of(new Coordinate(0, 0)));
    Platform.runLater(() -> {
      loserStage = new Stage();
      shopStage = new Stage();
    });
    playerIDToNames = idToNames;
    myResources = resourceBundle;
    initialize(firstPlayerBoards, initialPiecesLeft, firstPlayerUsables);
  }

  /**
   * Creates scene that GameView will be displayed on.
   *
   * @return new Scene
   */
  public Scene createScene() {
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + DAY_STYLESHEET).toExternalForm());
    return myScene;
  }

  /**
   * @param usables Usables available for purchase to appear in the shop
   */
  public void setShopUsables(List<Usable> usables) {
    shopUsables = usables;
  }

  // Creates initial list of internal player ID's, corresponding to boards
  private List<Integer> createInitialIDList(int numPlayers) {
    List<Integer> idList = new ArrayList<>();
    for (int i = 0; i < numPlayers; i++) {
      idList.add(i);
    }
    return idList;
  }

  // Main initialization checkpoint for organizational purposes
  private void initialize(List<CellState[][]> firstPlayerBoards,
      Collection<Collection<Coordinate>> initialPiecesLeft, List<UsableRecord> firstPlayerUsables) {
    initializeBoards(firstPlayerBoards, createInitialIDList(firstPlayerBoards.size()));
    createCenterPane();
    createRightPane();
    createTitlePanel();
    createPassMessageView();
    initializePiecesLeft(initialPiecesLeft);
    createInventory();
    updateInventory(firstPlayerUsables);
  }

  // Creates InventoryView and adds it to center pane
  private void createInventory() {
    inventory = new InventoryView();
    inventory.addObserver(this);
    myCenterPane.getChildren().add(inventory.getPane());
  }

  /**
   * Updates inventory view with items in given list
   *
   * @param usableList List of UsableRecords representing the new inventory
   */
  public void updateInventory(List<UsableRecord> usableList) {
    inventory.updateElements(usableList);
  }

  // Creates a reusable PassComputerScreen
  private void createPassMessageView() {
    passComputerMessageView = new PassComputerScreen(e -> switchToMainScreen(), myResources);
  }

  /**
   * Switches view back to main gameplay screen.
   */
  public void switchToMainScreen() {
    myScene.setRoot(myPane);
  }

  // Initializes pieces remaining pane with all of this player's Pieces
  private void initializePiecesLeft(Collection<Collection<Coordinate>> piecesLeft) {
    for (int i = 0; i < myBoards.size(); i++) {
      myPiecesLeft.add(piecesLeft);
    }
    updatePiecesLeft(myPiecesLeft.get(currentBoardIndex));
  }

  // Initializes player's own board and their enemy boards
  private void initializeBoards(List<CellState[][]> boards, List<Integer> idList) {
    GameBoardView self = new SelfBoardView(BOARD_SIZE, boards.get(0), myColorMap, idList.get(0));
    myBoards.add(self);
    self.addObserver(this);
    for (int i = 1; i < boards.size(); i++) {
      GameBoardView enemy = new EnemyBoardView(BOARD_SIZE, boards.get(i), myColorMap,
          idList.get(i));
      myBoards.add(enemy);
      enemy.addObserver(this);
    }
  }

  // Creates right pane to show game data, config buttons, and buttons to open shop
  private void createRightPane() {
    shopButton = ButtonMaker.makeTextButton(VIEW_SHOP_BTN_ID, e -> openShop(),
        myResources.getString(OPEN_SHOP_RESOURCE));
    piecesRemainingPane = new SetPiecePane(SET_PIECE_PANE_SIZE, myColorMap, myResources);
    piecesRemainingPane.setText(myResources.getString(SHIPS_REMAINING_RESOURCE));

    setupPieceLegendPane();

    shotsRemainingLabel = LabelMaker.makeDynamicLabel(
        myResources.getString(SHOTS_REMAINING_RESOURCE), "",
        SHOTS_REMAINING_LABEL_ID);
    numPiecesLabel = LabelMaker.makeDynamicLabel(myResources.getString(PIECES_LEFT_RESOURCE), "",
        NUM_PIECES_LABEL_ID);
    goldLabel = LabelMaker.makeDynamicLabel(myResources.getString(GOLD_LEFT_RESOURCE), "",
        GOLD_LABEL_ID);

    ConfigPane configPane = new ConfigPane(myResources);
    configPane.setText(myResources.getString(CONFIG_TEXT_RESOURCE));
    configPane.setNightAction(e -> toggleNightMode());
    configPane.setMenuAction(e -> navigateMenu());

    currentUsableLabel = LabelMaker.makeDynamicLabel(myResources.getString(CURRENT_USABLE_RESOURCE),
        BASIC_SHOT_DYNAMIC_TEXT, CURRENT_USABLE_LABEL_ID);

    VBox myRightPane = BoxMaker.makeVBox(CONFIG_BOX_ID, 0, Pos.TOP_CENTER, currentUsableLabel,
        shotsRemainingLabel,
        numPiecesLabel, goldLabel, shopButton,
        piecesRemainingPane, pieceLegendPane, configPane);
    myRightPane.setMinWidth(CONFIG_BOX_SIZE);
    myPane.setRight(myRightPane);
  }

  /**
   * Sets the current Usable that the player has equipped.
   *
   * @param id           ID of Usable
   * @param usableCoords Usable's coordinates
   */
  public void setCurrentUsable(String id, Collection<Coordinate> usableCoords) {
    currentUsableLabel.changeDynamicText(id);
    currentUsableRelativeCoords = usableCoords;
  }

  // Creates pane to show what certain cell colors represent
  private void setupPieceLegendPane() {
    pieceLegendPane = new LegendPane(myColorMap, myResources);
  }

  // Creates main center pane that holds inventory, boards, and gameplay buttons
  private void createCenterPane() {
    myCenterPane = BoxMaker.makeVBox(CENTER_PANE_ID, CENTER_PANE_SPACING, Pos.CENTER);
    myPane.setCenter(myCenterPane);

    setupBoardLabel();
    myCenterPane.getChildren().add(myBoards.get(currentBoardIndex).getBoardPane());
    setupBoardButtons();
  }

  // Creates title panel
  private void createTitlePanel() {
    myTitle = new TitlePanel("");
    updateTitle(playerIDToNames.get(myBoards.get(currentBoardIndex).getID()));
    myTitle.setId(GAME_TITLE_ID);
    myPane.setTop(myTitle);
  }

  // Creates label that shows which board is currently being displayed
  private void setupBoardLabel() {
    currentBoardLabel = LabelMaker.makeLabel(myResources.getString(YOUR_BOARD_RESOURCE),
        BOARD_LABEL_ID);
    currentBoardLabel.setId(CURRENT_BOARD_LABEL_ID);
    myCenterPane.getChildren().add(currentBoardLabel);
  }

  // Creates buttons to allow user to switch between BoardViews or end their turn
  private void setupBoardButtons() {
    Button leftButton = ButtonMaker.makeImageButton(LEFT_BUTTON_ID, e -> decrementBoardIndex(),
        IMAGES_PATH + LEFT_BUTTON_IMAGE, ARROW_SIZE, ARROW_SIZE);
    leftButton.getStyleClass().add(ARROW_CLASS);

    Button rightButton = ButtonMaker.makeImageButton(RIGHT_BUTTON_ID, e -> incrementBoardIndex(),
        IMAGES_PATH + RIGHT_BUTTON_IMAGE, ARROW_SIZE, ARROW_SIZE);
    rightButton.getStyleClass().add(ARROW_CLASS);

    endTurnButton = ButtonMaker.makeTextButton(END_TURN_BTN_ID, e -> endTurn(),
        myResources.getString(END_TURN_RESOURCE));
    endTurnButton.setDisable(true);
    boardButtonBox = BoxMaker.makeHBox(BUTTON_BOARD_BOX_ID, CENTER_PANE_SPACING, Pos.CENTER,
        leftButton,
        rightButton, endTurnButton);

    myCenterPane.getChildren().add(boardButtonBox);
  }

  /**
   * Allows users to end their turn by clicking on an "End Turn" button.
   */
  public void allowEndTurn() {
    endTurnButton.setDisable(false);
  }

  // Ends current player's turn and notifies observer of this event
  private void endTurn() {
    endTurnButton.setDisable(true);
    notifyObserver(END_TURN_OPERATION, "");
  }

  private void navigateMenu() {
    notifyObserver(MAIN_MENU_OPERATION, "");
  }

  // Decrements currentBoardIndex and updates the shown board
  private void decrementBoardIndex() {
    currentBoardIndex = (currentBoardIndex + myBoards.size() - 1) % myBoards.size();
    updateDisplayedBoard();
  }

  // Increments currentBoardIndex and updates the shown board
  private void incrementBoardIndex() {
    currentBoardIndex = (currentBoardIndex + myBoards.size() + 1) % myBoards.size();
    updateDisplayedBoard();
  }

  // Displays the board indicated by the updated value of currentBoardIndex
  private void updateDisplayedBoard() {
    LOG.info(BOARD_INDEX_LOG + currentBoardIndex);
    currentBoardLabel.setText(currentBoardIndex == 0 ? myResources.getString(YOUR_BOARD_RESOURCE)
        : myResources.getString(SHOTS_AGAINST_RESOURCE) + playerIDToNames.getOrDefault(
            myBoards.get(currentBoardIndex).getID(),
            myResources.getString(PLAYER_PREFIX_RESOURCE) + (myBoards.get(currentBoardIndex).getID()
                + 1)));
    refreshCenterPane();
    updatePiecesLeft(myPiecesLeft.get(currentBoardIndex));
    LOG.info(BOARD_INDEX_LOG + currentBoardIndex);
    LOG.info(BOARD_SHOW_LOG + (myBoards.get(currentBoardIndex).getID() + 1));
  }

  // Re-populates center pane's children when player switches boards
  private void refreshCenterPane() {
    myCenterPane.getChildren().clear();
    myCenterPane.getChildren()
        .addAll(currentBoardLabel, myBoards.get(currentBoardIndex).getBoardPane(), boardButtonBox,
            inventory.getPane());
  }

  // Updates title to reflect current player's name
  private void updateTitle(String playerName) {
    myTitle.changeTitle(playerName + myResources.getString(TURN_SUFFIX_RESOURCE));
  }

  // Displays a PassComputerScreen when the current player's turn ends
  private void displayPassComputerMessage(String nextPlayer) {
    passComputerMessageView.setLabelText(nextPlayer);
    System.out.println(nextPlayer);
    myScene.setRoot(passComputerMessageView);
  }

  // This method is called by reflection
  // Notifies observer that player has purchased an item
  private void buyItem(String itemID) {
    notifyObserver(new Object() {
    }.getClass().getEnclosingMethod().getName(), itemID);
  }

  // Returns currentBoardIndex for testing purposes
  public int getCurrentBoardIndex() {
    return currentBoardIndex;
  }

  /**
   * Detects a property change from an Object that this class is observing and invokes a
   * corresponding method with the event's info.
   *
   * @param evt Event that was fired by listenee
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

  // Notifies observer that player wishes to equip a Usable
  private void equipUsable(String id) {
    // this is the ID of the Usable
    notifyObserver(new Object() {
    }.getClass().getEnclosingMethod().getName(), id);
  }

  // Notifies observer that user has clicked somewhere on their own board
  private void cellClickedSelf(String clickInfo) {
    handleCellClicked(clickInfo);
  }

  // Notifies observer that user has clicked somewhere on an enemy's board
  private void cellClickedEnemy(String clickInfo) {
    handleCellClicked(clickInfo);
  }

  private void handleCellClicked(String clickInfo) {
    handleClickInfo(clickInfo, LOG, BOARD_CLICKED_LOG);
    notifyObserver(APPLY_USABLE_OPERATION, clickInfo);
  }

  public static void handleClickInfo(String clickInfo, Logger log, String boardClickedLog) {
    int row = Integer.parseInt(clickInfo.substring(0, clickInfo.indexOf(" ")));
    int col = Integer.parseInt(
        clickInfo.substring(clickInfo.indexOf(" ") + 1, clickInfo.lastIndexOf(" ")));
    int id = Integer.parseInt(clickInfo.substring(clickInfo.lastIndexOf(" ") + 1));
    log.info(String.format(boardClickedLog, id, row, col));
  }

  private void cellHoveredSelf(String hoverInfo) {

  }

  private void cellHoveredEnemy(String hoverInfo) {

  }

  private void cellExitedSelf(String exitedInfo) {

  }

  private void cellExitedEnemy(String exitedInfo) {

  }

  // Switches stylesheets between light mode and night mode
  private void toggleNightMode() {
    nightMode = !nightMode;
    myScene.getStylesheets().clear();
    if (nightMode) {
      myScene.getStylesheets().add(
          getClass().getResource(DEFAULT_RESOURCE_PACKAGE + NIGHT_STYLESHEET).toExternalForm());
    } else {
      myScene.getStylesheets()
          .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + DAY_STYLESHEET).toExternalForm());
    }
  }

  /**
   * Places a Piece of a certain type at the specified coordinates
   *
   * @param coords Coordinates to place Piece at
   * @param type   Type of piece being placed
   */
  @Override
  public void placePiece(Collection<Coordinate> coords, CellState type) {
    for (Coordinate coord : coords) {
      myBoards.get(currentBoardIndex).setColorAt(coord.getRow(), coord.getColumn(),
          myColorMap.get(type));
    }
  }

  /**
   * Removes any Pieces that are at the coordinates contained in coords.
   *
   * @param coords Coordinates that contain pieces to remove
   */
  @Override
  public void removePiece(Collection<Coordinate> coords) {
    for (Coordinate coord : coords) {
      myBoards.get(currentBoardIndex).setColorAt(coord.getRow(), coord.getColumn(),
          myColorMap.get(CellState.WATER));
    }
  }

  /**
   * Displays winner screen when a player wins the game
   *
   * @param name Name of player who won
   */
  public void displayWinningScreen(String name) {
    WinnerScreen winnerScreen = new WinnerScreen(myResources, name, e -> notifyObserver(MAIN_MENU_OPERATION, ""));
    myScene.setRoot(winnerScreen);
  }

  /**
   * Displays a loser screen when a player loses.
   *
   * @param name Name of player who lost
   */
  public void displayLosingScreen(String name) {
    LoserScreen loser = new LoserScreen(myResources, name);
    loserStage.setScene(new Scene(loser, LOSER_SIZE, LOSER_SIZE));
    loserStage.show();
  }

  /**
   * Closes loser screen.
   */
  public void closeLoserStage() {
    loserStage.close();
  }

  /**
   * Wrapper function to update all of current player's data at once.
   *
   * @param shotsRemaining     number of shots the user has left in their turn
   * @param numPiecesRemaining number of pieces remaining
   * @param amountOfGold       gold that user has
   */
  public void updateLabels(int shotsRemaining, int numPiecesRemaining, int amountOfGold) {
    setNumShotsRemaining(shotsRemaining);
    setNumPiecesRemaining(numPiecesRemaining);
    setGold(amountOfGold);
  }

  /**
   * Updates the user's side-view to show which of the opponent's ships are still alive.
   *
   * @param pieceCoords Coordinates of Piece objects owned by the opponent that are still alive.
   */
  @Override
  public void updatePiecesLeft(Collection<Collection<Coordinate>> pieceCoords) {
    myPiecesLeft.set(currentBoardIndex, pieceCoords);
    piecesRemainingPane.updateShownPieces(pieceCoords);
  }

  /**
   * Updates the text that shows the user how many shots they have left in their turn.
   *
   * @param shotsRemaining number of shots the user has left in their turn
   */
  @Override
  public void setNumShotsRemaining(int shotsRemaining) {
    shotsRemainingLabel.changeDynamicText(String.valueOf(shotsRemaining));
  }

  /**
   * Updates the text that shows the user how much gold they currently have.
   *
   * @param amountOfGold gold that user has
   */
  @Override
  public void setGold(int amountOfGold) {
    goldLabel.changeDynamicText(String.valueOf(amountOfGold));
  }

  /**
   * Updates the text that shows how many living pieces the current player has left.
   *
   * @param numPiecesRemaining number of pieces remaining
   */
  @Override
  public void setNumPiecesRemaining(int numPiecesRemaining) {
    numPiecesLabel.changeDynamicText(String.valueOf(numPiecesRemaining));
  }

  /**
   * Opens the user shop window, allowing a user to buy shot upgrades for that turn using their
   * gold.
   */
  @Override
  public void openShop() {
    shopStage.setOnCloseRequest(e -> {
      shopButton.setDisable(false);
      Spark.stop();
    });
    ShopView shop = new ShopView(shopUsables);
    shop.addObserver(this);
    shopStage.setScene(shop.getMyScene());
    shopStage.show();
    shopButton.setDisable(true);
  }

  /**
   * Closes the user shop window. This should happen when the user buys a shot upgrade.
   */
  @Override
  public void closeShop() {
    shopStage.close();
    shopButton.setDisable(false);
  }

  /**
   * Displays an error with a message in a user-friendly way.
   *
   * @param errorMsg message to appear on error
   */
  @Override
  public void showError(String errorMsg) {
    Alert alert = DialogMaker.makeAlert(errorMsg, GAMEVIEW_ALERT_ID);
    alert.showAndWait();
  }

  /**
   * Displays a certain shot outcome on a certain cell on the user's outgoing shots board. For
   * example, if the user clicked cell (1,0) and an enemy ship was at that location, cell (1,0)
   * would turn red.
   *
   * @param x      x coordinate of cell
   * @param y      y coordinate of cell
   * @param result indicates if the shot was a hit or a miss
   */
  @Override
  public void displayShotAt(int x, int y, CellState result) {
    myBoards.get(currentBoardIndex)
        .setColorAt(x, y, myColorMap.get(result));
  }

  /**
   * Displays a brief animation of an explosion wherever a player makes a shot. This method is
   * intended to freeze the program and should be used with extra caution and only in places where
   * concurrent processes will not interfere with any UI elements.
   *
   * @param row      row index of shot
   * @param col      column index of shot
   * @param consumer code to be executed after shot animation finishes playing
   * @param id       ID of current player, to be consumed by consumer
   */
  public void displayShotAnimation(int row, int col, Consumer<Integer> consumer, int id) {
    ImageView explosion = new ImageView();
    explosion.setImage(
        new Image(
            getClass().getResource(DEFAULT_RESOURCE_PACKAGE + IMAGES_PATH + EXPLOSION_IMAGE_NAME)
                .toString(),
            true));
    myBoards.get(currentBoardIndex).displayExplosionOnCell(row, col, explosion);
    FadeTransition ft = new FadeTransition(new Duration(EXPLOSION_DURATION), explosion);
    ft.setFromValue(1);
    ft.setToValue(0);
    ft.setOnFinished(e -> {
      consumer.accept(id);
      myBoards.get(currentBoardIndex).removeExplosionImage(explosion);
    });
    ft.play();
  }

  /**
   * Moves the view to next player.
   *
   * @param name Name of next player
   */
  public void moveToNextPlayer(String name) {
    displayPassComputerMessage(name);
    setCurrentUsable(BASIC_SHOT_DYNAMIC_TEXT, new ArrayList<>(List.of(new Coordinate(0, 0))));
    closeShop();
  }

  /**
   * Updates all visual components of GameView when switching player's turn.
   *
   * @param boardList  List of this player's boards
   * @param idList     List of board ID's
   * @param pieceList  List of this player's pieces remaining
   * @param usableList List of this player's owned Usables
   */
  public void update(List<CellState[][]> boardList, List<Integer> idList,
      List<Collection<Collection<Coordinate>>> pieceList, List<UsableRecord> usableList) {
    myBoards.clear();
    myPiecesLeft = pieceList;
    currentBoardIndex = 0;
    int firstID = idList.get(currentBoardIndex);
    initializeBoards(boardList, idList);
    updateTitle(playerIDToNames.get(firstID));
    updateInventory(usableList);
    updateDisplayedBoard();
  }

  /**
   * Displays an alert indicating that an AI player has finished their turn, and provides informaion
   * on the AI's moves.
   *
   * @param id    ID of AI player
   * @param shots information about the AI player's shots
   */
  public void displayAIMove(int id, List<Info> shots) {
    StringBuilder message = new StringBuilder();
    for (Info shot : shots) {
      message.append(String.format(AI_SHOT_TEXT,
          id + 1, shot.row(), shot.col(), shot.ID() + 1)).append("\n");
    }
    Alert alert = new Alert(AlertType.INFORMATION, message.toString());
    Node alertNode = alert.getDialogPane();
    alertNode.setId(AI_ALERT_ID);
    alert.show();
  }
}
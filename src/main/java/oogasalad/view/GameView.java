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

public class GameView extends PropertyObservable implements PropertyChangeListener, BoardVisualizer,
    ShopVisualizer, ShotVisualizer, GameDataVisualizer {

  private static final Logger LOG = LogManager.getLogger(GameView.class);
  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String DAY_STYLESHEET = "stylesheets/viewStylesheet.css";
  private static final String NIGHT_STYLESHEET = "stylesheets/nightStylesheet.css";
  private static final String IMAGES_PATH = "images/";
  private static final String EXPLOSION_IMAGE_NAME = "explosion-icon.png"; // TODO: Get explosion image from resource bundle
  private static final String BOARD_CLICKED_LOG = "Board %d was clicked at row: %d, col: %d";
  private static final String BOARD_HOVERED_LOG = "Board %d was hovered over at row: %d, col: %d";
  private static final String CENTER_PANE_ID = "view-center-pane";
  private static final String VIEW_PANE_ID = "view-pane";
  private static final String INVALID_METHOD = "Invalid method name given";
  private static final String SHOT_METHOD = "handleShot";
  private static final double BOARD_SIZE = 30;
  private static final int EXPLOSION_DURATION = 1000;

  private static final String BOARD_INDEX_LOG = "Current board index: ";
  private static final String BOARD_SHOW_LOG = "Showing board ";
  private static final String CELL_CLICKED_SELF_LOG = "cellClickedSelf";
  private static final String WON_SUFFIX = "WonSuffix";
  private static final String LOST_SUFFIX = "LostSuffix";
  private static final String LOST_ALERT_ID = "player-lost-alert";

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

  private TitlePanel myTitle;
  private List<BoardView> myBoards;
  private List<Collection<Collection<Coordinate>>> myPiecesLeft;
  private BorderPane myPane;
  private VBox myCenterPane;
  private Label currentBoardLabel;
  private HBox boardButtonBox;
  private Button leftButton;
  private Button rightButton;
  private Button endTurnButton;
  private VBox myRightPane;
  private Button shopButton;
  private SetPiecePane piecesRemainingPane;
  private LegendPane pieceLegendPane;
  private ConfigPane configPane;
  private DynamicLabel currentUsableLabel;
  private DynamicLabel shotsRemainingLabel;
  private DynamicLabel numPiecesLabel;
  private DynamicLabel goldLabel;
  private AbstractScreen passComputerMessageView;
  private ResourceBundle myResources;
  private InventoryView inventory;
  private Stage loserStage;
  private Stage shopStage;
  private List<Usable> shopUsables;
  private Collection<Coordinate> currentUsableRelativeCoords;
  private boolean nightMode;
  private Scene myScene;
  private int currentBoardIndex;
  private Map<Integer, String> playerIDToNames;
  private Map<CellState, Color> myColorMap;

  public GameView(List<CellState[][]> firstPlayerBoards,
      Collection<Collection<Coordinate>> initialPiecesLeft, Map<Integer, String> idToNames, List<UsableRecord> firstPlayerUsables, Map<CellState, Color> colorMap, ResourceBundle resourceBundle) {
    myPane = new BorderPane();
    myPane.setId(VIEW_PANE_ID);
    nightMode = false;
    myColorMap = colorMap;
    myBoards = new ArrayList<>();
    myPiecesLeft = new ArrayList<>();
    currentBoardIndex = 0;
    currentUsableRelativeCoords = new ArrayList<>(Arrays.asList(new Coordinate(0, 0)));
    Platform.runLater(() -> {
      loserStage = new Stage();
      shopStage = new Stage();
    });
    playerIDToNames = idToNames;
    myResources = resourceBundle;
    initialize(firstPlayerBoards, initialPiecesLeft, firstPlayerUsables);
  }

  public void setShopUsables(List<Usable> usables) {
    shopUsables = usables;
  }

  private List<Integer> createInitialIDList(int numPlayers) {
    List<Integer> idList = new ArrayList<>();
    for (int i = 0; i < numPlayers; i++) {
      idList.add(i);
    }
    return idList;
  }

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

  private void createInventory() {
    inventory = new InventoryView();
    inventory.addObserver(this);
    myCenterPane.getChildren().add(inventory.getPane());
  }

  public void updateInventory(List<UsableRecord> usableList) {
    inventory.updateElements(usableList);
  }

  private void createPassMessageView() {
    passComputerMessageView = new PassComputerScreen(e -> switchToMainScreen(), myResources);
  }

  public void switchToMainScreen() {
    myScene.setRoot(myPane);
  }

  public void initializePiecesLeft(Collection<Collection<Coordinate>> piecesLeft) {
    for (int i = 0; i < myBoards.size(); i++) {
      myPiecesLeft.add(piecesLeft);
    }
    updatePiecesLeft(myPiecesLeft.get(currentBoardIndex));
  }

  private void initializeBoards(List<CellState[][]> boards, List<Integer> idList) {
    GameBoardView self = new SelfBoardView(BOARD_SIZE, boards.get(0), myColorMap, idList.get(0));
    myBoards.add(self);
    self.addObserver(this);
    for (int i = 1; i < boards.size(); i++) {
      GameBoardView enemy = new EnemyBoardView(BOARD_SIZE, boards.get(i), myColorMap, idList.get(i));
      myBoards.add(enemy);
      enemy.addObserver(this);
    }
  }

  public Scene createScene() {
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + DAY_STYLESHEET).toExternalForm());
    return myScene;
  }

  private void createRightPane() {
    shopButton = ButtonMaker.makeTextButton("view-shop-button", e -> openShop(), myResources.getString(OPEN_SHOP_RESOURCE));
    piecesRemainingPane = new SetPiecePane(20, myColorMap, myResources);
    piecesRemainingPane.setText(myResources.getString(SHIPS_REMAINING_RESOURCE));

    setupPieceLegendPane();

    shotsRemainingLabel = LabelMaker.makeDynamicLabel(myResources.getString(SHOTS_REMAINING_RESOURCE), "",
        "shots-remaining-label");
    numPiecesLabel = LabelMaker.makeDynamicLabel(myResources.getString(PIECES_LEFT_RESOURCE), "", "num-pieces-label");
    goldLabel = LabelMaker.makeDynamicLabel(myResources.getString(GOLD_LEFT_RESOURCE), "", "gold-label");

    configPane = new ConfigPane(myResources);
    configPane.setText(myResources.getString(CONFIG_TEXT_RESOURCE));
    configPane.setOnAction(e -> changeStylesheet());

    currentUsableLabel = LabelMaker.makeDynamicLabel(myResources.getString(CURRENT_USABLE_RESOURCE), "Basic Shot", "current-usable-label");

    myRightPane = BoxMaker.makeVBox("configBox", 0, Pos.TOP_CENTER, currentUsableLabel, shotsRemainingLabel,
        numPiecesLabel, goldLabel, shopButton,
        piecesRemainingPane, pieceLegendPane, configPane);
    myRightPane.setMinWidth(300);
    myPane.setRight(myRightPane);
  }

  public void setCurrentUsable(String id, Collection<Coordinate> usableCoords) {
    currentUsableLabel.changeDynamicText(id);
    currentUsableRelativeCoords = usableCoords;
  }

  private void setupPieceLegendPane() {
    pieceLegendPane = new LegendPane(myColorMap, myResources);
  }

  private void createCenterPane() {
    myCenterPane = BoxMaker.makeVBox(CENTER_PANE_ID, 20, Pos.CENTER);
    myPane.setCenter(myCenterPane);

    setupBoardLabel();
    myCenterPane.getChildren().add(myBoards.get(currentBoardIndex).getBoardPane());
    setupBoardButtons();
  }

  private void createTitlePanel() {
    myTitle = new TitlePanel("");
    updateTitle(playerIDToNames.get(myBoards.get(currentBoardIndex).getID()));
    myTitle.setId("game-title");
    myPane.setTop(myTitle);
  }


  private void setupBoardLabel() {
    currentBoardLabel = LabelMaker.makeLabel(myResources.getString(YOUR_BOARD_RESOURCE), "board-label");
    currentBoardLabel.setId("currentBoardLabel");
    myCenterPane.getChildren().add(currentBoardLabel);
  }

  private void setupBoardButtons() {
    leftButton = ButtonMaker.makeImageButton("left-button", e -> decrementBoardIndex(),
        IMAGES_PATH + "arrow-left.png", 50, 50);
    leftButton.getStyleClass().add("arrow-button");

    rightButton = ButtonMaker.makeImageButton("right-button", e -> incrementBoardIndex(),
        IMAGES_PATH + "arrow-right.png", 50, 50);
    rightButton.getStyleClass().add("arrow-button");

    endTurnButton = ButtonMaker.makeTextButton("end-turn-button", e -> endTurn(), myResources.getString(END_TURN_RESOURCE));
    endTurnButton.setDisable(true);
    boardButtonBox = BoxMaker.makeHBox("board-button-box", 20, Pos.CENTER, leftButton, rightButton, endTurnButton);

    myCenterPane.getChildren().add(boardButtonBox);
  }

  public void allowEndTurn() {
    endTurnButton.setDisable(false);
  }

  private void endTurn() {
    endTurnButton.setDisable(true);
    notifyObserver("endTurn", "");
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
            myBoards.get(currentBoardIndex).getID(), myResources.getString(PLAYER_PREFIX_RESOURCE) + (myBoards.get(currentBoardIndex).getID() + 1)));
    refreshCenterPane();
    updatePiecesLeft(myPiecesLeft.get(currentBoardIndex));
    LOG.info(BOARD_INDEX_LOG + currentBoardIndex);
    LOG.info(BOARD_SHOW_LOG + (myBoards.get(currentBoardIndex).getID() + 1));
  }

  private void refreshCenterPane() {
    myCenterPane.getChildren().clear();
    myCenterPane.getChildren()
        .addAll(currentBoardLabel, myBoards.get(currentBoardIndex).getBoardPane(), boardButtonBox, inventory.getPane());
  }

  private void updateTitle(String playerName) {
    myTitle.changeTitle(playerName + myResources.getString(TURN_SUFFIX_RESOURCE));
  }

  private void switchPlayerMessage(String nextPlayer) {
    passComputerMessageView.setLabelText(nextPlayer);
    System.out.println(nextPlayer);
    myScene.setRoot(passComputerMessageView);
  }

  private void buyItem(String itemID) {
    notifyObserver(new Object(){}.getClass().getEnclosingMethod().getName(), itemID);
  }

  public int getCurrentBoardIndex() {
    return currentBoardIndex;
  }

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
    // this is the ID of the Usable
    notifyObserver(new Object(){}.getClass().getEnclosingMethod().getName(), id);
  }

  private void cellClickedSelf(String clickInfo) {
    handleCellClicked(clickInfo);

  }

  private void cellClickedEnemy(String clickInfo) {
    handleCellClicked(clickInfo);
  }

  private void handleCellClicked(String clickInfo) {
    int row = Integer.parseInt(clickInfo.substring(0, clickInfo.indexOf(" ")));
    int col = Integer.parseInt(clickInfo.substring(clickInfo.indexOf(" ") + 1, clickInfo.lastIndexOf(" ")));
    int id = Integer.parseInt(clickInfo.substring(clickInfo.lastIndexOf(" ") + 1));
    LOG.info(String.format(BOARD_CLICKED_LOG, id, row, col));
    notifyObserver("applyUsable", clickInfo);
  }

  private void cellHoveredSelf(String hoverInfo) {
    
  }

  private void cellHoveredEnemy(String hoverInfo) {

  }

  private void cellExitedSelf(String exitedInfo) {

  }

  private void cellExitedEnemy(String exitedInfo) {

  }

  private void changeStylesheet() {
    nightMode = !nightMode;
    myScene.getStylesheets().clear();
    if (nightMode) {
      myScene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + NIGHT_STYLESHEET).toExternalForm());
    } else {
      myScene.getStylesheets().add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + DAY_STYLESHEET).toExternalForm());
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

  public void displayWinningScreen(String name) {
    WinnerScreen winnerScreen = new WinnerScreen(myResources, name);
    myScene.setRoot(winnerScreen);
  }

  public void displayLosingScreen(String name) {
    LoserScreen loser = new LoserScreen(myResources, name);
    loserStage.setScene(new Scene(loser, 600, 600));
    loserStage.show();
  }

  public void closeLoserStage() {
    loserStage.close();
  }

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
   * @param numPiecesRemaining number of pieces remaiing
   */
  @Override
  public void setNumPiecesRemaining(int numPiecesRemaining) {
    numPiecesLabel.changeDynamicText(String.valueOf(numPiecesRemaining));
  }

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

  @Override
  public void closeShop() {
    shopStage.close();
    shopButton.setDisable(false);
  }

  public void showError(String errorMsg) {
    Alert alert = DialogMaker.makeAlert(errorMsg, "gameview-alert");
    alert.showAndWait();
  }

  @Override
  public void displayShotAt(int x, int y, CellState result) {
    myBoards.get(currentBoardIndex)
        .setColorAt(x, y, myColorMap.get(result));
  }

  public void displayShotAnimation(int row, int col, Consumer<Integer> consumer, int id) {
    ImageView explosion = new ImageView();
    explosion.setImage(
        new Image(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + IMAGES_PATH + EXPLOSION_IMAGE_NAME).toString(),
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


  public void moveToNextPlayer(String name) {
    switchPlayerMessage(name);
    setCurrentUsable("Basic Shot", new ArrayList<>(Arrays.asList(new Coordinate(0, 0))));
    closeShop();
  }

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

  public void displayAIMove(int id, List<Info> shots) {
    String message = "";
    for (int i = 0; i < shots.size(); i++) {
      message += String.format("Player %d took a shot at row %d, column %d on player %d",
          id+1, shots.get(i).row(), shots.get(i).col(), shots.get(i).ID()+1)+"\n";
    }
    Alert alert = new Alert(AlertType.INFORMATION, message);
    Node alertNode = alert.getDialogPane();
    alertNode.setId("alert");
    alert.show();
  }
}
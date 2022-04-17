package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import oogasalad.PropertyObservable;
import oogasalad.model.players.EngineRecord;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

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
import oogasalad.view.maker.LabelMaker;
import oogasalad.view.panes.ConfigPane;
import oogasalad.view.panes.LegendPane;
import oogasalad.view.panes.SetPiecePane;
import oogasalad.view.panels.TitlePanel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameView extends PropertyObservable implements PropertyChangeListener, BoardVisualizer,
    ShopVisualizer, ShotVisualizer, GameDataVisualizer {

  private static final Logger LOG = LogManager.getLogger(GameView.class);
  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String DAY_STYLESHEET = "stylesheets/viewStylesheet.css";
  private static final String NIGHT_STYLESHEET = "stylesheets/nightStylesheet.css";
  private static final String CELL_STATE_RESOURCES_PATH = "/CellState";
  private static final String IMAGES_PATH = "images/";
  private static final String BOARD_CLICKED_LOG = "Board %d was clicked at row: %d col: %d";
  private static final String CENTER_PANE_ID = "view-center-pane";
  private static final String VIEW_PANE_ID = "view-pane";
  private static final double BOARD_SIZE = 50;


  public static ResourceBundle CELL_STATE_RESOURCES = ResourceBundle.getBundle(
      CELL_STATE_RESOURCES_PATH);
  public static final String FILL_PREFIX = "FillColor_";

  private TitlePanel myTitle;
  private List<BoardView> myBoards;
  private List<Collection<Collection<Coordinate>>> myPiecesLeft;
  private BorderPane myPane;
  private VBox myCenterPane;
  private Label currentBoardLabel;
  private HBox boardButtonBox;
  private Button leftButton;
  private Button rightButton;

  private VBox myRightPane;
  private Button shopButton;
  private SetPiecePane piecesRemainingPane;
  private LegendPane pieceLegendPane;
  private LegendPane markerLegendPane;
  private ConfigPane configPane;
  private DynamicLabel shotsRemainingLabel;
  private DynamicLabel healthLabel;
  private DynamicLabel goldLabel;
  private PassComputerMessageView passComputerMessageView;
  private boolean nightMode;

  private Scene myScene;

  private int currentBoardIndex;

  private Map<Integer, String> playerIDToNames;

  public GameView(List<CellState[][]> firstPlayerBoards,
      Collection<Collection<Coordinate>> initialPiecesLeft, Map<Integer, String> idToNames) {
    myPane = new BorderPane();
    myPane.setId(VIEW_PANE_ID);
    nightMode = false;

    myBoards = new ArrayList<>();
    myPiecesLeft = new ArrayList<>();
    currentBoardIndex = 0;
    playerIDToNames = idToNames;

    initializeBoards(firstPlayerBoards, createInitialIDList(firstPlayerBoards.size()));
    createCenterPane();
    createRightPane();
    createTitlePanel();
    createPassMessageView();
    initializePiecesLeft(initialPiecesLeft);
  }

  private List<Integer> createInitialIDList(int numPlayers) {
    List<Integer> idList = new ArrayList<>();
    for (int i = 0; i < numPlayers; i++) {
      idList.add(i);
    }
    return idList;
  }

  private void createPassMessageView() {
    passComputerMessageView = new PassComputerMessageView();
    passComputerMessageView.setButtonOnMouseClicked(e -> myScene.setRoot(myPane));
  }

  public void initializePiecesLeft(Collection<Collection<Coordinate>> piecesLeft) {
    for (int i = 0; i < myBoards.size(); i++) {
      myPiecesLeft.add(piecesLeft);
    }
    updatePiecesLeft(myPiecesLeft.get(currentBoardIndex));
  }

  private void initializeBoards(List<CellState[][]> boards, List<Integer> idList) {
    GameBoardView self = new SelfBoardView(BOARD_SIZE, boards.get(0), idList.get(0));
    myBoards.add(self);
    self.addObserver(this);
    for (int i = 1; i < boards.size(); i++) {
      GameBoardView enemy = new EnemyBoardView(BOARD_SIZE, boards.get(i), idList.get(i));
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
    shopButton = ButtonMaker.makeTextButton("view-shop", e -> openShop(), "Open Shop");

    piecesRemainingPane = new SetPiecePane(20);
    piecesRemainingPane.setText("Ships Remaining");

    setupPieceLegendPane();

    shotsRemainingLabel = LabelMaker.makeDynamicLabel("Shots Remaining: %s", "0",
        "shots-remaining-label");
    healthLabel = LabelMaker.makeDynamicLabel("Health: %s", "0", "health-label");
    goldLabel = LabelMaker.makeDynamicLabel("Gold: %s", "0", "gold-label");

    configPane = new ConfigPane();
    configPane.setOnAction(e -> changeStylesheet());

    myRightPane = BoxMaker.makeVBox("configBox", 0, Pos.TOP_CENTER, shotsRemainingLabel,
        healthLabel, goldLabel, shopButton,
        piecesRemainingPane, pieceLegendPane, configPane);
    myRightPane.setMinWidth(300);
    myPane.setRight(myRightPane);
  }


  private void setupPieceLegendPane() {
    LinkedHashMap<String, Color> colorMap = new LinkedHashMap<>();
    for (CellState state : CellState.values()) {
      colorMap.put(state.name(),
          Color.valueOf(CELL_STATE_RESOURCES.getString(FILL_PREFIX + state.name())));
    }
    pieceLegendPane = new LegendPane(colorMap);
  }

  private void createCenterPane() {
    myCenterPane = BoxMaker.makeVBox(CENTER_PANE_ID, 20, Pos.CENTER);
    myPane.setCenter(myCenterPane);

    setupBoardLabel();
    myCenterPane.getChildren().add(myBoards.get(currentBoardIndex).getBoardPane());
    setupBoardButtons();
  }

  private void createTitlePanel() {
    myTitle = new TitlePanel("Player 1's Turn");
    myTitle.setId("game-title");
    myPane.setTop(myTitle);
  }


  private void setupBoardLabel() {
    currentBoardLabel = LabelMaker.makeLabel("Your Board", "board-label");
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

    boardButtonBox = BoxMaker.makeHBox("board-button-box", 20, Pos.CENTER, leftButton, rightButton);

    myCenterPane.getChildren().add(boardButtonBox);
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
    LOG.info("Current board index: " + currentBoardIndex);
    currentBoardLabel.setText(currentBoardIndex == 0 ? "Your Board"
        : "Your Shots Against " + playerIDToNames.getOrDefault(
            myBoards.get(currentBoardIndex).getID(),
            "Player " + (myBoards.get(currentBoardIndex).getID() + 1)));
    refreshCenterPane();
    updatePiecesLeft(myPiecesLeft.get(currentBoardIndex));
    LOG.info("Current board index: " + currentBoardIndex);
    LOG.info("Showing board " + (myBoards.get(currentBoardIndex).getID() + 1));
  }

  private void refreshCenterPane() {
    myCenterPane.getChildren().clear();
    myCenterPane.getChildren()
        .addAll(currentBoardLabel, myBoards.get(currentBoardIndex).getBoardPane(), boardButtonBox);
  }

  private void updateTitle(String playerName) {
    myTitle.changeTitle(playerName + "'s turn");
  }

  private void switchPlayerMessage(String nextPlayer) {
    passComputerMessageView.setPlayerName(nextPlayer);
    myScene.setRoot(passComputerMessageView);
  }


  public int getCurrentBoardIndex() {
    return currentBoardIndex;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    int id = ((Info) evt.getNewValue()).ID();
    int row = ((Info) evt.getNewValue()).row();
    int col = ((Info) evt.getNewValue()).col();
    LOG.info("Method name: " + evt.getPropertyName());
    LOG.info(String.format(BOARD_CLICKED_LOG, id, row, col));
    notifyObserver(evt.getPropertyName(), evt.getNewValue());
  }

  private void changeStylesheet() {
    nightMode = !nightMode;

    if (nightMode) {
      myScene.getStylesheets().clear();
      myScene.getStylesheets()
          .add(
              getClass().getResource(DEFAULT_RESOURCE_PACKAGE + NIGHT_STYLESHEET).toExternalForm());
    } else {
      myScene.getStylesheets().clear();
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
  public void placePiece(Collection<Coordinate> coords,
      CellState type) { //TODO: Change type to some enum
    for (Coordinate coord : coords) {
      myBoards.get(currentBoardIndex).setColorAt(coord.getRow(), coord.getColumn(),
          Color.valueOf(CELL_STATE_RESOURCES.getString(FILL_PREFIX + type.name())));
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
          Color.valueOf(CELL_STATE_RESOURCES.getString(CellState.WATER.name())));
    }
  }

  public void displayWinningMessage(int id) {
    LOG.info("Player " + (id + 1) + " Won!");
  }

  public void displayLosingMessage(int id) {
    LOG.info("Player " + (id + 1) + " Lost!");
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

//  /**
//   * Updates the text that shows the user whose turn it currently is.
//   *
//   * @param playerName name or ID of player whose turn it is
//   */
//  @Override
//  public void setPlayerTurnIndicator(String playerName) {
//
//  }

  /**
   * Updates the text that shows how much health the current player has left.
   *
   * @param healthRemaining amount of health points remaiing
   */
  @Override
  public void setHealthRemaining(int healthRemaining) {
    healthLabel.changeDynamicText(String.valueOf(healthRemaining));
  }

  @Override
  public void openShop() {
    System.out.println("Shop Opened");
  }

  @Override
  public void closeShop() {

  }

  public void showError(String errorMsg) {
    Alert alert = new Alert(AlertType.ERROR, errorMsg);
    Node alertNode = alert.getDialogPane();
    alertNode.setId("alert");
    alert.showAndWait();
  }

  @Override
  public void displayShotAt(int x, int y, CellState result) {
    myBoards.get(currentBoardIndex)
        .setColorAt(x, y, Color.valueOf(CELL_STATE_RESOURCES.getString(FILL_PREFIX + result.name())));
  }

  public void moveToNextPlayer(List<CellState[][]> boardList, List<Integer> idList,
      List<Collection<Collection<Coordinate>>> pieceList) {
    switchPlayerMessage(" " + (idList.get(0) + 1));
    myBoards.clear();
    myPiecesLeft = pieceList;
    currentBoardIndex = 0;
    int firstID = idList.get(currentBoardIndex);
    initializeBoards(boardList, idList);
    updateTitle(playerIDToNames.get(firstID));
    updateDisplayedBoard();
  }

//  public void updateCurrentPlayerName(String name) {
//    updateTitle(name);
//

  public void displayAIMove(EngineRecord move, int id) {
    String message = String.format("Player %d took a show at row %d, column %d on player %d",
        id + 1, move.shot().getRow(), move.shot().getColumn(), move.enemyID() + 1);
    Alert alert = new Alert(AlertType.INFORMATION, message);
    Node alertNode = alert.getDialogPane();
    alertNode.setId("alert");
    alert.show();
  }

}
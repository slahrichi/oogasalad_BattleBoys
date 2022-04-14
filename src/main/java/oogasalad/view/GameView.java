package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import javafx.geometry.Insets;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

import oogasalad.view.board.BoardView;
import oogasalad.view.board.EnemyBoardView;
import oogasalad.view.board.GameBoardView;
import oogasalad.view.board.SelfBoardView;
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
  private static final String STYLESHEET = "viewStylesheet.css";
  private static final String CELLSTATE_RESOURCES_PATH = "/CellState";
  private static final String MARKER_RESOURCES_PATH = "/Markers";

  public static ResourceBundle CELL_STATE_RESOURCES = ResourceBundle.getBundle(
      CELLSTATE_RESOURCES_PATH);;
  public static ResourceBundle MARKER_RESOURCES = ResourceBundle.getBundle(
      MARKER_RESOURCES_PATH);
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
  private LegendPane legendPane;
  private Label shotsRemainingLabel;
  private Label healthLabel;
  private Label goldLabel;
  private PassComputerMessageView passComputerMessageView;

  private Scene myScene;

  private int currentBoardIndex;

  public GameView(List<CellState[][]> firstPlayerBoards, Collection<Collection<Coordinate>> coords) {
    myPane = new BorderPane();
    myPane.setBackground(
        new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    myPane.setId("view-pane");

    myBoards = new ArrayList<>();
    myPiecesLeft = new ArrayList<>();
    currentBoardIndex = 0;
    initializeFirstPlayerBoards(firstPlayerBoards);
    initializePiecesLeft(coords);
    createCenterPane();
    createRightPane();
    createTitlePanel();
    createPassMessageView();
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

  private void initializeFirstPlayerBoards(List<CellState[][]> boards) {
    myBoards.add(new SelfBoardView(50, boards.get(0), 0));
    for (int i = 1; i < boards.size(); i++) {
      GameBoardView enemy = new EnemyBoardView(50, boards.get(i), i);
      myBoards.add(enemy);
      enemy.addObserver(this);
    }
  }

  public Scene createScene() {
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
    return myScene;
  }

  public void createRightPane() {
    shopButton = new Button("Open Shop");
    shopButton.setId("view-shop");
    shopButton.setFont(new Font(15));
    shopButton.setOnMouseClicked(e -> openShop());

    piecesRemainingPane = new SetPiecePane(20);
    piecesRemainingPane.setText("Ships Remaining");
    setupLegendPane();
    shotsRemainingLabel = new Label("Shots Remaining: 0");
    shotsRemainingLabel.setFont(new Font(25));
    healthLabel = new Label("Health: 0");
    healthLabel.setFont(new Font(25));
    goldLabel = new Label("Gold: 0");
    goldLabel.setFont(new Font(25));

    myRightPane = new VBox(shotsRemainingLabel, healthLabel, goldLabel, shopButton,
        piecesRemainingPane, legendPane);
    myRightPane.setId("configBox");
    myRightPane.setSpacing(20);
    myRightPane.setAlignment(Pos.CENTER);
    myRightPane.setMinWidth(300);
    myPane.setRight(myRightPane);
  }

  private void setupLegendPane() {
    LinkedHashMap<String, Color> colorMap = new LinkedHashMap<>();
    for(CellState state : CellState.values()) {
      colorMap.put(state.name(), Color.valueOf(FILL_PREFIX + CELL_STATE_RESOURCES.getString(state.name())));
    }
    legendPane = new LegendPane(colorMap);
  }

  private void createCenterPane() {
    myCenterPane = new VBox();
    myCenterPane.setId("view-center-pane");
    myCenterPane.setSpacing(20);
    myCenterPane.setAlignment(Pos.CENTER);
    myPane.setCenter(myCenterPane);
    setupBoardLabel();
    myCenterPane.getChildren().add(myBoards.get(currentBoardIndex).getBoardPane());
    setupBoardButtons();
  }

  private void createTitlePanel() {
    myTitle = new TitlePanel("Player 1's Turn");
    myPane.setTop(myTitle);
  }

  private void setupBoardLabel() {
    currentBoardLabel = new Label("Your Board");
    currentBoardLabel.setId("currentBoardLabel");
    myCenterPane.getChildren().add(currentBoardLabel);
  }

  private void setupBoardButtons() {
    boardButtonBox = new HBox();
    boardButtonBox.setId("board-button-box");
    boardButtonBox.setSpacing(20);
    boardButtonBox.setAlignment(Pos.CENTER);

    leftButton = new Button("<-");
    leftButton.setId("left-button");
    leftButton.setFont(new Font(25));
    leftButton.setOnMouseClicked(e -> decrementBoardIndex());

    rightButton = new Button("->");
    rightButton.setId("right-button");
    rightButton.setFont(new Font(25));
    rightButton.setOnMouseClicked(e -> incrementBoardIndex());

    boardButtonBox.getChildren().addAll(leftButton, rightButton);
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
    System.out.println("Current board index: "+currentBoardIndex);
    currentBoardLabel.setText(currentBoardIndex == 0 ? "Your Board"
        : "Your Shots Against Player " + (myBoards.get(currentBoardIndex).getID() + 1));
    refreshCenterPane();
    updatePiecesLeft(myPiecesLeft.get(currentBoardIndex));
    LOG.info("Current board index: "+currentBoardIndex);
    LOG.info("Showing board " + (myBoards.get(currentBoardIndex).getID()+1));
  }

  private void refreshCenterPane() {
    myCenterPane.getChildren().clear();
    myCenterPane.getChildren()
        .addAll(currentBoardLabel, myBoards.get(currentBoardIndex).getBoardPane(), boardButtonBox);
  }

  private void updateTitle(int playerID) {
    myTitle.changeTitle("Player " + (playerID+1) + "'s turn");
  }

  private void switchPlayerMessage(String nextPlayer) {
    passComputerMessageView.setPlayerName(nextPlayer);
    myScene.setRoot(passComputerMessageView);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName(), evt.getNewValue());
  }

  /**
   * Places a Piece of a certain type at the specified coordinates
   *
   * @param coords Coordinates to place Piece at
   * @param type   Type of piece being placed
   */
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
  public void removePiece(Collection<Coordinate> coords) {
    for (Coordinate coord : coords) {
      myBoards.get(currentBoardIndex).setColorAt(coord.getRow(), coord.getColumn(),
          Color.valueOf(CELL_STATE_RESOURCES.getString(CellState.WATER.name())));
    }
  }

  public void displayWinningMessage(int id) {
    LOG.info("Player "+(id+1)+" Won!");
  }

  public void displayLosingMessage(int id) {
    LOG.info("Player "+(id+1)+" Lost!");
  }

  @Override
  public void updatePiecesLeft(Collection<Collection<Coordinate>> pieceCoords) {
    myPiecesLeft.set(currentBoardIndex, pieceCoords);
    piecesRemainingPane.updateShownPieces(pieceCoords);
  }

  @Override
  public void setNumShotsRemaining(int shotsRemaining) {
    shotsRemainingLabel.setText("Shots Remaining: " + shotsRemaining);
  }

  @Override
  public void setGold(int amountOfGold) {
    goldLabel.setText("Gold: " + amountOfGold);
  }

  @Override
  public void setPlayerTurnIndicator(String playerName) {

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
        .setColorAt(x, y, Color.valueOf(MARKER_RESOURCES.getString(FILL_PREFIX + result.name())));
  }

  public void moveToNextPlayer(List<CellState[][]> boardList, List<Integer> idList, List<Collection<Collection<Coordinate>>> pieceList) {
    switchPlayerMessage("Player "+(idList.get(0)+1));
    myBoards.clear();
    myPiecesLeft = pieceList;
    currentBoardIndex = 0;
    CellState[][] firstBoard = boardList.get(currentBoardIndex);
    int firstID = idList.get(currentBoardIndex);
    myBoards.add(new SelfBoardView(50, firstBoard, firstID));
    for (int i = 1; i < boardList.size(); i++) {
      GameBoardView enemy = new EnemyBoardView(50, boardList.get(i), idList.get(i));
      myBoards.add(enemy);
      enemy.addObserver(this);
    }
    updateTitle(firstID);
    updateDisplayedBoard();
  }
}
package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
  private static final String STYLESHEET = "mainStylesheet.css";

  private TitlePanel myTitle;
  private ResourceBundle myCellStateResources;
  private ResourceBundle myMarkerResources;
  private static final String FILL_PREFIX = "FillColor_";

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

  private Scene myScene;

  private int currentBoardIndex;

  public GameView(List<CellState[][]> firstPlayerBoards) {
    myCellStateResources = ResourceBundle.getBundle("/CellState");
    myMarkerResources = ResourceBundle.getBundle("/Markers");

    myPane = new BorderPane();
    myPane.setBackground(
        new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    myPane.setId("view-pane");

    myBoards = new ArrayList<>();
    myPiecesLeft = new ArrayList<>();
    currentBoardIndex = 0;
    initializeFirstPlayerBoards(firstPlayerBoards);
    createCenterPane();
    createRightPane();
    createTitlePanel();
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
    myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
    return myScene;
  }

  public void createRightPane() {
    shopButton = new Button("Open Shop");
    shopButton.setFont(new Font(15));
    shopButton.setOnMouseClicked(e -> openShop());

    piecesRemainingPane = new SetPiecePane(20);
    piecesRemainingPane.setText("Ships Remaining");
    legendPane = new LegendPane();
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
    currentBoardLabel.setAlignment(Pos.CENTER);
    currentBoardLabel.setTextAlignment(TextAlignment.CENTER);
    currentBoardLabel.setFont(new Font(50));
    myCenterPane.getChildren().add(currentBoardLabel);
  }

  private void setupBoardButtons() {
    boardButtonBox = new HBox();
    boardButtonBox.setSpacing(20);
    boardButtonBox.setAlignment(Pos.CENTER);

    leftButton = new Button("<-");
    leftButton.setFont(new Font(25));
    leftButton.setOnMouseClicked(e -> decrementBoardIndex());

    rightButton = new Button("->");
    rightButton.setFont(new Font(25));
    rightButton.setOnMouseClicked(e -> incrementBoardIndex());

    boardButtonBox.getChildren().addAll(leftButton, rightButton);
    myCenterPane.getChildren().add(boardButtonBox);
  }

  private void handleKeyInput(KeyCode code) {
    if (code == KeyCode.LEFT) {
      System.out.println("Left pressed");
      decrementBoardIndex();
    } else if (code == KeyCode.RIGHT) {
      System.out.println("Right pressed");
      incrementBoardIndex();
    }
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
    currentBoardLabel.setText(currentBoardIndex == 0 ? "Your Board"
        : "Your Shots Against Player " + (myBoards.get(currentBoardIndex).getID() + 1));
    refreshCenterPane();
    updatePiecesLeft(myPiecesLeft.get(currentBoardIndex));
    LOG.info("Showing board " + (myBoards.get(currentBoardIndex).getID()+1));
  }

  private void refreshCenterPane() {
    myCenterPane.getChildren().clear();
    myCenterPane.getChildren()
        .addAll(currentBoardLabel, myBoards.get(currentBoardIndex).getBoardPane(), boardButtonBox);
  }

  public void promptPlayTurn() {
    System.out.println("Please play turn! Maybe we should show a player ID here!");
  }

  private void updateTitle(int playerID) {
    myTitle.changeTitle("Player " + (playerID+1) + "'s turn");
  }

  public void switchPlayerMessage(String nextPlayer) {

    Alert alert = new Alert(AlertType.INFORMATION,
        "Pass the computer to the next player: " + nextPlayer + ". Proceed when the "
            + "next player is ready.");
    Node alertNode = alert.getDialogPane();
    alertNode.setId("switchAlert");
    alert.showAndWait();


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
          Color.valueOf(myCellStateResources.getString(FILL_PREFIX + type.name())));
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
          Color.valueOf(myCellStateResources.getString(CellState.WATER.name())));
    }
  }

  public void endGame() {

  }

  @Override
  public void updatePiecesLeft(Collection<Collection<Coordinate>> pieceCoords) {
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
        .setColorAt(x, y, Color.valueOf(myMarkerResources.getString(FILL_PREFIX + result.name())));
  }

  public void moveToNextPlayer(List<CellState[][]> boardList, List<Integer> idList) {
    switchPlayerMessage("Player "+(idList.get(0)+1));
    myBoards.clear();
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

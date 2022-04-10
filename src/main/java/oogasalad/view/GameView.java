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
import oogasalad.model.utilities.tiles.enums.Marker;
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
  private static final double SCREEN_WIDTH = 900;
  private static final double SCREEN_HEIGHT = 600;
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "mainStylesheet.css";

  private TitlePanel myTitle;
  private ResourceBundle myCellStateResources;
  private ResourceBundle myMarkerResources;
  private static final String FILL_PREFIX = "FillColor_";

  private Label titleName;
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
    myPane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
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
    myRightPane.setSpacing(20);
    myRightPane.setAlignment(Pos.CENTER);
    myRightPane.setMinWidth(300);
    myRightPane.setId("configBox");
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

  private void createTitlePanel(){
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

  private void createBoards() {
//    List<Coordinate> coordinateList = new ArrayList<>(Arrays.asList(new Coordinate(0, 1),
//        new Coordinate(1, 0), new Coordinate(1, 1)));
//    List<ShipCell> dummyShipCellList = new ArrayList<>();
//    dummyShipCellList.add(new ShipCell(1, new Coordinate(0, 1), 0, "0"));
//    dummyShipCellList.add(new ShipCell(1, new Coordinate(1, 0), 0, "1"));
//    dummyShipCellList.add(new ShipCell(1, new Coordinate(1, 1), 0, "2"));
//    StaticPiece dummyShip = new StaticPiece(dummyShipCellList, coordinateList, "0");
//
//    List<Coordinate> coordinateList2 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
//        new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 2)));
//    List<ShipCell> dummyShipCellList2 = new ArrayList<>();
//    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0, 0), 0, "0"));
//    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1, 0), 0, "1"));
//    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1, 1), 0, "2"));
//    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0, 1), 0, "3"));
//    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0, 2), 0, "4"));
//    StaticPiece dummyShip2 = new StaticPiece(dummyShipCellList2, coordinateList2, "0");
//
//    List<Coordinate> coordinateList3 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
//        new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 0), new Coordinate(3, 0)));
//    List<ShipCell> dummyShipCellList3 = new ArrayList<>();
//    dummyShipCellList3.add(new ShipCell(1, new Coordinate(0, 0), 0, "0"));
//    dummyShipCellList3.add(new ShipCell(1, new Coordinate(1, 0), 0, "1"));
//    dummyShipCellList3.add(new ShipCell(1, new Coordinate(1, 1), 0, "2"));
//    dummyShipCellList3.add(new ShipCell(1, new Coordinate(2, 0), 0, "3"));
//    dummyShipCellList3.add(new ShipCell(1, new Coordinate(3, 0), 0, "4"));
//    StaticPiece dummyShip3 = new StaticPiece(dummyShipCellList3, coordinateList3, "0");
//
//    List<Coordinate> coordinateList4 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
//        new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3), new Coordinate(0, 4)));
//    List<ShipCell> dummyShipCellList4 = new ArrayList<>();
//    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0, 0), 0, "0"));
//    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0, 1), 0, "1"));
//    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0, 2), 0, "2"));
//    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0, 3), 0, "3"));
//    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0, 4), 0, "4"));
//    StaticPiece dummyShip4 = new StaticPiece(dummyShipCellList4, coordinateList4, "0");

//    CellState[][] arrayLayout = new CellState[8][8];
//    for (CellState[] ints : arrayLayout) {
//      Arrays.fill(ints, CellState.WATER);
//    }
//
//    // player's own board, no listeners on it
//    SelfBoardView board1 = new SelfBoardView(50, myData.players().get(0).getBoard()
//        .getCurrentBoardState(), 1);
//    myBoards.add(board1);
//
//    GameBoardView board2 = new EnemyBoardView(50, arrayLayout, 2);
//    board2.addObserver(this);
//    myBoards.add(board2);
//    board2.setColorAt(4, 3, Color.YELLOW);
//    board2.setColorAt(6, 3, Color.YELLOW);
//    board2.setColorAt(7, 5, Color.YELLOW);
//
//
//    GameBoardView board3 = new EnemyBoardView(50, arrayLayout, 3);
//    board3.addObserver(this);
//    myBoards.add(board3);
//    board3.setColorAt(2, 2, Color.YELLOW);
//    board3.setColorAt(3, 6, Color.ORANGE);
//    board3.setColorAt(4, 6, Color.ORANGE);
//
//    GameBoardView board4 = new EnemyBoardView(50, arrayLayout, 4);
//    board4.addObserver(this);
//    myBoards.add(board4);
//    board4.setColorAt(5, 3, Color.YELLOW);
//    board4.setColorAt(2, 3, Color.YELLOW);
//    board4.setColorAt(7, 7, Color.RED);
//    board4.setColorAt(7, 6, Color.RED);
//    board4.setColorAt(6, 7, Color.RED);
  }

  public void promptPlayTurn() {
    System.out.println("Please play turn! Maybe we should show a player ID here!");
  }

  private void updateTitle(int playerID) {
    myTitle.changeTitle("Player " + (playerID+1) + "'s turn");
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
//    for (Piece piece : pieces) {
//      pieceCoords.add(piece.getRelativeCoords());
//    }
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
    CellState[][] board = boardList.get(1);
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        System.out.println("row: "+i+" col: " +j+" "+board[i][j].name());
      }
    }
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

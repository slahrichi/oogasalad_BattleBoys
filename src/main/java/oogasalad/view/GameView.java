package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.view.board.BoardView;
import oogasalad.view.board.EnemyBoardView;
import oogasalad.view.board.GameBoardView;
import oogasalad.view.board.SelfBoardView;
import oogasalad.view.board.BoardShapeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GameView extends PropertyObservable implements PropertyChangeListener, BoardVisualizer, ShopVisualizer, ShotVisualizer, GameDataVisualizer {

  private static final Logger LOG = LogManager.getLogger(GameView.class);
  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "setupStylesheet.css";

  private HBox myTitle;
  private Label titleName;
  private List<BoardView> myBoards;
  private BorderPane myPane;
  private VBox myCenterPane;
  private Label currentBoardLabel;
  private Scene myScene;

  private int currentBoardIndex;

  public GameView() {

    myPane = new BorderPane();
    myPane.setId("view-pane");
    createTitle();

    myBoards = new ArrayList<>();

    currentBoardIndex = 0;
    createCenterPane();
  }

  private void createTitle() {
    myTitle = new HBox();
    myTitle.setId("titleBox");
    titleName = new Label("OOGASalad Battleship");
    myTitle.getChildren().add(titleName);
    titleName.setId("titleText");
    myPane.setTop(myTitle);
  }

  public Scene createScene() {
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
    return myScene;
  }

  private void createCenterPane() {
    createBoards();

    myCenterPane = new VBox();
    myCenterPane.setId("view-center-pane");
    myCenterPane.setSpacing(20);
    myCenterPane.setAlignment(Pos.CENTER);

    currentBoardLabel = new Label("Your Board");
    currentBoardLabel.setAlignment(Pos.CENTER);
    currentBoardLabel.setTextAlignment(TextAlignment.CENTER);
    currentBoardLabel.setFont(new Font(50));
    myPane.setCenter(myCenterPane);
    updateDisplayedBoard();
  }

  private void handleKeyInput(KeyCode code) {
    if(code == KeyCode.LEFT) {
      currentBoardIndex = (currentBoardIndex + myBoards.size() - 1) % myBoards.size();
    } else if (code == KeyCode.RIGHT) {
      currentBoardIndex = (currentBoardIndex + myBoards.size() + 1) % myBoards.size();
    }
    LOG.info("Showing board " + currentBoardIndex);
    updateDisplayedBoard();
  }

  // Displays the board indicated by the updated value of currentBoardIndex
  private void updateDisplayedBoard() {
    StackPane boardToDisplay = myBoards.get(currentBoardIndex).getBoardPane();
    currentBoardLabel.setText(currentBoardIndex == 0 ? "Your Board" : "Your Shots Against Player " + (currentBoardIndex + 1));
    myCenterPane.getChildren().clear();
    myCenterPane.getChildren().addAll(currentBoardLabel, boardToDisplay);
  }

  public void showGame() {

  }

  private void createBoards() {
    List<Coordinate> coordinateList = new ArrayList<>(Arrays.asList(new Coordinate(0, 1),
        new Coordinate(1, 0), new Coordinate(1, 1)));
    List<ShipCell> dummyShipCellList = new ArrayList<>();
    dummyShipCellList.add(new ShipCell(1, new Coordinate(0,1), 0, "0"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    StaticPiece dummyShip = new StaticPiece(dummyShipCellList, coordinateList, "0");

    List<Coordinate> coordinateList2 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
        new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 2)));
    List<ShipCell> dummyShipCellList2 = new ArrayList<>();
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0,0), 0, "0"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0,1), 0, "3"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0,2), 0, "4"));
    StaticPiece dummyShip2 = new StaticPiece(dummyShipCellList2, coordinateList2, "0");

    List<Coordinate> coordinateList3 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
        new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 0), new Coordinate(3, 0)));
    List<ShipCell> dummyShipCellList3 = new ArrayList<>();
    dummyShipCellList3.add(new ShipCell(1, new Coordinate(0,0), 0, "0"));
    dummyShipCellList3.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList3.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    dummyShipCellList3.add(new ShipCell(1, new Coordinate(2,0), 0, "3"));
    dummyShipCellList3.add(new ShipCell(1, new Coordinate(3,0), 0, "4"));
    StaticPiece dummyShip3 = new StaticPiece(dummyShipCellList3, coordinateList3, "0");

    List<Coordinate> coordinateList4 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
        new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3), new Coordinate(0, 4)));
    List<ShipCell> dummyShipCellList4 = new ArrayList<>();
    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,0), 0, "0"));
    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,1), 0, "1"));
    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,2), 0, "2"));
    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,3), 0, "3"));
    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,4), 0, "4"));
    StaticPiece dummyShip4 = new StaticPiece(dummyShipCellList4, coordinateList4, "0");

    int[][] arrayLayout = new int[8][8];
    for(int i = 0; i < arrayLayout.length; i++){
      Arrays.fill(arrayLayout[i], 1);
    }

    // player's own board, no listeners on it
    SelfBoardView board1 = new SelfBoardView(new BoardShapeType(250, 250), arrayLayout, 1);
    myBoards.add(board1);
    board1.setColorAt(1, 1, Color.RED);
    board1.setColorAt(2, 1, Color.RED);
    board1.setColorAt(1, 2, Color.RED);
    board1.setColorAt(1, 3, Color.RED);
    board1.setColorAt(5, 5, Color.BLACK);
    board1.setColorAt(5, 6, Color.BLACK);
    board1.setColorAt(5, 7, Color.BLACK);


    // create the last board with a different array layout
    GameBoardView board2 = new EnemyBoardView(new BoardShapeType(250, 250), arrayLayout, 2);
    board2.addObserver(this);
    myBoards.add(board2);
    board2.setColorAt(4,3, Color.YELLOW);
    board2.setColorAt(6, 3, Color.YELLOW);
    board2.setColorAt(7, 5, Color.YELLOW);


    GameBoardView board3 = new EnemyBoardView(new BoardShapeType(250, 250), arrayLayout, 3);
    board3.addObserver(this);
    myBoards.add(board3);
    board3.setColorAt(2, 2, Color.YELLOW);
    board3.setColorAt(3, 6, Color.ORANGE);
    board3.setColorAt(4, 6, Color.ORANGE);

    GameBoardView board4 = new SelfBoardView(new BoardShapeType(250, 250), arrayLayout, 4);
    board4.addObserver(this);
    myBoards.add(board4);
    board4.setColorAt(5,3, Color.YELLOW);
    board4.setColorAt(2,3, Color.YELLOW);
    board4.setColorAt(7,7, Color.RED);
    board4.setColorAt(7, 6, Color.RED);
    board4.setColorAt(6, 7, Color.RED);
  }

  public void promptPlayTurn() {
    System.out.println("Please play turn! Maybe we should show a player ID here!");
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName(), evt.getNewValue());
  }

  /**
   * Places a Piece of a certain type at the specified coordinates
   * @param coords Coordinates to place Piece at
   * @param type Type of piece being placed
   */
  public void placePiece(Collection<Coordinate> coords, String type) { //TODO: Change type to some enum
    for(Coordinate coord : coords) {
      myBoards.get(currentBoardIndex).setColorAt(coord.getRow(), coord.getColumn(), Color.BLACK);
    }
  }

  /**
   * Removes any Pieces that are at the coordinates contained in coords.
   * @param coords Coordinates that contain pieces to remove
   */
  public void removePiece(Collection<Coordinate> coords) {
    for(Coordinate coord : coords) {
      myBoards.get(currentBoardIndex).setColorAt(coord.getColumn(), coord.getRow(), Color.LIGHTBLUE);
    }
  }

  @Override
  public void updateShipsLeft(Collection<Piece> pieces) {

  }

  @Override
  public void setNumShotsRemaining(int shotsRemaining) {

  }

  @Override
  public void setGold(int amountOfGold) {

  }

  @Override
  public void setPlayerTurnIndicator(String playerName) {

  }

  @Override
  public void openShop() {

  }

  @Override
  public void closeShop() {

  }

  @Override
  public void displayShotAt(int x, int y, boolean wasHit) { //TODO: Change wasHit to an enumerated result type
    Color newColor = wasHit ? Color.RED : Color.YELLOW;
    myBoards.get(currentBoardIndex).setColorAt(x, y, newColor);
  }
}

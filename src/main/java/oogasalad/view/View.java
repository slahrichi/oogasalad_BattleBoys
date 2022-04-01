package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.view.board.BoardView;
import oogasalad.view.board.EnemyBoardView;
import oogasalad.view.board.GameBoardView;
import oogasalad.view.board.SelfBoardView;
import oogasalad.view.board.ShapeType;

public class View extends PropertyObservable implements PropertyChangeListener, BoardVisualizer, ShopVisualizer, ShotVisualizer, GameDataVisualizer {

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;

  private List<BoardView> myBoards;
  private BorderPane myPane;
  private StackPane myCenterPane;
  private Scene myScene;

  private int currentBoardIndex;

  public View() {
    myPane = new BorderPane();
    myCenterPane = new StackPane();
    myPane.setCenter(myCenterPane);

    myBoards = new ArrayList<>();
    createBoards(3);
    currentBoardIndex = 0;
    updateDisplayedBoard();
  }

  public Scene createViewFromPlayers(List<Player> playerList) {
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    return myScene;
  }

  private void handleKeyInput(KeyCode code) {
    if(code == KeyCode.LEFT) {
      currentBoardIndex = (currentBoardIndex + myBoards.size() - 1) % myBoards.size();
    } else if (code == KeyCode.RIGHT) {
      currentBoardIndex = (currentBoardIndex + myBoards.size() + 1) % myBoards.size();
    }
    System.out.println("Showing board " + currentBoardIndex);
    updateDisplayedBoard();
  }

  // Displays the board indicated by the updated value of currentBoardIndex
  private void updateDisplayedBoard() {
    StackPane boardToDisplay = myBoards.get(currentBoardIndex).getBoardPane();
    myCenterPane.getChildren().clear();
    myCenterPane.getChildren().add(boardToDisplay);
  }

  private void createBoards(int numBoards) {
    int[][] arrayLayout = new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    for (int i = 0; i < numBoards-1; i++) {
      GameBoardView board = new EnemyBoardView(new ShapeType(), arrayLayout, i);
      board.addObserver(this);
      myBoards.add(board);
    }
    // create the last board with a different array layout
    GameBoardView board2 = new EnemyBoardView(new ShapeType(), new int[][]{{0, 1, 0}, {1, 1, 1}, {0, 1, 0}}, 2);
    board2.addObserver(this);
    myBoards.add(board2);

    GameBoardView board3 = new EnemyBoardView(new ShapeType(), new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 1, 1}}, 3);
    board3.addObserver(this);
    myBoards.add(board3);

    GameBoardView board4 = new SelfBoardView(new ShapeType(), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}}, 4);
    board4.addObserver(this);
    myBoards.add(board4);
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

  }

  /**
   * Removes any Pieces that are at the coordinates contained in coords.
   * @param coords Coordinates that contain pieces to remove
   */
  public void removePiece(Collection<Coordinate> coords) {

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
    myBoards.get(currentBoardIndex).setColorAt(x, y, Color.PINK);
  }
}

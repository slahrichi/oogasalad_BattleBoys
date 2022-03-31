package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import oogasalad.PropertyObservable;

public class View extends PropertyObservable implements PropertyChangeListener {

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;

  private List<BoardView> myBoards;
  private BorderPane myPane;
  private Pane myCenterPane;
  private Scene myScene;

  private int currentBoardIndex;

  public View() {
    myPane = new BorderPane();
    myCenterPane = new Pane();
    BorderPane.setAlignment(myCenterPane, Pos.CENTER);
    myPane.setCenter(myCenterPane);

    myBoards = new ArrayList<>();
    createBoards(3);
    currentBoardIndex = 0;
  }

  public Scene createViewFromPlayers() {
//    Group board0 = myBoards.get(0).getBoard();
//    BorderPane.setAlignment(board0, Pos.CENTER);
//    myPane.setTop(board0);
//    Group board1 = myBoards.get(1).getBoard();
//    BorderPane.setAlignment(board1, Pos.CENTER);
//    myPane.setLeft(board1);
//    Group board2 = myBoards.get(2).getBoard();
//    BorderPane.setAlignment(board2, Pos.CENTER);
//    myPane.setCenter(board2);
//    Group board3 = myBoards.get(3).getBoard();
//    BorderPane.setAlignment(board3, Pos.CENTER);
//    myPane.setRight(board3);
//    Group board4 = myBoards.get(4).getBoard();
//    BorderPane.setAlignment(board4, Pos.CENTER);
//    myPane.setBottom(board4);
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
    Pane boardToDisplay = myBoards.get(currentBoardIndex).getBoardPane();
    myCenterPane.getChildren().clear();
    myCenterPane.getChildren().add(boardToDisplay);
  }

  private void createBoards(int numBoards) {
    int[][] arrayLayout = new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    for (int i = 0; i < numBoards-1; i++) {
      BoardView board = new BoardView(new ShapeType(), arrayLayout, i);
      board.addObserver(this);
      myBoards.add(board);
    }
    // create the last board with a different array layout
    BoardView board2 = new BoardView(new ShapeType(), new int[][]{{0, 1, 0}, {1, 1, 1}, {0, 1, 0}}, 2);
    board2.addObserver(this);
    myBoards.add(board2);

    BoardView board3 = new BoardView(new ShapeType(), new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 1, 1}}, 3);
    board3.addObserver(this);
    myBoards.add(board3);

    BoardView board4 = new BoardView(new ShapeType(), new int[][]{{1, 1, 1}, {1, 0, 1}, {1, 1, 1}}, 4);
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
}

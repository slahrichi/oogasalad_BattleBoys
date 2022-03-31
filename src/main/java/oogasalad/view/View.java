package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import oogasalad.PropertyObservable;

public class View extends PropertyObservable implements PropertyChangeListener {

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;

  private List<BoardView> myBoards;
  private BorderPane myPane;
  private Scene myScene;

  public View() {
    myPane = new BorderPane();
    myBoards = new ArrayList<>();
    createBoards(5);
  }

  public Scene createViewFromPlayers() {
    Group board0 = myBoards.get(0).getBoard();
    BorderPane.setAlignment(board0, Pos.CENTER);
    myPane.setTop(board0);
    Group board1 = myBoards.get(1).getBoard();
    BorderPane.setAlignment(board1, Pos.CENTER);
    myPane.setLeft(board1);
    Group board2 = myBoards.get(2).getBoard();
    BorderPane.setAlignment(board2, Pos.CENTER);
    myPane.setCenter(board2);
    Group board3 = myBoards.get(3).getBoard();
    BorderPane.setAlignment(board3, Pos.CENTER);
    myPane.setRight(board3);
    Group board4 = myBoards.get(4).getBoard();
    BorderPane.setAlignment(board4, Pos.CENTER);
    myPane.setBottom(board4);
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    return myScene;
  }

  private void createBoards(int numBoards) {
    int[][] arrayLayout = new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    for (int i = 0; i < numBoards-1; i++) {
      BoardView board = new BoardView(new ShapeType(), arrayLayout, i);
      board.addObserver(this);
      myBoards.add(board);
    }
    // create the last board with a different array layout
    BoardView board = new BoardView(new ShapeType(), new int[][]{{0, 1, 0}, {1, 1, 1}, {0, 1, 0}}, 4);
    board.addObserver(this);
    myBoards.add(board);
  }

  public void promptPlayTurn() {
    System.out.println("Please play turn! Maybe we should show a player ID here!");
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName(), evt.getNewValue());
  }
}

package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
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

  public Scene getScene() {
    myPane.setTop(myBoards.get(0).getBoard());
    myPane.setLeft(myBoards.get(1).getBoard());
    myPane.setCenter(myBoards.get(2).getBoard());
    myPane.setRight(myBoards.get(3).getBoard());
    myPane.setBottom(myBoards.get(4).getBoard());
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    return myScene;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName(), evt.getNewValue());
  }
}

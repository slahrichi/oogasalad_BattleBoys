package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.scene.Group;

import oogasalad.model.utilities.Coordinate;
import oogasalad.PropertyObservable;

public class BoardView extends PropertyObservable implements PropertyChangeListener {

  static final double BOARD_WIDTH = 200;
  static final double BOARD_HEIGHT = 200;

  private CellView[][] myLayout;
  private Group myBoard;
  private int myID;

  // controller passes some kind of parameter to the
  public BoardView(ShapeType shape, int[][] arrayLayout, int id) {
    setupBoard(arrayLayout, shape);
    myID = id;
  }

  private void setupBoard(int[][] arrayLayout, ShapeType shape) {
    myLayout = new CellView[arrayLayout.length][arrayLayout[0].length];
    myBoard = new Group();
    for (int col = 0; col < arrayLayout.length; col++) {
      for(int row = 0; row < arrayLayout[0].length; row++) {
        if (arrayLayout[col][row] == 1) {
          CellView cell = new CellView(shape, col, row, arrayLayout[0].length, arrayLayout.length);
          cell.addObserver(this);
          myLayout[col][row] = cell;
        }
      }
    }
  }

  public Group getBoard() {
    for (int i = 0; i < myLayout.length; i++) {
      for (int j = 0; j < myLayout[0].length; j++) {
        if (myLayout[i][j] != null) {
          myBoard.getChildren().add(myLayout[i][j].getCell());
        }
      }
    }
    return myBoard;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
//    System.out.println(evt.getPropertyName());
    notifyObserver("boardClicked", new ShotInfo(((Coordinate) evt.getNewValue()).getRow(), ((Coordinate) evt.getNewValue()).getColumn(), myID));
  }
}
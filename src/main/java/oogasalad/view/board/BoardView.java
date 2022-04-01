package oogasalad.view.board;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import oogasalad.model.utilities.Coordinate;
import oogasalad.PropertyObservable;
import oogasalad.view.CellView;
import oogasalad.view.ShotInfo;

public abstract class BoardView extends PropertyObservable implements PropertyChangeListener {

  static final double BOARD_WIDTH = 200;
  static final double BOARD_HEIGHT = 200;

  protected CellView[][] myLayout;
  private StackPane myBoard;
  private Group myBase;
  private int myID;
  
  protected static int INVALID = 0;
  protected static int EMPTY = 1;
  protected static int SHIP = 2;
  protected static int SPECIAL = 3;
  protected Map<Integer, Paint> mapCellToColor;

  // controller passes some kind of parameter to the
  public BoardView(ShapeType shape, int[][] arrayLayout, int id) {
    mapCellToColor = new HashMap<>();
    mapCellToColor.put(INVALID, Color.WHITE);
    mapCellToColor.put(EMPTY, Color.BLUE);
    mapCellToColor.put(SHIP, Color.BLACK);
    mapCellToColor.put(SPECIAL, Color.YELLOW);
    setupBoard(arrayLayout, shape);
    myID = id;
  }

  private void setupBoard(int[][] arrayLayout, ShapeType shape) {
    myLayout = new CellView[arrayLayout.length][arrayLayout[0].length];
    myBoard = new StackPane();
    myBase = new Group();
    initializeCellViews(arrayLayout, shape);
    initializeBoardNodes();
  }

  public abstract void initializeCellViews(int[][] arrayLayout, ShapeType shape);

  /**
   * Changes the color of a cell on the BoardView.
   * @param row x coordinate of cell
   * @param col y coordinate of cell
   * @param color Paint to change the cell's fill to
   */
  public void setColorAt(int row, int col, Paint color) {
    if(!(row < myLayout.length && col < myLayout[0].length)) {
      throw new IllegalArgumentException("Row " + row + " and column " + col + " out of bounds");
    }

    myLayout[row][col].getCell().setFill(color);
  }

  private void initializeBoardNodes() {
    for (int i = 0; i < myLayout.length; i++) {
      for (int j = 0; j < myLayout[0].length; j++) {
        if (myLayout[i][j] != null) {
          myBase.getChildren().add(myLayout[i][j].getCell());
        }
      }
    }
    myBoard.getChildren().add(myBase);
  }

  public StackPane getBoardPane() {
    return myBoard;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
//    System.out.println(evt.getPropertyName());
    notifyObserver("boardClicked", new ShotInfo(((Coordinate) evt.getNewValue()).getColumn(), ((Coordinate) evt.getNewValue()).getRow(), myID));
  }
}

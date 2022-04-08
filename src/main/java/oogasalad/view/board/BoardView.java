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
import oogasalad.view.Info;

public abstract class BoardView extends PropertyObservable implements PropertyChangeListener {

  protected CellView[][] myLayout;
  private StackPane myBoard;
  private Group myBase;
  private int myID;
  protected BoardMaker myBoardMaker;

  // piece type enum
  protected static int INVALID = 0;
  protected static int EMPTY = 1;
  protected static int HEALTHY_SHIP = 2;
  protected static int DAMAGED_SHIP = 3;
  protected static int DESTROYED_SHIP = 4;
  protected static int SPECIAL = 5;
  protected Map<Integer, Paint> mapCellToColor;

  // shot result enum
  protected static int MISSED = 0;
  protected static int HIT_PIECE = 1;
  protected static int HIT_SPECIAL = 2;
  protected Map<Integer, Paint> mapShotToColor;

  // controller passes some kind of parameter to the
  public BoardView(double size, int[][] arrayLayout, int id) {
    myBoardMaker = new BoardMaker(size, arrayLayout.length, arrayLayout[0].length);

    mapCellToColor = new HashMap<>();
    mapCellToColor.put(INVALID, Color.WHITE);
    mapCellToColor.put(EMPTY, Color.BLUE);
    mapCellToColor.put(HEALTHY_SHIP, Color.BLACK);
    mapCellToColor.put(DAMAGED_SHIP, Color.ORANGE);
    mapCellToColor.put(DESTROYED_SHIP, Color.RED);
    mapCellToColor.put(SPECIAL, Color.YELLOW);

    mapShotToColor = new HashMap<>();
    mapShotToColor.put(MISSED, Color.WHITE);
    mapShotToColor.put(HIT_PIECE, Color.RED);
    mapShotToColor.put(HIT_SPECIAL, Color.YELLOW);
    myID = id;
    setupBoard(arrayLayout);
  }

  private void setupBoard(int[][] arrayLayout) {
    myLayout = new CellView[arrayLayout.length][arrayLayout[0].length];
    myBoard = new StackPane();
    myBoard.setId("board-view");
    myBase = new Group();
    myBase.setId("board-view-base");
    initializeCellViews(arrayLayout);
    initializeBoardNodes();
  }

  public abstract void initializeCellViews(int[][] arrayLayout);

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

  public void clear() {
    for (int i = 0; i < myLayout.length; i++) {
      for (int j = 0; j < myLayout[0].length; j++) {
        myLayout[i][j].getCell().setFill(mapCellToColor.get(EMPTY));
      }
    }
  }

  private void initializeBoardNodes() {
    for (int i = 0; i < myLayout.length; i++) {
      for (int j = 0; j < myLayout[0].length; j++) {
        if (myLayout[i][j] != null) {
          myLayout[i][j].getCell().setId("cell-view-"+i+"-"+j+"-"+myID);
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
    notifyObserver("boardClicked", new Info(((Coordinate) evt.getNewValue()).getRow(), ((Coordinate) evt.getNewValue()).getColumn(), myID));
  }
}

package oogasalad.view.board;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ResourceBundle;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import oogasalad.model.utilities.Coordinate;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;
import oogasalad.view.Info;

public abstract class BoardView extends PropertyObservable implements PropertyChangeListener {

  protected CellView[][] myLayout;
  private StackPane myBoard;
  private Group myBase;
  private int myID;
  protected BoardMaker myBoardMaker;
  protected ResourceBundle myCellStateResources;
  protected ResourceBundle myMarkerResources;
  protected static String FILL_PREFIX = "FillColor_";

  // controller passes some kind of parameter to the
  public BoardView(double size, CellState[][] arrayLayout, int id) {
    myBoardMaker = new BoardMaker(size);
    myCellStateResources = ResourceBundle.getBundle("/CellState");
    myMarkerResources = ResourceBundle.getBundle("/Markers");

    myID = id;
    setupBoard(arrayLayout);
  }

  private void setupBoard(CellState[][] arrayLayout) {
    myLayout = new CellView[arrayLayout.length][arrayLayout[0].length];
    myBoard = new StackPane();
    myBoard.setId("board-view");
    myBase = new Group();
    myBase.setId("board-view-base");
    initializeCellViews(arrayLayout);
    initializeBoardNodes();
  }

  public abstract void initializeCellViews(CellState[][] arrayLayout);

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
        myLayout[i][j].getCell().setFill(Color.valueOf(myCellStateResources.getString(FILL_PREFIX+CellState.WATER.name())));
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

  public int getID() {
    return myID;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
//    System.out.println(evt.getPropertyName());
    notifyObserver("boardClicked", new Info(((Coordinate) evt.getNewValue()).getRow(), ((Coordinate) evt.getNewValue()).getColumn(), myID));
  }
}

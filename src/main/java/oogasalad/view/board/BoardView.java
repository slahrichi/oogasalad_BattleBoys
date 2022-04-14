package oogasalad.view.board;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import oogasalad.model.utilities.Coordinate;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;
import oogasalad.view.Info;

public abstract class BoardView extends PropertyObservable implements PropertyChangeListener {
  private static final String BOARD_ID = "board-view";
  private static final String BASE_ID = "board-view-base";
  private static final String ERROR_MESSAGE = "Row %i and column %i out of bounds";
  private static final String BOARD_CLICKED_METHOD_NAME = "boardClicked";
  private static final String CELL_ID = "cell-view-%d-%d-%d";

  protected CellView[][] myLayout;
  private StackPane myBoard;
  private Group myBase;
  private int myID;
  protected BoardMaker myBoardMaker;
  protected static String FILL_PREFIX = "FillColor_";

  // controller passes some kind of parameter to the
  public BoardView(double size, CellState[][] arrayLayout, int id) {
    myBoardMaker = new BoardMaker(size);
    myID = id;
    setupBoard(arrayLayout);
  }

  private void setupBoard(CellState[][] arrayLayout) {
    myLayout = new CellView[arrayLayout.length][arrayLayout[0].length];
    myBoard = new StackPane();
    myBoard.setId(BOARD_ID);
    myBase = new Group();
    myBase.setId(BASE_ID);
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
      throw new IllegalArgumentException(String.format(ERROR_MESSAGE, row, col));
    }
    myLayout[row][col].getCell().setFill(color);
  }

  private void initializeBoardNodes() {
    for (int i = 0; i < myLayout.length; i++) {
      for (int j = 0; j < myLayout[0].length; j++) {
        if (myLayout[i][j] != null) {
          myLayout[i][j].getCell().setId(String.format(CELL_ID, i, j, myID));
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
    notifyObserver(BOARD_CLICKED_METHOD_NAME, new Info(((Coordinate) evt.getNewValue()).getRow(), ((Coordinate) evt.getNewValue()).getColumn(), myID));
  }
}

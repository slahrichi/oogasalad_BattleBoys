package oogasalad.view.board;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;

public abstract class BoardView extends PropertyObservable implements PropertyChangeListener {
  private static final String BOARD_ID = "board-view";
  private static final String BASE_ID = "board-view-base";
  private static final String ERROR_MESSAGE = "Row %d and column %d out of bounds";
  private static final String CELL_ID = "cell-view-%d-%d-%d";

  protected CellView[][] myLayout;
  private Pane myBoard;
  private Group myBase;
  protected int myID;
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

  public Paint getColorAt(int row, int col) {
    if(!(row < myLayout.length && col < myLayout[0].length)) {
      throw new IllegalArgumentException(String.format(ERROR_MESSAGE, row, col));
    }
    return myLayout[row][col].getCell().getFill();
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

  public void displayExplosionOnCell(int row, int col, ImageView explosionImage) {
    getBoardPane().getChildren().add(explosionImage);
    double cellX = myLayout[row][col].getCell().getBoundsInParent().getMinX();
    double cellY = myLayout[row][col].getCell().getBoundsInParent().getMinY();
    double width = myLayout[row][col].getCell().getBoundsInParent().getWidth();
    double height = myLayout[row][col].getCell().getBoundsInParent().getHeight();

    explosionImage.setFitWidth(width * 2);
    explosionImage.setFitHeight(height * 2);
    explosionImage.setTranslateX(width / 2.0 - (getWidth() / 2 - cellX));
    explosionImage.setTranslateY(height / 2.0 - (getHeight() / 2 - cellY));
  }

  protected double getWidth() {
    return myLayout[0][myLayout[0].length - 1].getCell().getBoundsInParent().getMaxX() -
        myLayout[0][0].getCell().getBoundsInParent().getMinX();
  }

  protected double getHeight() {
    return myLayout[myLayout.length - 1][0].getCell().getBoundsInParent().getMaxY() -
        myLayout[0][0].getCell().getBoundsInParent().getMinY();
  }


  public void removeExplosionImage(ImageView explosionImage) {
    getBoardPane().getChildren().remove(explosionImage);
  }

  public Pane getBoardPane() {
    return myBoard;
  }

  public int getID() {
    return myID;
  }

  @Override
  public abstract void propertyChange(PropertyChangeEvent evt);
}

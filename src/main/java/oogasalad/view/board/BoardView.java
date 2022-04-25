package oogasalad.view.board;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;

/**
 * Creates the board superclass that defines what a board in the view looks like and how it can
 * function
 *
 * @author Minjun Kwak
 */
public abstract class BoardView extends PropertyObservable implements PropertyChangeListener {

  private static final String BOARD_ID = "board-view";
  private static final String BASE_ID = "board-view-base";
  private static final String ERROR_MESSAGE = "Row %d and column %d out of bounds";
  private static final String CELL_ID = "cell-view-%d-%d-%d";

  protected CellView[][] myLayout;
  private Pane myBoard;
  private Group myBase;
  protected int myID;
  protected Map<CellState, Color> myColorMap;

  /**
   * Creates a BoardView with an ID
   *
   * @param size        the size of each cell
   * @param arrayLayout the layout of the state of each cell in the board
   * @param id          the id of this board
   */
  public BoardView(double size, CellState[][] arrayLayout, Map<CellState, Color> colorMap, int id) {
    myID = id;
    myColorMap = colorMap;
    setupBoard(arrayLayout, size);
  }

  // Sets up the board by creating a StackPane for the board, a CellView array for the board, and a Group
  // to act as the base for this board view
  private void setupBoard(CellState[][] arrayLayout, double size) {
    myLayout = new CellView[arrayLayout.length][arrayLayout[0].length];
    myBoard = new StackPane();
    myBoard.setId(BOARD_ID);
    myBase = new Group();
    myBase.setId(BASE_ID);
    initializeCellViews(arrayLayout, size);
    initializeBoardNodes();
  }

  /**
   * @param arrayLayout
   * @param size
   */
  public abstract void initializeCellViews(CellState[][] arrayLayout, double size);

  /**
   * Changes the color of a cell on the BoardView.
   *
   * @param row   the row number of this cell
   * @param col   the column number of this cell
   * @param color Paint to change the cell's fill to
   */
  public void setColorAt(int row, int col, Paint color) {
    if (!(row < myLayout.length && col < myLayout[0].length)) {
      throw new IllegalArgumentException(String.format(ERROR_MESSAGE, row, col));
    }
    myLayout[row][col].getCell().setFill(color);
  }

  /**
   * Gets the color of a cell on the BoardView.
   *
   * @param row the row number of this cell
   * @param col the column number of this cell
   * @return the color of this cell
   */
  public Paint getColorAt(int row, int col) {
    if (!(row < myLayout.length && col < myLayout[0].length)) {
      throw new IllegalArgumentException(String.format(ERROR_MESSAGE, row, col));
    }
    return myLayout[row][col].getCell().getFill();
  }

  // adds polygons representing the cells to the board's base Group, and adds the base to the board's StackPane
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

  /**
   * Shows an explosion animation on a cell
   *
   * @param row            the row number of the cell
   * @param col            the column number of the cell
   * @param explosionImage the image to be shown in the animation
   */
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

  // gets the width of a cell
  private double getWidth() {
    return myLayout[0][myLayout[0].length - 1].getCell().getBoundsInParent().getMaxX() -
        myLayout[0][0].getCell().getBoundsInParent().getMinX();
  }

  // gets the height of a cell
  private double getHeight() {
    return myLayout[myLayout.length - 1][0].getCell().getBoundsInParent().getMaxY() -
        myLayout[0][0].getCell().getBoundsInParent().getMinY();
  }

  /**
   * Removes the explosion image from the board
   *
   * @param explosionImage the image to be removed from the board
   */
  public void removeExplosionImage(ImageView explosionImage) {
    getBoardPane().getChildren().remove(explosionImage);
  }

  /**
   * Getter for the StackPane that has the board view in it
   *
   * @return the StackPane myBoard
   */
  public Pane getBoardPane() {
    return myBoard;
  }

  /**
   * Getter for the ID of this BoardView
   *
   * @return myID
   */
  public int getID() {
    return myID;
  }

  /**
   * The listener method of this BoardView that is called when the class it is observing notifies
   * this class
   *
   * @param evt the evt associated with the notification
   */
  @Override
  public abstract void propertyChange(PropertyChangeEvent evt);
}

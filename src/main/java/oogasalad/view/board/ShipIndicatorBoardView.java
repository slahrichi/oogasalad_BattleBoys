package oogasalad.view.board;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;
import oogasalad.view.Info;

/**
 * This class creates a dummy board that is meant to be displayed on the right pane of the screen,
 * which shows the next ship to be placed when used in setup, or shows the ships remaining for each
 * player. Because it is a dummy board that is not meant to be interacted with, observers are not
 * added to the cells of this board.
 *
 * @author Minjun Kwak
 */
public class ShipIndicatorBoardView extends BoardView {

  /**
   * Creates the necessary components of a BoardView by calling its superclass constructor
   *
   * @param size        the size of each cell
   * @param arrayLayout the layout of the cell states of the board
   * @param id          the id of this BoardView
   */
  public ShipIndicatorBoardView(double size, CellState[][] arrayLayout, Map<CellState, Color> colorMap, int id) {
    super(size, arrayLayout, colorMap, id);
  }

  /**
   * Initializes the cells of the BoardView by creating new CellView instances and adding them to
   * the CellView array associated with this BoardView
   *
   * @param arrayLayout the layout of the cells of this BoardView
   * @param size        the size of each cell
   */
  @Override
  public void initializeCellViews(CellState[][] arrayLayout, double size) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        List<Double> points = BoardMaker.calculatePoints(row, col, size);
        CellView cell = new CellView(points, myColorMap.get(arrayLayout[row][col]),
            row, col);
        myLayout[row][col] = cell;
      }
    }
  }

  /**
   * The listener method of this BoardView that is called when the class it is observing notifies
   * this class
   *
   * @param evt the evt associated with the notification
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName(), new Info(((Coordinate) evt.getNewValue()).getRow(),
        ((Coordinate) evt.getNewValue()).getColumn(), myID));
  }
}

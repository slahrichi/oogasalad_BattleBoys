package oogasalad.view.board;

import java.beans.PropertyChangeEvent;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

/**
 * Creates a BoardView subclass representing an enemy player's board. This BoardView is displayed as
 * one of the BoardViews during the main game.
 *
 * @author Minjun Kwak, Eric Xie, Edison Ooi
 */
public class SelfBoardView extends GameBoardView {

  private static final String SELF = "Self";

  /**
   * Creates the necessary components of a BoardView by calling its superclass constructor
   *
   * @param size        the size of each cell
   * @param arrayLayout the layout of the cell states of the board
   * @param id          the id of this BoardView
   */
  public SelfBoardView(double size, CellState[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  /**
   * The listener method of this BoardView that is called when the class it is observing notifies
   * this class
   *
   * @param evt the evt associated with the notification
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName()+SELF, ((Coordinate) evt.getNewValue()).getRow() + " " + ((Coordinate) evt.getNewValue()).getColumn() + " " + myID);
  }
}

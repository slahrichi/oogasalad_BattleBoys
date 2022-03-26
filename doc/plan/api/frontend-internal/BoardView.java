/**
 * BoardView represents classes which contain a collection of cell views that can be occupied by Pieces
 * or be clicked on.
 *
 * @author Eric Xie, Edison Ooi, Minjun Kwak
 */
public interface BoardView {

  /**
   * Changes the color of a cell on the BoardView.
   * @param x x coordinate of cell
   * @param y y coordinate of cell
   * @param color Paint to change the cell's fill to
   */
  public void changeColorAt(int x, int y, Paint color);

  /**
   * Adds an event listener, such as a click listener, to a cell view so that the controller knows
   * when the user has clicked on some cell on the board.
   * @param x x coordinate of cell
   * @param y y coordinate of cell
   * @param listener event listener to add on cell view
   */
  public void setEventListenerOnCell(int x, int y, Listener listener);
}
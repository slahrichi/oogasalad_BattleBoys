package oogasalad.view;

import oogasalad.model.utilities.tiles.enums.CellState;


/**
 * This interface represents classes that show the user the result of their attempted shot at an
 * opponent, whether the shot was a hit, miss, sink, etc.
 *
 * @author Edison Ooi
 */

public interface ShotVisualizer {

  /**
   * Displays a certain shot outcome on a certain cell on the user's outgoing shots board. For
   * example, if the user clicked cell (1,0) and an enemy ship was at that location, cell (1,0) would
   * turn red.
   * @param x x coordinate of cell
   * @param y y coordinate of cell
   * @param result indicates if the shot was a hit or a miss
   */
  void displayShotAt(int x, int y, CellState result);

}

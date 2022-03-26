public interface ShotVisualizer {

  /**
   * Displays a certain shot outcome on a certain cell on the user's outgoing shots board. For
   * example, if the user clicked cell (1,0) and an enemy ship was at that location, cell (1,0) would
   * turn red.
   * @param x x coordinate of cell
   * @param y y coordinate of cell
   * @param wasHit indicates if the shot was a hit or a miss
   */
  public void displayShotAt(int x, int y, boolean wasHit);

}
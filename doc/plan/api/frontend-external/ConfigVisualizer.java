public interface ConfigVisualizer {

  /**
   * Updates the user's side-view to show which of the opponent's ships are still alive.
   *
   * @param pieces Piece objects owned by the opponent that are still alive.
   */
  void updateShipsLeft(Collection<Piece> pieces);

  /**
   * Opens the user shop window, allowing a user to buy shot upgrades for that turn using their
   * gold.
   */
  void openShop();

}


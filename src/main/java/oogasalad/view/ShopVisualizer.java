package oogasalad.view;

/**
 * This interface represents classes that can open and close the shop.
 *
 * @author Eric Xie, Edison Ooi, Minjun Kwak
 */
public interface ShopVisualizer {
  /**
   * Opens the user shop window, allowing a user to buy shot upgrades for that turn using their
   * gold.
   */
  public void openShop();

  /**
   * Closes the user shop window. This should happen when the user buys a shot upgrade.
   */
  public void closeShop();
}

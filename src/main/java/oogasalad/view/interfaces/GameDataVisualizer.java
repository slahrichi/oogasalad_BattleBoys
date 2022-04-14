package oogasalad.view.interfaces;

import java.util.Collection;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;

/**
 * GameDataVisualizer represents classes that display metadata about the current game being played,
 * such as which enemy ships are left, how many shots they have left, or who's turn it is.
 *
 * @author Edison Ooi
 */
public interface GameDataVisualizer {

  /**
   * Updates the user's side-view to show which of the opponent's ships are still alive.
   *
   * @param pieces Piece objects owned by the opponent that are still alive.
   */
  public void updatePiecesLeft(Collection<Collection<Coordinate>> pieces);

  /**
   * Updates the text that shows the user how many shots they have left in their turn.
   *
   * @param shotsRemaining number of shots the user has left in their turn
   */
  public void setNumShotsRemaining(int shotsRemaining);

  /**
   * Updates the text that shows the user how much gold they currently have.
   *
   * @param amountOfGold gold that user has
   */
  public void setGold(int amountOfGold);

  /**
   * Updates the text that shows the user whose turn it currently is.
   *
   * @param playerName name or ID of player whose turn it is
   */
  public void setPlayerTurnIndicator(String playerName);

}

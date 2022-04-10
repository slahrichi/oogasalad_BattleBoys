package oogasalad.model.utilities.WinConditions;

import java.util.Collection;
import java.util.function.Function;
import oogasalad.model.players.Player;
import oogasalad.model.players.PlayerRecord;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.WinConditions.WinConditionInterface;

public abstract class WinCondition implements WinConditionInterface {

  /**
   * updateWinner method sends the WinCondition's lambda to the provided collection of players in order
   * to update their win state
   * @param player Player to apply the WinCondition's win lambda to
   */
  public void updateWinner(Player player) {
      player.applyWinCondition(getWinLambda());
  }

  /**
   * This method is used to get the functional lambda that defines how the WinCondition determines if a Player wins or not
   * @return Function lambda that takes in the old PlayerRecord as a parameter and returns an updated PlayerRecord
   */
  public abstract Function<PlayerRecord, WinState> getWinLambda();
}

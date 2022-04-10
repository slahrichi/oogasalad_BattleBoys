package oogasalad.model.utilities.WinConditions;

import java.util.Collection;
import java.util.function.Function;
import oogasalad.model.players.Player;
import oogasalad.model.players.PlayerRecord;
import oogasalad.model.utilities.Board;

/**
 * WinCondition API analyzes the board state for every player in order to check/determine if a player has
 * won or lost the current game. Determining win states in WinConditions will be defined by a lambda function.
 * This class is intended to be an internal API to the Controller to help determine winners using controller
 * parameters
 */
public interface WinConditionInterface {

  /**
   * updateWinner method sends the WinCondition's lambda to the provided collection of players in order
   * to update their win state
   * @param player Player to apply the WinCondition's win lambda to
   */
  public WinState updateWinner(Player player);

  /**
   * This method is used to get the functional lambda that defines how the WinCondition determines if a Player wins or not
   * @return Function lambda that takes in the old PlayerRecord as a parameter and returns an updated PlayerRecord
   */
  public abstract Function<PlayerRecord, WinState> getWinLambda();
}

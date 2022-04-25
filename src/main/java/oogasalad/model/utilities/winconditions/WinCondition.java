package oogasalad.model.utilities.winconditions;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import oogasalad.model.players.Player;
import oogasalad.model.players.PlayerRecord;
import oogasalad.model.utilities.tiles.enums.CellState;

/**
 * WinCondition abstract class that sets up the structure for all subclass winconditions. This defines common components
 * such as default and nondesirable cell states or a common update winner function.
 *
 * Assumptions:
 * - WinCondition can be determined based off the data in player records
 * - Desirable/Nondesirable cell states can be defined by existing CellState Enum
 *
 * Dependencies:
 * - Player: applies win condition onto players and their created player record to check if the player has won/loss
 * - CellState: Uses cell state enum to define desirable and nondesirable cell states
 *
 * @author: Brandon Bae
 */
public abstract class WinCondition implements WinConditionInterface {
  public static final Set<CellState> DEFAULT_DESIRABLE_CELL_STATES = Collections.unmodifiableSet(new HashSet<>(
      Arrays.asList(CellState.SHIP_HEALTHY, CellState.SHIP_DAMAGED, CellState.SHIP_SUNKEN)));
  public static final Set<CellState> DEFAULT_NONDESIRABLE_CELL_STATES = Collections.unmodifiableSet(new HashSet<>(
      Arrays.asList(CellState.SHIP_SUNKEN, CellState.ISLAND_SUNK, CellState.WATER, CellState.WATER_HIT)));

  /**
   * updateWinner method sends the WinCondition's lambda to the provided collection of players in order
   * to update their win state
   * @param player Player to apply the WinCondition's lambda to determine if player has won or not
   * @return WinState enum value representing if the player has won, loss, or neither based off the lambda
   */
  public WinState updateWinner(Player player) {
      return player.applyWinCondition(getWinLambda());
  }

  /**
   * This method is used to get the functional lambda that defines how the WinCondition determines if a Player wins or not
   * @return Function lambda that takes in a PlayerRecord (defines a player's instance variables) as a parameter and
   *         returns a Win state representing if the player has won, loss, or neither based off the lambda
   */
  public abstract Function<PlayerRecord, WinState> getWinLambda();


  /**
   * This method returns a set of desirable cell states associated with this Win/Loss Condition. This is meant to be
   * used by the AI players to determine what types of hits are actively helping the AI move closer to winning. This
   * implementation of getDesirableCellStates() returns a default set of cell states that is meant for basic battle ship
   * @return Set of cell states that represents cell states that help fulfill the win condition
   */
  public Set<CellState> getDesirableCellStates() {
    return DEFAULT_DESIRABLE_CELL_STATES;
  };

  /**
   * This method returns a set of nondesirable cell states associated with this Win/Loss Condition. This is meant to be
   * used by the AI players to determine what types of hits are detrimental to the AI such as fulfilling a loss condition
   * or not helping a win condition. This implementation of getNonDesirableCellStates() returns a default set of cell
   * states that is meant for basic battle ship
   * @return Set of cell states that represents cell states that be detrimental for an AI to continue pursuing
   */
  public Set<CellState> getNonDesirableCellStates() {
    return DEFAULT_NONDESIRABLE_CELL_STATES;
  }
}

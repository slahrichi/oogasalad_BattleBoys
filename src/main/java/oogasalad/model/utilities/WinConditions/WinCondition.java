package oogasalad.model.utilities.WinConditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import oogasalad.model.players.Player;
import oogasalad.model.players.PlayerRecord;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.WinConditions.WinConditionInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

public abstract class WinCondition implements WinConditionInterface {
  public static final Set<CellState> DEFAULT_DESIRABLE_CELL_STATES = Collections.unmodifiableSet(new HashSet<CellState>(
      Arrays.asList(CellState.SHIP_HEALTHY, CellState.SHIP_DAMAGED)));
  public static final Set<CellState> DEFAULT_NONDESIRABLE_CELL_STATES = Collections.unmodifiableSet(new HashSet<CellState>(
      Arrays.asList(CellState.SHIP_SUNKEN, CellState.ISLAND_SUNK, CellState.WATER, CellState.WATER_HIT)));

  /**
   * updateWinner method sends the WinCondition's lambda to the provided collection of players in order
   * to update their win state
   * @param player Player to apply the WinCondition's win lambda to
   */
  public WinState updateWinner(Player player) {
      return player.applyWinCondition(getWinLambda());
  }

  /**
   * This method is used to get the functional lambda that defines how the WinCondition determines if a Player wins or not
   * @return Function lambda that takes in the old PlayerRecord as a parameter and returns an updated PlayerRecord
   */
  public abstract Function<PlayerRecord, WinState> getWinLambda();

  public Set<CellState> getDesirableCellStates() {
    return DEFAULT_DESIRABLE_CELL_STATES;
  };

  public Set<CellState> getNonDesirableCellStates() {
    return DEFAULT_NONDESIRABLE_CELL_STATES;
  }
}

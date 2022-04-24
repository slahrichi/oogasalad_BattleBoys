package oogasalad.model.utilities.winconditions;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import oogasalad.model.players.PlayerRecord;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Subclass of WinCondition that defines a win or loss condition that triggers when the player hits X amount of the
 * specified cellstate
 *
 * Assumptions:
 * - provided X value is less than or equal to the number of cells of type provided cellState
 * - CellState is a valid cell state
 * - Provided CellState is a X_SUNK type cell state as hits usually return a X_SUNK type cell state. This means that if we
 *   define a cell state that is not sunk it will most likely not trigger.
 *
 * Dependencies:
 * - Player: Needs to check the Player's hitsMap to check the number of each CellState that they have hit
 *
 * @author Brandon Bae
 */
public class HitXCellsCondition extends WinCondition{

  private static final Logger LOG = LogManager.getLogger(HitXCellsCondition.class);
  private CellState myCellToHit;
  private int myNumToHit;
  private WinState myConditionType;

  /**
   * Constructor for HitXCellsCondition
   * @param cellToHit type of CellState to hit in order to count
   * @param numToHit number of cells to hit in order to trigger the condition
   * @param conditionType Whether player wins or loses when condition triggers
   */
  public HitXCellsCondition(CellState cellToHit, int numToHit, WinState conditionType) {
      myCellToHit = cellToHit;
      myNumToHit = numToHit;
      myConditionType = conditionType;
  }

  /**
   * WinLambda that makes a player win/lose (based off conditionType in constructor) when they hit x amount of the defined
   * cellstate
   * @return Function<PlayerRecord,WinState> that defines the win/loss condition to send to the player
   */
  @Override
  public Function<PlayerRecord, WinState> getWinLambda() {
    return (PlayerRecord playerInfo) -> {
      Map<CellState, Integer> playerHitsMap = playerInfo.hitsMap();
      int playerNumHit;
      if(playerHitsMap.containsKey(myCellToHit)) {
        playerNumHit = playerHitsMap.get(myCellToHit);
      }
      else {
        playerNumHit = 0;
      }
      LOG.info(String.format("Player has %d out of %d %s to %s", playerNumHit, myNumToHit, myCellToHit, myConditionType));
      if(playerNumHit >= myNumToHit){
        return myConditionType;
      }
      return WinState.NEUTRAL;
    };
  }

  /**
   * Overridden getDesirableCellStates method that makes sure to add the defined CellState to the DefaultDesirableStates
   * if it is a Win type condition as it is appropriate for the AI to search for. Otherwise it removes it from the DefaultDesirableStates
   * as it is a cellstate that leads to a loss type condition
   * @return  updated CellStates set that defines desirable cell states for AI to seek out
   */
  @Override
  public Set<CellState> getDesirableCellStates() {
    Set<CellState> updatedDesirableCellStates = new HashSet<CellState>(DEFAULT_DESIRABLE_CELL_STATES);
    if(myConditionType == WinState.WIN) {
      updatedDesirableCellStates.add(myCellToHit);
    }
    else {
      updatedDesirableCellStates.remove(myCellToHit);
    }
    return updatedDesirableCellStates;
  }

  /**
   * Overridden getNonDesirableCellStates method that makes sure to add the defined CellState to the DefaultNonDesirableStates
   * if it is a Loss type condition as it is appropriate for the AI to avoid. Otherwise it removes it from the DefaultDesirableStates
   * as it is a cellstate that leads to a win type condition
   * @return  updated CellStates set that defines nondesirable cell states for AI to seek out
   */
  @Override
  public Set<CellState> getNonDesirableCellStates() {
    Set<CellState> updatedNonDesirableCellStates = new HashSet<CellState>(DEFAULT_NONDESIRABLE_CELL_STATES);
    if(myConditionType == WinState.WIN) {
      updatedNonDesirableCellStates.remove(myCellToHit);
    }
    else {
      updatedNonDesirableCellStates.add(myCellToHit);
    }
    return updatedNonDesirableCellStates;
  }
}

package oogasalad.model.utilities.WinConditions;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import oogasalad.model.players.PlayerRecord;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HitXCellsCondition extends WinCondition{

  private static final Logger LOG = LogManager.getLogger(HitXCellsCondition.class);
  private CellState myCellToHit;
  private int myNumToHit;
  private WinState myConditionType;

  public HitXCellsCondition(CellState cellToHit, int numToHit, WinState conditionType) {
      myCellToHit = cellToHit;
      myNumToHit = numToHit;
      myConditionType = conditionType;
  }

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

package oogasalad.model.utilities.WinConditions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import oogasalad.model.players.PlayerRecord;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HaveXGoldWinCondition extends WinCondition{
  public static final List<CellState> DESIRABLE_CELL_STATES = Collections.unmodifiableList(new ArrayList<CellState>(
      Arrays.asList(CellState.SHIP_HEALTHY, CellState.SHIP_DAMAGED)));

  private static final Logger LOG = LogManager.getLogger(HaveXGoldWinCondition.class);

  private int myGoldToHave;


  public HaveXGoldWinCondition(int goldToHave) {
    myGoldToHave = goldToHave;
  }

  @Override
  public Function<PlayerRecord, WinState> getWinLambda() {
    return (PlayerRecord playerInfo) -> {
      int boardState = 0;
      int playerGold = playerInfo.myCurrency();
      LOG.info(String.format("Player has %d Gold", playerInfo.myCurrency()));
      if(playerGold >= myGoldToHave){
        return WinState.WIN;
      }
      return WinState.NEUTRAL;
    };
  }
}

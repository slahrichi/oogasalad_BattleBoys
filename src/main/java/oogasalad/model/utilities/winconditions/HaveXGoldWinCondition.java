package oogasalad.model.utilities.winconditions;

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

/**
 * Subclass of WinCondition that defines a WinCondition where players win when they reach x amount of gold
 *
 * Assumptions:
 * - provided X value is a possible value to reach with the given gold values of pieces on everyone's board
 *
 * Dependencies:
 * - Player: needs to check the player's amount of gold
 *
 * @author Brandon Bae
 */
public class HaveXGoldWinCondition extends WinCondition{
  public static final List<CellState> DESIRABLE_CELL_STATES = Collections.unmodifiableList(new ArrayList<>(
      Arrays.asList(CellState.SHIP_HEALTHY, CellState.SHIP_DAMAGED)));

  private static final Logger LOG = LogManager.getLogger(HaveXGoldWinCondition.class);

  private int myGoldToHave;

  /**
   * Constructor for HaveXGoldWinCondition
   * @param goldToHave amount of  gold for a player to have to win the game
   */
  public HaveXGoldWinCondition(int goldToHave) {
    myGoldToHave = goldToHave;
  }

  /**
   * WinLambda that makes a player win when they hit x amount of the defined gold
   * @return Function<PlayerRecord,WinState> that defines the win/loss condition to send to the player
   */
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

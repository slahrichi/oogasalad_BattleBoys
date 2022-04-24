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
 * Subclass of WinCondition that defines a loss condition where if a player's board has lost x amount of ships the player loses
 *
 * Assumptions:
 * - provided X value is less than or equal to number of ships on player boards
 *
 * Dependencies:
 * - Board: Needs to check the player's board to see how many ships have been lost
 *
 * @author Brandon Bae
 */
public class LoseXShipsLossCondition extends WinCondition {
  public static final List<CellState> DESIRABLE_CELL_STATES = Collections.unmodifiableList(new ArrayList<>(
      Arrays.asList(CellState.SHIP_HEALTHY, CellState.SHIP_DAMAGED)));

  private static final Logger LOG = LogManager.getLogger(HaveXGoldWinCondition.class);

  private int myNumToSink;

  /**
   * Constructor for LoseXShipsLossCondition.
   * @param numToSink Defines the number of sunk ships allowed before a player loses
   */
  public LoseXShipsLossCondition(int numToSink) {
    myNumToSink = numToSink;
    //myTypeToSink = id;
  }

  /**
   * WinLambda that makes a player lose if their board has >= myNumToSink ships sunk.
   * @return Function<PlayerRecord,WinState> that defines the loss condition to send to the player
   */
  @Override
  public Function<PlayerRecord, WinState> getWinLambda() {
    return (PlayerRecord playerInfo) -> {
      int boardState = 0;
      Board playerBoard = playerInfo.myBoard();
      LOG.info("NumPieces Sunk = " + playerBoard.getNumPiecesSunk());
      if(playerBoard.getNumPiecesSunk()>=myNumToSink){
        return WinState.LOSE;
      }
      return WinState.NEUTRAL;
    };
  }
}

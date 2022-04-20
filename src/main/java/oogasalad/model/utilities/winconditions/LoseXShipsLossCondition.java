package oogasalad.model.utilities.winconditions;

import java.util.function.Function;
import oogasalad.model.players.PlayerRecord;
import oogasalad.model.utilities.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoseXShipsLossCondition extends WinCondition {
  //String myTypeToSink;
  private static final Logger LOG = LogManager.getLogger(HaveXGoldWinCondition.class);

  private int myNumToSink;

  public LoseXShipsLossCondition(int numToSink) {
    myNumToSink = numToSink;
    //myTypeToSink = id;
  }


  @Override
  public Function<PlayerRecord, WinState> getWinLambda() {
    return (PlayerRecord playerInfo) -> {
      int boardState = 0;
      Board playerBoard = playerInfo.myBoard();
      LOG.info("NumPieces Sunk = " + playerBoard.getNumPiecesSunk());
      if(playerBoard.getNumPiecesSunk()>=myNumToSink){
        return WinState.LOSE; //change this to whatever enum value represents "Win"
      }
      return WinState.NEUTRAL; //change this to whatever enum value represents "neutral" state (not win or loss)
    };
  }
}

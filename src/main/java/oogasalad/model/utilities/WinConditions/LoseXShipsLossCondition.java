package oogasalad.model.utilities.WinConditions;

import java.util.function.Function;
import oogasalad.model.players.PlayerRecord;
import oogasalad.model.utilities.Board;

public class LoseXShipsLossCondition extends WinCondition{
  //String myTypeToSink;
  int myNumToSink;

  public LoseXShipsLossCondition(int numToSink) {
    myNumToSink = numToSink;
    //myTypeToSink = id;
  }


  @Override
  public Function<PlayerRecord, WinState> getWinLambda() {
    return (PlayerRecord playerInfo) -> {
      int boardState = 0;
      Board playerBoard = playerInfo.myBoard();
      if(playerBoard.getNumPiecesSunk()==myNumToSink){
        return WinState.LOSE; //change this to whatever enum value represents "Win"
      }
      return WinState.NEUTRAL; //change this to whatever enum value represents "neutral" state (not win or loss)
    };
  }
}

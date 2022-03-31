package oogasalad.model.utilities.WinConditions;

import java.util.function.Function;
import oogasalad.model.players.PlayerRecord;
import oogasalad.model.utilities.Board;

public class SinkXWinCondition extends WinCondition{
  String myTypeToSink;
  int myNumToSink;

  public SinkXWinCondition(String typeToSink, int numToSink) {
    myNumToSink = numToSink;
    myTypeToSink = typeToSink;
  }


  @Override
  public Function<PlayerRecord, Integer> getWinLambda() {
    return (PlayerRecord playerInfo) -> {
      int boardState = 0;
      Board playerBoard = playerInfo.myBoard();
      if(playerBoard.getNumHit(myTypeToSink)==myNumToSink){
        return WinConditionState.WIN.ordinal(); //change this to whatever enum value represents "Win"
      }
      return WinConditionState.NEUTRAL.ordinal(); //change this to whatever enum value represents "neutral" state (not win or loss)
    };
  }
}

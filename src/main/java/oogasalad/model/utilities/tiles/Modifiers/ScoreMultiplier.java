package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;

public class ScoreMultiplier extends Modifiers{

  private int multiplier;





  @Override
  protected Consumer createConsumer() {
    Consumer ret = new BoardConsumer() {
      @Override
      public void accept(Board board) {
        board.applyMultiplier(multiplier);
      }
    };
    return ret;
  }


  @Override
  public Consumer modifierFunction(Board board){
    return myConsumer;
  }

  @Override
  public String toString() {
    return "Multiply Score by " + multiplier;
  }
}

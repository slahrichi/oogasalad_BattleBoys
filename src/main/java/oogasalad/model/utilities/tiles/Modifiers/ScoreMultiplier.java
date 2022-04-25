package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;

public class ScoreMultiplier extends Modifiers{

  private int multiplier;

  @Override
  protected Consumer createConsumer() {
    Consumer ret = new PlayerConsumer() {
      @Override
      public void accept(Player[] players) {
        players[0].setMultiplier(multiplier);
      }
    };
    return ret;
  }


  @Override
  public Consumer modifierFunction(Player[] players){
    return myConsumer;
  }

  @Override
  public String toString() {
    return "Multiply Score by " + multiplier;
  }
}

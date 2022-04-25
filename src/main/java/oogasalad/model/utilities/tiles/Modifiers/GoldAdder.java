package oogasalad.model.utilities.tiles.Modifiers;

import java.nio.channels.MulticastChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;

import oogasalad.model.players.Player;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

public class GoldAdder extends Modifiers{

  private int myGold;

  private int multiplier =1;

  public GoldAdder(int gold){
    myGold = gold;
  }

  public void setMultiplier(int factor){
    multiplier = factor;
  }
  @Override
  public Consumer modifierFunction(Player[] players){
    return createConsumer();
  }

  @Override
  public Boolean checkConditions(CellInterface cell) {
    if (allowableStates.contains(cell.getCellState()) && !hasBeenAppliedAlready){
      hasBeenAppliedAlready = true;
      return true;
    }
    return false;
  }

  @Override
  protected Consumer createConsumer() {
    PlayerConsumer consumer = a->{
      a[0].addGold(myGold*multiplier);
    };
    return consumer;
  }

  @Override
  public String toString() {
    return "GoldAdder";
  }
}

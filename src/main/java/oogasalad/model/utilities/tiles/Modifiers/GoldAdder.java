package oogasalad.model.utilities.tiles.Modifiers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;

import oogasalad.model.players.Player;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

public class GoldAdder extends Modifiers{

  private boolean hasBeenAppliedAlready = false;
  private HashSet<CellState> allowableStates = new HashSet<>(Arrays.asList(
      new CellState[]{CellState.SHIP_SUNKEN, CellState.ISLAND_SUNK}));
  private int myGold;
  public GoldAdder(int gold){
    myGold = gold;
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
      a[0].addGold(myGold);
    };
    return consumer;
  }


  @Override
  public String toString() {
    return "GoldAdder";
  }
}

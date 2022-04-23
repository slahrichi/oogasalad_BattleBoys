package oogasalad.model.utilities.tiles.Modifiers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

public class GoldAdder extends Modifiers{

  private boolean hasBeenAppliedAlready = false;
  private HashSet<CellState> allowableStates = new HashSet<>(Arrays.asList(
      new CellState[]{CellState.SHIP_SUNKEN, CellState.ISLAND_SUNK}));

  public GoldAdder(int gold){
    PlayerConsumer consumer = a->{
      a[0].addGold(gold);
    };
    this.setMyConsumer(consumer);
  }

  @Override
  public Consumer modifierFunction(CellInterface cell){
    return myConsumer;
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
  public String toString() {
    return "GoldAdder";
  }
}

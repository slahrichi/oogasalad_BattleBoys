package oogasalad.model.utilities.tiles.Modifiers;

import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

public class GoldAdder extends PlayerModifier{

  private boolean hasBeenAppliedAlready = false;

  public GoldAdder(int gold){
    PlayerConsumer consumer = a->{
      a[0].addGold(gold);
    };
    this.setMyConsumer(consumer);
  }
  @Override
  public Boolean checkConditions(CellInterface cell) {
    if (cell.getCellState().equals(CellState.SHIP_SUNKEN) && !hasBeenAppliedAlready){
      hasBeenAppliedAlready = true;
      return true;
    }
    return false;
  }


  @Override
  public String toString() {
    return null;
  }
}

package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.model.utilities.tiles.CellInterface;

public class Burner extends Modifiers{


  private int count;
  private int dmg;
  public Burner(int dmg, int count){
    this.count = count;
    this.dmg   = dmg;
  }

  @Override
  public Consumer modifierFunction(CellInterface cell){
    return createConsumer();
  }

  public Boolean checkConditions(CellInterface cell){
    count--;
    return count>=0;
  }

  @Override
  protected Consumer createConsumer() {
    return new CellConsumer() {
      @Override
      public void accept(CellInterface cellInterface) {
        cellInterface.hit(dmg);
      }
    };
  }

  public String toString(){
    return "Burn " + dmg + " for " + count + " turns";
  };

}
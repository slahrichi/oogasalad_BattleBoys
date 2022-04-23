package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.model.utilities.tiles.CellInterface;

public class Burner extends Modifiers{


  private int count;
  private int dmg;
  public Burner(int dmg, int count){
    this.count = count;
    this.dmg   = dmg;
    setMyConsumer(new CellConsumer() {
      @Override
      public void accept(CellInterface cellInterface) {
        cellInterface.hit(dmg);
      }
    });
  }

  @Override
  public Consumer modifierFunction(CellInterface cell){
    return myConsumer;
  }

  public Boolean checkConditions(CellInterface cell){
    count--;
    return count>=0;
  }
  public String toString(){
    return "Brun " + dmg + " for " + count + " turns";
  };

}

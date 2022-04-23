package oogasalad.model.utilities.usables.items;

import java.util.function.BiConsumer;
import oogasalad.controller.GameManager;
import oogasalad.model.utilities.usables.Usable;

public abstract class Item extends Usable {

  public Item(String ID, int gold) {
    super(ID, gold);
    makeItemFunction();
  }

  protected abstract void makeItemFunction();


  @Override
  public BiConsumer<String, GameManager> handleUsage(){
    return (clickInfo, gm)-> {
      int id = Integer.parseInt(clickInfo.substring(clickInfo.lastIndexOf(" ") + 1));
      if(gm.getCurrentPlayer() == id) {
        gm.handleShot(clickInfo);
      }
      else
        throw new IllegalArgumentException("Please Choose an Your Own Board");
    };
  }

}

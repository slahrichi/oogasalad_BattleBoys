package oogasalad.model.utilities.usables.items;

import java.util.function.BiConsumer;
import oogasalad.controller.GameManager;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.UsableFunction;

public abstract class Item extends Usable {

  public Item(String ID, int gold) {
    super(ID, gold);
    makeItemFunction();
  }

  protected abstract UsableFunction  makeItemFunction();


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

  @Override
  public String getType() {
    return "PowerUp";
  }

  @Override
  public UsableFunction getFunction(){return makeItemFunction();}
}

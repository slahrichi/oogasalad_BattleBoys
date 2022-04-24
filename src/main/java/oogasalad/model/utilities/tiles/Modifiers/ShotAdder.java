package oogasalad.model.utilities.tiles.Modifiers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

public class ShotAdder extends Modifiers{

  private boolean hasBeenAppliedAlready = false;
  private int shotsAdded;



  public ShotAdder(int count){
    shotsAdded = count;
    setMyConsumer(new GameManagerConsumer() {
      @Override
      public void accept(GameManager gameManager) {
        gameManager.addRemainingShots(count);
      }
    });
  }

  @Override
  public Consumer modifierFunction(GameManager gm){
    return myConsumer;
  }

  @Override
  public String toString() {
    return "Add Shot by " + shotsAdded;
  }
}

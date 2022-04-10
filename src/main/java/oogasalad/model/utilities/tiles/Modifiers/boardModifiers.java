package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.tiles.CellInterface;

public abstract class boardModifiers implements modifiers{

  boardConsumer myConsumer;

  @Override
  public abstract Boolean checkConditions(CellInterface cell);

  public boardConsumer modifierFunction() {
    return myConsumer;
  }

}

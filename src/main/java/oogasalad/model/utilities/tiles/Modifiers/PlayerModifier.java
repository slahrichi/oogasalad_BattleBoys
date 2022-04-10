package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.model.utilities.tiles.CellInterface;

public abstract class PlayerModifier implements Modifiers {

  PlayerConsumer myConsumer;

  @Override
  public abstract Boolean checkConditions(CellInterface cell);

  @Override
  public Consumer modifierFunction() {
    return myConsumer;
  }
  @Override
  public abstract String toString();
}


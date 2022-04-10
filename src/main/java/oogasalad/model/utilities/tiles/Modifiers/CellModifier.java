package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.model.utilities.tiles.CellInterface;

public abstract class CellModifier implements Modifiers {
  CellConsumer myConsumer;

  @Override
  public abstract Boolean checkConditions(CellInterface cell);

  public Consumer modifierFunction() {
    return myConsumer;
  }
  @Override
  public abstract String toString();
}

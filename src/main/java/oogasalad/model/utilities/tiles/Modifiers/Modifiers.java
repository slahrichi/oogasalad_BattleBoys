package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.model.utilities.tiles.CellInterface;

public interface Modifiers {

  public Boolean checkConditions(CellInterface cell);

  public Consumer modifierFunction();
}

package oogasalad.model.utilities.tiles.Modifiers;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;
import java.util.function.Function;
import javafx.scene.control.Cell;
import oogasalad.model.utilities.tiles.CellInterface;

public interface modifiers {

  public Boolean checkConditions(CellInterface cell);

  public Consumer modifierFunction();
}

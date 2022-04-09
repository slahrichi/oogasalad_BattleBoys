package oogasalad.model.utilities.tiles;

import java.util.List;
import java.util.function.Function;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

public class IslandCell implements CellInterface {

  @Override
  public int hit() {
    return 0;
  }

  @Override
  public List<Function> boardUpdate() {
    return null;
  }

  @Override
  public List<Function> playerUpdate() {
    return null;
  }


  @Override
  public boolean canCarryObject() {
    return false;
  }

  @Override
  public void updateCoordinates(int row, int col) {

  }

  @Override
  public Coordinate getCoordinates() {
    return null;
  }

  @Override
  public CellState getCellState() {
    return null;
  }
}

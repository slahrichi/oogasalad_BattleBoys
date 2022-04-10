package oogasalad.model.utilities.tiles;

import java.util.List;
import java.util.function.Consumer;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

public class IslandCell implements CellInterface {

  @Override
  public CellState hit() {
    return CellState.ISLAND_HEALTHY;
  }

  @Override
  public List<Consumer> update() {
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

  @Override
  public int getHealth() {
    return 1;
  }
}

package oogasalad.model.utilities.tiles;

import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

public class WaterCell extends Cell implements CellInterface {

  public static final CellState WATER_CELL_INTIAL_STATE = CellState.WATER;
  public static final int WATER_HP_AMT = 1;

  public WaterCell(Coordinate coord){
    super(coord, WATER_CELL_INTIAL_STATE, WATER_HP_AMT);
  }


  @Override
  public CellState hit(int dmg) {
    return CellState.WATER_HIT;
  }

  @Override
  public boolean canCarryObject() {
    return true;
  }

  @Override
  public void moveCell(Coordinate nextMovement) {

  }
}

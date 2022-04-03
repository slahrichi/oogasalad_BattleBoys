package oogasalad.model.utilities.tiles;

import java.util.List;
import java.util.function.Function;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;

public class WaterCell implements Cell {

  private Coordinate myCoordinate;
  private cellStates currentState = cellStates.HEALTHY;

  public WaterCell(int row, int col){
    myCoordinate = new Coordinate(row, col);
  }

  public WaterCell(Coordinate coord){
    myCoordinate = coord;
  }

  @Override
  public int hit() {
    return 0;
  }

  @Override
  public List<Function> update() {
    return null;
  }

  @Override
  public boolean canCarryObject() {
    return true;
  }

  @Override
  public void updateCoordinates(int row, int col) {
    myCoordinate = new Coordinate(row,col);
  }

  @Override
  public Coordinate getCoordinates() {
    return myCoordinate;
  }
}

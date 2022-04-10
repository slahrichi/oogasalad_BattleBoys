package oogasalad.model.utilities.tiles;

import java.util.List;
import java.util.function.Function;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

public class WaterCell implements CellInterface {

  private Coordinate myCoordinate;
  private CellState currentState = CellState.WATER;

  public WaterCell(int row, int col){
    myCoordinate = new Coordinate(row, col);
  }

  public WaterCell(Coordinate coord){
    myCoordinate = coord;
  }

  @Override
  public CellState hit() {
    return CellState.WATER;
  }

  @Override
  public List<Function>boardUpdate() {
    return null;
  }

  @Override
  public List<Function>playerUpdate(){
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

  @Override
  public CellState getCellState() {
    return currentState;
  }

  @Override
  public int getHealth() {
    return 1;
  }
}

package oogasalad.model.utilities.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.enums.CellState;

public class WaterCell extends Cell implements CellInterface {

  public static final CellState WATER_CELL_INTIAL_STATE = CellState.WATER;
  public static final int WATER_HP_AMT = 1;
  //private Coordinate myCoordinate;
  //private CellState currentState = CellState.WATER;

  public WaterCell(Coordinate coord){
    super(coord, WATER_CELL_INTIAL_STATE, WATER_HP_AMT);
    //myCoordinate = coord;
  }


  @Override
  public CellState hit() {
    return CellState.WATER_HIT;
  }

  @Override
  public boolean canCarryObject() {
    return true;
  }

  /*
  @Override
  public List<Modifiers> update() {
    ArrayList<Modifiers> returnMods = new ArrayList<>();

    return returnMods;
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
  public void addModifier(Modifiers myMod) {
    return;
  }

  @Override
  public CellState getCellState() {
    return currentState;
  }

  @Override
  public int getHealth() {
    return 1;
  }

   */
}

package oogasalad.model.utilities.tiles;

import java.util.ArrayList;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.enums.CellState;

public class IslandCell extends Cell implements CellInterface {

  public static final CellState ISLAND_CELL_INITIAL_STATE = CellState.ISLAND_HEALTHY;

  //private Coordinate myCoordinate;
  //private int myHealthBar;
  //private CellState currentState;
  //private ArrayList<Modifiers> myModifiers = new ArrayList<>();

  public IslandCell(Coordinate coordinate, int health) {
    super(coordinate, ISLAND_CELL_INITIAL_STATE, health);
  }

  public IslandCell(Coordinate coordinate, int health, String id) {
    super(coordinate, ISLAND_CELL_INITIAL_STATE, health, id);
  }


  @Override
  public CellState hit(int dmg) {
    addToHealthBar(-dmg);
    setCellState(CellState.ISLAND_SUNK);
    if(getHealth()<=0) {
      return CellState.ISLAND_SUNK;
    }
    return CellState.ISLAND_HEALTHY;
  }


  @Override
  public boolean canCarryObject() {
    return false;
  }

  @Override
  public void moveCell(Coordinate nextMovement) {

  }
}

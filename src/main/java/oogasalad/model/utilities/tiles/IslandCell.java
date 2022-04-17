package oogasalad.model.utilities.tiles;

import java.util.ArrayList;
import java.util.List;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;

public class IslandCell extends Cell implements CellInterface {

  public static final CellState ISLAND_CELL_INITIAL_STATE = CellState.ISLAND_HEALTHY;

  private Coordinate myCoordinate;
  private int myHealthBar;
  private CellState currentState;
  private ArrayList<Modifiers> myModifiers = new ArrayList<>();

  public IslandCell(Coordinate coordinate, int health) {
    super(coordinate, ISLAND_CELL_INITIAL_STATE, health);
  }


  @Override
  public CellState hit(int dmg) {
    return CellState.ISLAND_HEALTHY;
  }


  @Override
  public boolean canCarryObject() {
    return false;
  }

  @Override
  public List<Modifiers> update() {
    ArrayList<Modifiers> returnMods = new ArrayList<>();
    for(Modifiers mod: myModifiers){
      if(mod.checkConditions(this)) returnMods.add(mod);
    }
    for(Modifiers currMod: returnMods){
      try {
        currMod.modifierFunction().accept(this);
        returnMods.remove(currMod);
      }catch(Exception e){
      }
    }
    return returnMods;
  }

}

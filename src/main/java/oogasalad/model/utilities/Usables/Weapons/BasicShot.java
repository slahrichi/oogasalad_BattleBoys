package oogasalad.model.utilities.Weapons;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;

public class BasicShot extends Weapon{

  public BasicShot(){
    super.myID = "Basic Shot";
    super.relativeCoordShots.add(new Coordinate(0,0));
    makeWeaponFunction();
  }


  @Override
  protected void updateRelativeCoords(Board currBoard) {
    return;
  }

  @Override
  protected void makeWeaponFunction() {
    super.myWeaponFunction = new WeaponFunction() {
      @Override
      public Map<Coordinate, CellState> apply(Coordinate coordinate, Board board) {
        Map<Coordinate, CellState> results = new HashMap<>();
        if(board.checkBoundedCoordinate())
      }
    }
  }
}

package oogasalad.model.utilities.usables.weapons;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.UsableFunction;

public class EmpoweredShot extends Weapon{

  int myDMG;
  public EmpoweredShot(String ID, int gold, int dmg){
    super(ID, gold);
    myDMG = dmg;
    addRelativePosition(new Coordinate(0,0), dmg);
  }

  @Override
  protected UsableFunction makeWeaponFunction() {
    UsableFunction ret = (coordinate, board) -> {
      Map<Coordinate, CellState> retMap = new HashMap<>();
      if(board.checkBoundedCoordinate(coordinate)){
        retMap.put(coordinate, board.hit(coordinate, myDMG));
        return retMap;
      }else{
        throw new NullPointerException("Coordinate does not exist in the Board");
      }
    };
    return ret;
  }
}

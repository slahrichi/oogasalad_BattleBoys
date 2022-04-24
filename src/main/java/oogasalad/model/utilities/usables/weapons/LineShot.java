package oogasalad.model.utilities.usables.weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.UsableFunction;

public class LineShot extends Weapon{
  /**
   * Hits an entire row or column
   */
  private int myDmg;
  public LineShot(String id, int gold, int dmg){
    super(id, gold);
    myDmg = dmg;

  }

  public LineShot(String[] parameters){
    this(parameters[0], Integer.parseInt(parameters[1]),Integer.parseInt(parameters[2]));
  }

  @Override
  protected UsableFunction makeWeaponFunction() {
    UsableFunction ret = (abs, board)->
    {
      Map<Coordinate, CellState> retMap = new HashMap<>();
      List<Coordinate> relative = new ArrayList<>();
      if(abs.getRow() == 0){
        for(int i = 0; i< board.getSize()[0]; i++){
          relative.add(new Coordinate(i, abs.getColumn()));
        }
      }else{
        for(int i = 0; i<board.getSize()[1]; i++){
          relative.add(new Coordinate(abs.getRow(), i));
        }
      }
      for(Coordinate coord : relative){
        retMap.put(coord, board.hit(coord, myDmg));
      }
      return retMap;
    };
    return ret;
  }
}

package oogasalad.model.utilities.usables.weapons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.WaterCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.UsableFunction;

public class SonarShot extends Weapon {

  private int myRadius;
  public SonarShot(String id, int gold, int radius){
    super(id, gold);
    this.myRadius = radius;
    generateRelativeCoords();

  }



  private void generateRelativeCoords(){
    int count = 0;
    for(int row = -1*myRadius ; row<myRadius; row++){
      for(int col = -1*myRadius; col<myRadius; col++){
        addRelativePosition(new Coordinate(row,col), 0);
      }
    }
  }
  @Override
  protected UsableFunction makeWeaponFunction() {
    UsableFunction ret =  (abs, board)->{
      Map<Coordinate, CellState> retValues = new HashMap<>();
      for(Coordinate coord: getRelativeCoordShots().keySet()){
        Coordinate currCoord = new Coordinate(abs.getRow()+coord.getRow(), abs.getColumn() + coord.getColumn());
        if(!(board.getCell(currCoord) instanceof WaterCell))
          retValues.put(currCoord, CellState.SCANNED);
        else
          retValues.put(currCoord, board.hit(currCoord, 1));
      }
      return  retValues;
    };
    return ret;
  }
}

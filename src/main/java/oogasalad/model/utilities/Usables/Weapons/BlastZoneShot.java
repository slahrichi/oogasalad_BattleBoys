package oogasalad.model.utilities.Usables.Weapons;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

/**
 * Has starting damage and decreases as radius increases.
 */
public class BlastZoneShot extends Weapon {

  int myStartingDmg;
  public BlastZoneShot(String ID, int gold,  int startingDmg){
    super(ID, gold);
    myStartingDmg = startingDmg;
    generateRelativeCoords();
    makeWeaponFunction();
  }

  private void generateRelativeCoords(){
    int count = 0;
    for(int tempDmg = myStartingDmg; tempDmg>0; tempDmg--){
      for(int row = -1*count; row<=count; row++){
        for(int col = -1*count; col<=count; col++){
          if(!getRelativeCoordShots().containsKey(new Coordinate(row, col)))
            addRelativePosition(new Coordinate(row,col), tempDmg);
        }
      }
      count++;
    }
  }

  @Override
  protected void makeWeaponFunction() {
    setMyFunction((coordinate, board) -> {
      Map<Coordinate, CellState> retMap = new HashMap<>();
      Map<Coordinate, Integer> relativeCoords = getRelativeCoordShots();
      for(Coordinate coord : relativeCoords.keySet()){
        if(board.checkBoundedCoordinate(coord)){
          retMap.put(coord, board.hit(coord, relativeCoords.get(coord)));
        }
      }
      return retMap;
    });
  }
}

package oogasalad.model.utilities.Usables.Weapons;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

public class RandomShot extends Weapon{
  /**
   * Define an nxn area and k shots will shot at random in that area
   */
  int numShots;
  int radius;
  int dmgPerShot;
  public RandomShot(String id, int gold, int radius, int numShots, int dmgPerShot){
    super(id, gold);
    this.numShots = numShots;
    this.radius = radius;
    this.dmgPerShot = dmgPerShot;
  }

  @Override
  protected void makeWeaponFunction() {
    setMyFunction((absolute, board) ->{
      Set<Coordinate> relatives = new HashSet<>();
      Map<Coordinate, CellState> retMap =new HashMap<>();
      while(relatives.size()<numShots){
        Random rand = new Random();
        relatives.add(new Coordinate(rand.nextInt(radius), rand.nextInt(radius)));
      }
      for(Coordinate coord:relatives){
        Coordinate hitCoord = new Coordinate(coord.getRow()+absolute.getRow(), coord.getColumn() + coord.getRow());
        if(board.checkBoundedCoordinate(hitCoord)){
          retMap.put(hitCoord, board.hit(hitCoord, dmgPerShot));
        }
      }
      return retMap;
      });
  };
}

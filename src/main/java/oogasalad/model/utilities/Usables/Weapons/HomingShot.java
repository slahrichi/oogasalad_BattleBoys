package oogasalad.model.utilities.Usables.Weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.tiles.ShipCell;

public class HomingShot extends Weapon{
  /**
   * Choose an nxn area and if there is at least one piece, then it will home in on that shiptile. Modify to add more homing shots at once
   */
  int numShots;
  int radius;
  int dmgPerShot;

  public HomingShot(String id, int gold, int radius, int numShots, int dmgPerShot){
    super(id, gold);
    this.numShots = numShots;
    this.radius = radius;
    this.dmgPerShot = dmgPerShot;
    for(int i = 0; i<radius; i++){
      for(int j = 0; j<radius; j++){
        addRelativePosition(new Coordinate(i,j), dmgPerShot);
      }
    }
  }

  @Override
  protected void makeWeaponFunction() {
    setMyFunction((absolute, board) ->{
      List<Coordinate> coordsWithShips = new ArrayList<>();
      List<Coordinate> coordsWithIslands = new ArrayList<>();
      for(int i = 0; i<radius; i++){
        for(int j = 0; j<radius; j++){
          if(board.getCell(new Coordinate(i,j)) instanceof ShipCell)
            coordsWithShips.add(new Coordinate(i,j));
          else if (board.getCell(new Coordinate(i,j)) instanceof IslandCell)
            coordsWithIslands.add(new Coordinate(i,j));
        }
      }

      Set<Coordinate> relatives = new HashSet<>();
      Map<Coordinate, CellState> retMap =new HashMap<>();
      while(relatives.size()<numShots && !coordsWithIslands.isEmpty() && !coordsWithShips.isEmpty()){
        Random rand = new Random();
        if(!coordsWithShips.isEmpty()){
          Coordinate c = coordsWithShips.get(rand.nextInt(coordsWithShips.size()));
          relatives.add(c);
          coordsWithShips.remove(c);
        }
        else if(!coordsWithIslands.isEmpty()) {
          Coordinate c = coordsWithIslands.get(rand.nextInt(coordsWithShips.size()));
          relatives.add(c);
          coordsWithShips.remove(c);
        }
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

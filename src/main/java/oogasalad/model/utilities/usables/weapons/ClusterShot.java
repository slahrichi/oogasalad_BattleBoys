package oogasalad.model.utilities.usables.weapons;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

public class ClusterShot extends Weapon{


  public ClusterShot(String id, int gold, Map<Coordinate, Integer> relativeCoordinates) {
    super(id, gold);
    setRelativeCoordShots(relativeCoordinates);
  }

  @Override
  protected void makeWeaponFunction() {
    setMyFunction((absolute, board) -> {
      Map<Coordinate, Integer> relativeCoords = getRelativeCoordShots();
      Map<Coordinate, CellState> returnStates = new HashMap<>();
      for(Coordinate relative : relativeCoords.keySet()){
        Coordinate mappedCoord = new Coordinate(relative.getRow() + absolute.getRow(),relative.getColumn() + absolute.getColumn());
        if(board.checkBoundedCoordinate( mappedCoord)){
          returnStates.put(mappedCoord, board.hit(mappedCoord, relativeCoords.get(relative)));
        }
      }
      return returnStates;
    });
  }

}

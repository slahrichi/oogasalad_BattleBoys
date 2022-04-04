package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.List;
import oogasalad.model.utilities.tiles.ShipCell;

public class Weapon {

  List<Coordinate> myHitMap;

  public Weapon(List<Coordinate> hitMap) {
    myHitMap = hitMap;
  }

  public List<Coordinate> getHitMap() {
    return myHitMap;
  }

  public void applyWeaponEffect(ShipCell target) {

  }

  public List<Coordinate> getAffectedCellCoordinates(ShipCell target) {
    List<Coordinate> result = new ArrayList<>();
    Coordinate targetCord = target.getCoordinates();
    for (Coordinate aoe : myHitMap) {
      result.add(new Coordinate(target.getCoordinates().getRow() + aoe.getRow(),
          target.getCoordinates().getColumn() + aoe.getColumn()));
    }
  return  result;
  }
}

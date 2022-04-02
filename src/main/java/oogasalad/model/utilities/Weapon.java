package oogasalad.model.utilities;

import java.util.List;
import oogasalad.model.utilities.tiles.ShipCell;

public class Weapon {
  List<Coordinate> myHitMap;
  public Weapon(List<Coordinate> hitMap){
    myHitMap=hitMap;
  }

  public List<Coordinate> getHitMap(){
    return myHitMap;
  }

  public void applyWeaponEffect(ShipCell target){

  }
}

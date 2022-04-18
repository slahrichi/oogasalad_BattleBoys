package oogasalad.model.utilities.Usables.Weapons;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;
import oogasalad.model.utilities.tiles.ShipCell;
/**
 * Hits  a tile and if its a ship, then it will apply a modifier that burns the ship overtime */

public class BurnShot extends Weapon {

  int dmgPerTurn;
  int turnsLasted;
  public BurnShot(String id, int gold, int dmgPerTurn, int turnsLasted){
    super(id, gold);
    this.dmgPerTurn = dmgPerTurn;
    this.turnsLasted = turnsLasted;
    addRelativePosition(new Coordinate(0,0), 1);
    makeWeaponFunction();
  }


  @Override
  protected void makeWeaponFunction() {
    setMyFunction((coord, board) ->{
      Map<Coordinate, CellState> retMap = new HashMap<>();
      if(board.canBeStruck(coord)){
        if(board.getCell(coord) instanceof ShipCell){
        //  board.getCell(coord).addModifier(BurnModifier);  Burn Modifier needs to implemented at the moment
        // board.getCell(coord).addModifier(updateFrontEndModifier);
        }
        else
          retMap.put(coord, board.hit(coord, dmgPerTurn));
        return retMap;
      } else{
        throw new NullPointerException("Coordinate Out of Bounds");
      }
    });
  }

}

package oogasalad.model.utilities.usables.weapons;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Modifiers.Burner;
import oogasalad.model.utilities.tiles.Modifiers.FrontEndUpdater;
import oogasalad.model.utilities.tiles.enums.CellState;
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
  public BurnShot(String[] parameters){
    this(parameters[0], Integer.parseInt(parameters[1]),Integer.parseInt(parameters[2]), Integer.parseInt(parameters[3]));
  }

  @Override
  protected void makeWeaponFunction() {
    setMyFunction((coord, board) ->{
      Map<Coordinate, CellState> retMap = new HashMap<>();
      if(board.canBeStruck(coord)){
        if(board.getCell(coord) instanceof ShipCell){
          board.getCell(coord).addModifier(new Burner(dmgPerTurn, turnsLasted));
          board.getCell(coord).addModifier(new FrontEndUpdater(turnsLasted));
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

package oogasalad.model.utilities.usables.weapons;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;

public class MoltovShot extends Weapon{
  /**
   * If it hits a tile, then it spreads to the rest of the piece
   */
  private int dmg;
  public MoltovShot(String id, int gold, int dmg){
    super(id, gold);
    this.dmg = dmg;
    addRelativePosition(new Coordinate(0,0), dmg);
  }

  public MoltovShot(String[] parameters){
    this(parameters[0], Integer.parseInt(parameters[1]),Integer.parseInt(parameters[2]));
  }

  @Override
  protected void makeWeaponFunction(){
    setMyFunction((abs, board)->{
      CellInterface cell = board.getCell(abs);
      Map<Coordinate, CellState> resMap = new HashMap<>();
      resMap.put(abs, board.hit(abs,dmg));
      if(board.getCell(abs) instanceof ShipCell){
        for(ShipCell shipcell : board.getPiece(((ShipCell) cell).getId()).getAllCells()){
          if(cell != shipcell){
            shipcell.hit(dmg);
           resMap.put(shipcell.getCoordinates(), shipcell.getCellState());
          }
        }
      }
      return resMap;
    });
  }

}

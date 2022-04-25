package oogasalad.model.utilities.usables.items;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.UsableFunction;

public class ShipHeal extends Item{

  private int healPower;
  public ShipHeal(String ID, int gold, int healPower) {
    super(ID, gold);
    this.healPower = healPower;
  }


  @Override
  protected UsableFunction makeItemFunction(){
    UsableFunction ret = (abs, board)->{
      CellInterface cell = board.getCell(abs);
      Map<Coordinate, CellState> resMap = new HashMap<>();
      if(board.getCell(abs) instanceof ShipCell){
        for(ShipCell shipcell : board.getPiece(((ShipCell) board.getCell(abs)).getId()).getAllCells()){
            shipcell.hit(-1*healPower);
            resMap.put(shipcell.getCoordinates(), shipcell.getCellState());
        }
      }
      return resMap;
    };
    return ret;
  }

}

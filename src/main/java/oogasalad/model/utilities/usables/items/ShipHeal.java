package oogasalad.model.utilities.usables.items;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;

public class ShipHeal extends Item{

  private int healPower;
  public ShipHeal(String ID, int gold, int healPower) {
    super(ID, gold);
    this.healPower = healPower;
  }

  @Override
  protected void makeItemFunction(){
    setMyFunction((abs, board)->{
      CellInterface cell = board.getCell(abs);
      Map<Coordinate, CellState> resMap = new HashMap<>();
      resMap.put(abs, board.hit(abs,healPower));
      if(board.getCell(abs) instanceof ShipCell){
        for(ShipCell shipcell : board.getPiece(((ShipCell) cell).getId()).getAllCells()){
          if(cell != shipcell){
            shipcell.hit(healPower);
            resMap.put(shipcell.getCoordinates(), shipcell.getCellState());
          }
        }
      }
      return resMap;
    });
  }

}

package oogasalad.model.utilities;

import java.util.List;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.ShipCell;

public class StaticPiece extends Piece {
  public StaticPiece(List<ShipCell> cellList){
    super(cellList);
  }

  @Override
  public void registerDamage(ShipCell hitLocation) {

       if (getCellList().contains(hitLocation)) {
         updateStatus("Damaged");
         getCellList().remove(hitLocation);
       }
       if (checkDeath()) {
         updateStatus("Dead");
       }
     }




}

package oogasalad.model.utilities;

import java.util.List;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.ShipCell;

public class StaticPiece extends Piece {
  public StaticPiece(List<ShipCell> cellList){
    super(cellList);
  }

  @Override
  public void registerDamage(Coordinate hitLocation) {

    // cell.getPosition() doesn't exist
    for (Cell cell : getCellList()) {
     if (cell.getCoordinates().getColumn() == hitLocation.getColumn() && cell.getCoordinates().getRow()== hitLocation.getRow()) {
       if (getHPList().contains(hitLocation)) {
         updateStatus("Damaged");
         getHPList().remove(cell.getCoordinates());
       }
       if (checkDeath()) {
         updateStatus("Dead");
       }
     }
    }
  }


}

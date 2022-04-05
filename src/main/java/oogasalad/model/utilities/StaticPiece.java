package oogasalad.model.utilities;

import java.util.List;
import oogasalad.model.utilities.tiles.ShipCell;

public class StaticPiece extends Piece {

  public StaticPiece(List<ShipCell> cellList, List<Coordinate> relativeCoords, String id) {
    super(cellList, relativeCoords, id);
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

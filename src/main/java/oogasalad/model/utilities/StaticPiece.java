package oogasalad.model.utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import oogasalad.model.utilities.tiles.ShipCell;

public class StaticPiece extends Piece implements Serializable {

  public StaticPiece(List<ShipCell> cellList, List<Coordinate> relativeCoords, String id) {
    super(cellList, relativeCoords, new ArrayList<>(), id);
  }
  public StaticPiece(String id){
    super(id);
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

  @Override
  public Piece copyOf() {
    return new StaticPiece(List.copyOf(getCellList()), List.copyOf(getRelativeCoords()), getID());
  }


}

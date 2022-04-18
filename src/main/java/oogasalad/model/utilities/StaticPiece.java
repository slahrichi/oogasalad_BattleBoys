package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oogasalad.model.utilities.tiles.ShipCell;

public class StaticPiece extends Piece {

  public static final List<Coordinate> NO_MOVEMENT_PATH = Collections.unmodifiableList(new ArrayList<Coordinate>());

  public StaticPiece(List<ShipCell> cellList, List<Coordinate> relativeCoords, String id) {
    super(cellList, relativeCoords, NO_MOVEMENT_PATH, id);
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

package oogasalad.model.utilities.Pieces;

import java.util.List;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.ShipCell;

public class MovingPiece extends Piece{

  public MovingPiece(List<ShipCell> cellList, List<Coordinate> relativeCoords, List<Coordinate> patrolPath, String id) {
    super(cellList, relativeCoords, patrolPath,  id);
  }

  public MovingPiece(MovingPiece piece) {
    super(piece);

  }

  public MovingPiece(String id){
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
    //return new MovingPiece(List.copyOf(getCellList()), List.copyOf(getRelativeCoords()),List.copyOf() getID());
    return new MovingPiece(this);
  }


}

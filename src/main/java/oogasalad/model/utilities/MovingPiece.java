package oogasalad.model.utilities;

import java.util.List;
import oogasalad.model.utilities.tiles.ShipCell;

public class MovingPiece extends Piece {

  private List<Coordinate> myPatrolPath;
  public MovingPiece(List<ShipCell> cellList, List<Coordinate> relativeCoords, List<Coordinate> patrolPath, String id) {
    super(cellList, relativeCoords, patrolPath,  id);
    myPatrolPath = patrolPath;
  }


  @Override
  public Piece copyOf() {
    return new MovingPiece(List.copyOf(getCellList()), List.copyOf(getRelativeCoords()), List.copyOf(myPatrolPath), getID());

  }


}

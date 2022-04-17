package oogasalad.model.utilities.Pieces;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.ShipCell;

public class PieceMover {
  public static final Coordinate NO_ROUTE_MOVEMENT = new Coordinate(0,0);

  private Queue<Coordinate> route;

  public PieceMover(List<Coordinate> directionList) {
    route = new LinkedList<Coordinate>(directionList);
  }

  public PieceMover(PieceMover parent) {
    route = new LinkedList<Coordinate>(parent.route);
  }

  public void moveCells(List<ShipCell> pieceCells, Map<Coordinate, CellInterface> boardMap) {
    Coordinate nextMovement = getNextMovement();
    if(canPieceMove(nextMovement, pieceCells, boardMap)) {
      for(CellInterface cell: pieceCells) {
        cell.moveCell(nextMovement);
      }
    }
  }

  private boolean canPieceMove(Coordinate nextMovement, List<ShipCell> pieceCells, Map<Coordinate, CellInterface> boardMap) {
    for(CellInterface cell: pieceCells) {
      Coordinate moveLocation = Coordinate.sum(nextMovement, cell.getCoordinates());
      if(!(boardMap.containsKey(moveLocation) && boardMap.get(moveLocation).canCarryObject())) {
        return false;
      }
    }
    return true;
  }

  private Coordinate getNextMovement() {
    if(route.isEmpty()) {
      return NO_ROUTE_MOVEMENT;
    }
    else {
      Coordinate nextMovement = route.poll();
      Coordinate inverseMovement = new Coordinate(-nextMovement.getRow(), -nextMovement.getColumn());
      route.offer(inverseMovement);
      return nextMovement;
    }
  }
}

package oogasalad.model.utilities;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import oogasalad.model.utilities.tiles.CellInterface;

public class PieceMover {
  public static final Coordinate NO_ROUTE_MOVEMENT = new Coordinate(0,0);

  private Queue<Coordinate> route;

  public PieceMover(List<Coordinate> directionList) {
    route = new LinkedList<Coordinate>(directionList);
  }

  public void moveCells(List<CellInterface> pieceCells) {
    Coordinate nextMovement = getNextMovement();
    for(CellInterface cell: pieceCells) {
      cell.moveCell(nextMovement);
    }
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

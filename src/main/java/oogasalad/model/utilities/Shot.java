package oogasalad.model.utilities;

import javafx.scene.control.Cell;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

public class Shot {

  private Coordinate myRelativeCoordinate;
  private int myDamage;
  public Shot(Coordinate relCoord, int damage) {
    myRelativeCoordinate = relCoord;
    myDamage = damage;
  }

  public CellState applyShot(CellInterface affectedCell) {
    for(int i = 0; i<myDamage; i++) {
      affectedCell.hit();
    }
    return affectedCell.getCellState();
  }

  public Coordinate getShotAbsoluteCoord(Coordinate absoluteCoordinate) {
    return new Coordinate(absoluteCoordinate.getRow()+myRelativeCoordinate.getRow(), absoluteCoordinate.getColumn()+myRelativeCoordinate.getColumn());
  }
}
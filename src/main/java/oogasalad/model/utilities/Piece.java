package oogasalad.model.utilities;

import java.util.List;
import oogasalad.model.utilities.tiles.Cell;

public class Piece {

  private List<Cell> cellList;

  public Piece(List<Cell> cellList) {
    this.cellList = cellList;
  }

  public List<Cell> getCellList() {
    return cellList;
  }

}

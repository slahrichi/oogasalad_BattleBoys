package oogasalad.model.utilities;

import java.util.List;

public class Piece {

  private List<Cell> cellList;

  public Piece(List<Cell> cellList) {
    this.cellList = cellList;
  }

  public List<Cell> getCellList() {
    return cellList;
  }

}

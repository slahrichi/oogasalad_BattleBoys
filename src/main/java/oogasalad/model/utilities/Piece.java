package oogasalad.model.utilities;

import java.util.List;
import oogasalad.model.utilities.tiles.ShipTile;
import oogasalad.model.utilities.tiles.Tile;

public class Piece {

  private List<Tile> cellList;

  public Piece(List<Tile> cellList) {
    this.cellList = cellList;
  }

  public List<Tile> getCellList() {
    return cellList;
  }

  public void deadPart(ShipTile shipTile) {
  }
}

package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.ShipCell;

public abstract class Piece {

  private List<ShipCell> cellList;
  private List<Coordinate> cellListHP = new ArrayList<>();
  private String status;
  private Board myBoard;
  private String pieceId;

  public Piece(List<ShipCell> cellList, String id) {
    this.cellList = cellList;
    status = "Alive";
    pieceId = id;
    intializeHPList(cellList);
    //myBoard = board;
  }

  public List<ShipCell> getCellList() {
    return cellList;
  }

  private void intializeHPList(List<ShipCell> shape) {
    for (ShipCell cell : shape) {
      cellListHP.add(cell.getCoordinates());
    }
  }

  public abstract void registerDamage(ShipCell hitLocation);

  protected boolean checkDeath() {
    return (cellList.size() == 0);
  }

  public String getStatus() {
    return status;
  }


  public Consumer<Map<Coordinate, Cell>> update() {
    return null;
  }

  protected void updateStatus(String newStatus) {
    status = newStatus;
  }

  public List<Coordinate> getHPList() {
    return cellListHP;
  }

  public String getID(){return pieceId;}
}


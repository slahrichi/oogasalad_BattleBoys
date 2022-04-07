package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.CellState;
import oogasalad.model.utilities.tiles.ShipCell;

public abstract class Piece {

  private List<ShipCell> cellList;
  private List<ShipCell> allCells;
  private List<Coordinate> cellListHP = new ArrayList<>();
  private List<Coordinate> myRelativeCoords;
  private String status;
  private String pieceId;

  public Piece(List<ShipCell> cells, List<Coordinate> relativeCoords, String id) {
    status = "Alive";
    pieceId = id;
    cellList = cells;
    allCells = new ArrayList<>(cellList);
    myRelativeCoords = relativeCoords;
    //intializeHPList(cellList);
  }

  public Piece(List<ShipCell> cells, String id){
    status = "Alive";
    pieceId = id;
    cellList = cells;
    allCells = new ArrayList<>(cellList);
    for(ShipCell cell: cellList){
      myRelativeCoords.add(cell.getCoordinates());
    }
  }


  public void placeCellsAt(Coordinate absoluteCoord) {
    for(ShipCell c: cellList) {
      c.placeAt(absoluteCoord);
    }
  }

  public List<ShipCell> getCellList() {
    return cellList;
  }
  public List<ShipCell> getAllCells() {return allCells;}

  public void initializeHPList() {
    for (ShipCell cell : cellList) {
      cellListHP.add(cell.getCoordinates());
    }
  }

  public Collection<Coordinate> getRelativeCoords() {
    return myRelativeCoords; //get immutable version
  }

  public abstract void registerDamage(ShipCell hitLocation);

  protected boolean checkDeath() {
    return (cellList.size() == 0);
  }

  public String getStatus() {
    return status;
  }

  public Consumer<Map<Coordinate, CellInterface>> update() {
    return null;
  }

  protected void updateStatus(String newStatus) {
    status = newStatus;
  }

  public List<Coordinate> getHPList() {
    return cellListHP;
  }

  public String getID(){return pieceId;}

  public abstract Piece copyOf();

}


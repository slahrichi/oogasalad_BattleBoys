package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.WaterCell;
import oogasalad.model.utilities.tiles.enums.CellState;

public abstract class Piece {

  private List<ShipCell> cellList;
  private List<ShipCell> allCells;
  private PieceMover myMover;
  private List<Coordinate> cellListHP = new ArrayList<>();
  private List<Coordinate> myRelativeCoords;
  private String status;
  private String pieceId;

  public Piece(List<ShipCell> cells, List<Coordinate> relativeCoords, List<Coordinate> patrolPath, String id) {
    status = "Alive";
    pieceId = id;
    cellList = createNewCellListInstance(cells);
    allCells = new ArrayList<>(cellList);
    myRelativeCoords = relativeCoords;
    myMover = new PieceMover(patrolPath);
    //intializeHPList(cellList);
  }

  public void movePiece(Map<Coordinate, CellInterface> boardMap) {
    myMover.moveCells(cellList, boardMap);
  }


  public void removeFromBoard(Map<Coordinate, CellInterface> boardMap) {
    for(ShipCell currCell: allCells) {
      boardMap.put(currCell.getCoordinates(), new WaterCell(currCell.getCoordinates()));
    }
    cellListHP.clear();
  }


  private List<ShipCell> createNewCellListInstance(List<ShipCell> cells) {
    List<ShipCell> newCellList = new ArrayList<>();
    for(ShipCell c: cells) {
      newCellList.add(new ShipCell(c));
    }
    return newCellList;
  }

  public Piece(String id){
    status = "Alive";
    pieceId = id;
  }

  public void initCellList(List<ShipCell> cells){
    cellList = cells;
    allCells = new ArrayList<>(cellList);
    myRelativeCoords = new ArrayList<Coordinate>();
    for(ShipCell cell: cellList){
      myRelativeCoords.add(cell.getRelativeCoordinate());
    }
  }

  public void placeCellsAt(Coordinate absoluteCoord) {
    for(ShipCell c: cellList) {
      c.placeAt(absoluteCoord);
    }
  }

  public Collection<Coordinate> getAbsoluteCoords() {
    List<Coordinate> coords = new ArrayList<>();
    for (ShipCell ship : cellList) {
      coords.add(ship.getCoordinates());
    }
    return coords;
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
    System.out.println("Ship's hp is " + cellList.size());
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

  public String getID(){return pieceId;}

  public abstract Piece copyOf();

  public void updateShipHP() {
    Iterator<ShipCell> itr = cellList.iterator();
    while(itr.hasNext()) {
      ShipCell currCell = itr.next();
      if(currCell.getCellState()== CellState.SHIP_SUNKEN) {
        itr.remove();
        cellListHP.remove(currCell.getCoordinates());
      }
    }
  }

  @Override
  public boolean equals(Object o) {
    if(o == null) return false;
    if(o == this) return true;
    if(!(o instanceof Piece)) return false;
    Piece other = (Piece)o;
    //just need to check cellList
    return this.cellList.equals(other.cellList);
  }

}


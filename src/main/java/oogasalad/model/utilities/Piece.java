package oogasalad.model.utilities;

import java.util.*;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.tiles.WaterCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Piece Class that represents a ship piece in the game. This encapsulates the ship's cells and keeps track of the ship's
 * remaining alive cells
 *
 * Assuptions:
 * - ShipCell id's match the piece id
 * - Relative Coordinates match the coordinates of the piece's cells
 *
 * Dependencies:
 * - ShipCell: Piece is built up by a collection of ShipCells
 *
 * @author Brandon Bae, Luka Mdivani, Prajwal Jagadish
 */
public abstract class Piece {

  private static final Logger LOG = LogManager.getLogger(Piece.class);
  private List<ShipCell> cellList;
  private List<ShipCell> allCells;
  private PieceMover myMover;
  private List<Coordinate> myRelativeCoords;
  private String pieceId;

  public Piece(List<ShipCell> cells, List<Coordinate> relativeCoords, List<Coordinate> patrolPath, String id) {
    pieceId = id;
    cellList = createNewCellListInstance(cells);
    allCells = new ArrayList<>(cellList);
    myRelativeCoords = relativeCoords;
    myMover = new PieceMover(patrolPath);
  }

  private List<ShipCell> createNewCellListInstance(List<ShipCell> cells) {
    List<ShipCell> newCellList = new ArrayList<>();
    for(ShipCell c: cells) {
      newCellList.add(new ShipCell(c));
    }
    return newCellList;
  }

  protected void movePiece(Map<Coordinate, CellInterface> boardMap) {
    myMover.moveCells(allCells, boardMap);
  }

  protected void removeFromBoard(Map<Coordinate, CellInterface> boardMap) {
    for(ShipCell currCell: allCells) {
      boardMap.put(currCell.getCoordinates(), new WaterCell(currCell.getCoordinates()));
    }
  }

  protected void placeCellsAt(Coordinate absoluteCoord) {
    for(ShipCell c: cellList) {
      c.placeAt(absoluteCoord);
    }
  }

  protected boolean checkDeath() {
    LOG.info(String.format("Ship %s has %d remaining alive cells", pieceId, cellList.size()));
    return (cellList.size() == 0);
  }


  public List<ShipCell> getCellList() {
    return cellList;
  }
  public List<ShipCell> getAllCells() {return allCells;}


  public Collection<Coordinate> getRelativeCoords() {
    return Collections.unmodifiableList(myRelativeCoords);
  }


  public void registerDamage(ShipCell hitLocation) {
    if (getCellList().contains(hitLocation)) {
      getCellList().remove(hitLocation);
    }
  }

  public String getID(){return pieceId;}

  public abstract Piece copyOf();

  public void updateShipHP() {
    Iterator<ShipCell> itr = cellList.iterator();
    while(itr.hasNext()) {
      ShipCell currCell = itr.next();
      if(currCell.getCellState()== CellState.SHIP_SUNKEN) {
        itr.remove();
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


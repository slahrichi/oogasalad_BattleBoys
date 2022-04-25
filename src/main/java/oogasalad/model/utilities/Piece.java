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
 * - PieceMover: Helper Class that handles moving pieces
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

  /**
   * Piece constructor that creates a piece instance based off given parameters
   * @param cells List of ShipCells that make up the pieces
   * @param relativeCoords List of Coordinates that show the relative locations of the Piece's ShipCells from a top left reference
   * @param patrolPath List of Coordinates that represent the relative movements the piece should make each step of its patrol route
   * @param id String ID of the piece
   */
  public Piece(List<ShipCell> cells, List<Coordinate> relativeCoords, List<Coordinate> patrolPath, String id) {
    pieceId = id;
    cellList = createNewCellListInstance(cells);
    allCells = new ArrayList<>(cellList);
    myRelativeCoords = relativeCoords;
    myMover = new PieceMover(patrolPath);
  }

  //helper method to create a new instance of the given List of cells
  //important as without this helper method all the instance of pieces of the same type will have the same List<ShipCell>
  //instance which can lead to pieces of the same type being sunk on everyone's board.
  private List<ShipCell> createNewCellListInstance(List<ShipCell> cells) {
    List<ShipCell> newCellList = new ArrayList<>();
    for(ShipCell c: cells) {
      newCellList.add(new ShipCell(c));
    }
    return newCellList;
  }

  /**
   * Moves the piece based off its PieceMover and route instance
   * @param boardMap Map to move the piece and its corresponding cells on
   */
  protected void movePiece(Map<Coordinate, CellInterface> boardMap) {
    myMover.moveCells(allCells, boardMap);
  }

  /**
   * Remove the current piece from the given board
   * @param boardMap Map to remove the piece and its corresponding cells from
   */
  protected void removeFromBoard(Map<Coordinate, CellInterface> boardMap) {
    for(ShipCell currCell: allCells) {
      boardMap.put(currCell.getCoordinates(), new WaterCell(currCell.getCoordinates()));
    }
  }

  /**
   * Place all the cells in the piece at the given absolute coordinate (represents top left corner of piece)
   * @param absoluteCoord absolute coordinate to place piece at (represents top left corner of piece)
   */
  protected void placeCellsAt(Coordinate absoluteCoord) {
    for(ShipCell c: cellList) {
      c.placeAt(absoluteCoord);
    }
  }

  /**
   * Checks whether the piece is dead or not
   * @return
   */
  protected boolean checkDeath() {
    LOG.info(String.format("Ship %s has %d remaining alive cells", pieceId, cellList.size()));
    return (cellList.size() == 0);
  }

  /**
   * Method to get CellList
   * @return unmodifiable copy of instance variable cellList
   */
  public List<ShipCell> getCellList() {
    return Collections.unmodifiableList(cellList);
  }

  /**
   * Method to get allCells
   * @return unmodifiable copy of instance variable allCells
   */
  public List<ShipCell> getAllCells() {
    return Collections.unmodifiableList(allCells);
  }

  /**
   * Method to get myRelativeCoordinates
   * @return unmodifiable copy of instance variable myRelativeCoords
   */
  public Collection<Coordinate> getRelativeCoords() {
    return Collections.unmodifiableList(myRelativeCoords);
  }

  /**
   * Removes the given Shipcell from cellList (List acts as an hp bar of sorts)
   * @param hitLocation ShipCell that was hit
   */
  public void registerDamage(ShipCell hitLocation) {
    if (getCellList().contains(hitLocation)) {
      getCellList().remove(hitLocation);
    }
  }

  /**
   * Getter for piece ID
   * @return String pieceID
   */
  public String getID(){return pieceId;}

  /**
   * Method that returns a copy of the current piece with the same instance variables
   * @return Piece copy that has the same instance variables as the current piece
   */
  public abstract Piece copyOf();

  /**
   * Updates the pieces cellList with only remaining alive ShipCells
   */
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


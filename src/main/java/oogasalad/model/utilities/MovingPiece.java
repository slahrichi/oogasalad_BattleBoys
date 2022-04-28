package oogasalad.model.utilities;

import java.util.List;
import oogasalad.model.utilities.tiles.ShipCell;

/**
 * Subclass of Piece that has an actual patrol route it moves along
 *
 * Assumptions:
 * - myPatrolPath is not empty
 *
 * @author Brandon Bae
 */
public class MovingPiece extends Piece {

  private List<Coordinate> myPatrolPath;

  /**
   * Constructor for MovingPiece
   * @param cellList List of ShipCells that make up the pieces
   * @param relativeCoords List of Coordinates that show the relative locations of the Piece's ShipCells from a top left reference
   * @param patrolPath List of Coordinates that represent the relative movements the piece should make each step of its patrol route
   * @param id String ID of the piece
   */
  public MovingPiece(List<ShipCell> cellList, List<Coordinate> patrolPath, List<Coordinate> relativeCoords, String id) {
    super(cellList, relativeCoords, patrolPath,  id);
    myPatrolPath = patrolPath;
  }


  /**
   * Method that returns a copy of the current piece with the same instance variables
   * @return Piece copy that has the same instance variables as the current piece
   */
  @Override
  public Piece copyOf() {
    return new MovingPiece(List.copyOf(getCellList()), List.copyOf(getRelativeCoords()), List.copyOf(myPatrolPath), getID());

  }


}

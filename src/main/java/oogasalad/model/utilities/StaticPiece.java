package oogasalad.model.utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.ShipCell;

/**
 * Subclass of Piece that does not move
 *
 * @author Brandon Bae
 */
public class StaticPiece extends Piece {

  public static final List<Coordinate> NO_MOVEMENT_PATH = Collections.unmodifiableList(new ArrayList<Coordinate>());

  /**
   * Constructor for StaticPiece
   * @param cellList List of ShipCells that make up the pieces
   * @param relativeCoords List of Coordinates that show the relative locations of the Piece's ShipCells from a top left reference
   * @param id String ID of the piece
   */
  public StaticPiece(List<ShipCell> cellList, List<Coordinate> relativeCoords, String id) {
    super(cellList, relativeCoords, NO_MOVEMENT_PATH, id);
  }

  /**
   * Method that returns a copy of the current piece with the same instance variables
   * @return Piece copy that has the same instance variables as the current piece
   */
  @Override
  public Piece copyOf() {
    return new StaticPiece(List.copyOf(getCellList()), List.copyOf(getRelativeCoords()), getID());
  }


}

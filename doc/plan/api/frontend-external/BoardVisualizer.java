public interface BoardVisualizer {

  /**
   * Places a Piece of a certain type at the specified coordinates
   * @param coords Coordinates to place Piece at
   * @param type Type of piece being placed
   */
  public void placePiece(Collection<Coordinate> coords, PieceType type);

  /**
   * Removes any Pieces that are at the coordinates contained in coords.
   * @param coords Coordinates that contain pieces to remove
   */
  public void removePiece(Collection<Coordinate> coords);
}
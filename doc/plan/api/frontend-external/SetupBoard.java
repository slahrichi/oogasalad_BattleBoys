/**
 * This interface represents classes that participate in the first phase of the game, which is when
 * users are able to place their Pieces on their board.
 *
 * @author Eric Xie, Edison Ooi, Minjun Kwak
 */
public interface SetupBoard {

  /**
   * Places a piece on the player's board based on where the user clicked to place it.
   * @param coords The coordinates occupied by this piece
   * @param type The type of Piece that was placed
   */
  void placePiece(List<Coordinate> coords, PieceType type);

  /**
   * Updates the visual indicator of the current Piece the user is placing.
   * @param coords The coordinates occupied by this Piece
   */
  void updateCurrentPiece(List<Coordinate> coords);

  /**
   * Return an error message in a pop-up window detailing the specific error
   * that the user made (ex: Ship placed out of bounds, invalid ship placement, etc.)
   *
   * @param errorMessage A string of the error message to be displayed to the user
   */
  void displayError(String errorMessage);


}
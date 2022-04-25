package oogasalad.view.interfaces;

import java.util.Collection;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

/**
 * This interface presents an API that is used to interface with the View for placing and removing
 * ships during the setup of the game.
 * @author Minjun Kwak, Edison Ooi, Eric Xie
 */
public interface BoardVisualizer {

  /**
   * Places a Piece of a certain type at the specified coordinates
   * @param coords Coordinates to place Piece at
   * @param type Type of piece being placed
   */
  void placePiece(Collection<Coordinate> coords, CellState type);

  /**
   * Removes any Pieces that are at the coordinates contained in coords.
   */
  void removePiece(Collection<Coordinate> coords);
}
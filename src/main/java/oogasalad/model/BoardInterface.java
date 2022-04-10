package oogasalad.model;

import java.util.ArrayList;
import java.util.List;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.enums.CellState;

public interface BoardInterface {

  public boolean placePlace(Coordinate c, CellInterface cell);

  public List<Piece> listPieces();

  public List<Coordinate> listCoordinates();

  public CellState[][] getCurrentBoardState();

  public int getNumPiecesSunk();

  public CellState hit(Coordinate c);

  public boolean canBeStruck(Coordinate c);

  public List<Modifiers> update();

}

package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.tiles.Cell;

public class Board {

  private Map<Coordinate, Cell> boardMap;

  public Board(int rows, int cols) {
    initialize(rows, cols);
  }

  private void initialize(int rows, int cols) {
    boardMap = new HashMap<>();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        boardMap.put(new Coordinate(i, j), null);
      }
    }
  }

  public void place(Coordinate coord, Cell c) {
    boardMap.put(coord, c);
  }

  public Cell checkCell(Coordinate c) {
    return boardMap.get(c);
  }

  public List<Cell> listPieces() {
    return new ArrayList<>(boardMap.values());
  }

  public List<Coordinate> listCoordinates() { return new ArrayList<>(boardMap.keySet()); }
}

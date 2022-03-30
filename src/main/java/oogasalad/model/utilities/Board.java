package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Board {

  private Map<Coordinate, Cell> boardMap;

  public Board() {

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
}

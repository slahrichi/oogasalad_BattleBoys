package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.tiles.Tile;

public class Board {

  private Map<Coordinate, Tile> boardMap;

  public Board() {
    boardMap = new HashMap<>();
  }

  public void place(Coordinate coord, Tile c) {
    boardMap.put(coord, c);
  }

  public Tile checkCell(Coordinate c) {
    return boardMap.get(c);
  }

  public List<Tile> listPieces() {
    return new ArrayList<>(boardMap.values());
  }
}

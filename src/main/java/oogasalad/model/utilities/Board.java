package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

  private Map<Coordinate, Cell> boardMap;
  private Map<String, Integer> maxElementsMap;
  private Map<String, Integer> elementHitMap;

  public Board() {
    boardMap = new HashMap<>();
  }

  public void place(Coordinate coord, Cell c) {
    maxElementsMap.putIfAbsent(c.getType(), 0);
    maxElementsMap.putIfAbsent(c.getType(), maxElementsMap.get(c.getType())+1);
    boardMap.put(coord, c);
  }

  public Cell checkCell(Coordinate c) {
    return boardMap.get(c);
  }

  public int getNumHit(String cellType) {
    return elementHitMap.get(cellType);
  }


  public List<Cell> listPieces() {
    return new ArrayList<>(boardMap.values());
  }
}

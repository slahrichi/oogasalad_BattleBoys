package oogasalad.model.players;

import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.tiles.enums.CellState;

public record PlayerRecord(int myHealth, int myCurrency, Map<CellState,Integer> hitsMap, Map<String, Integer> itemList, Board myBoard) {


}

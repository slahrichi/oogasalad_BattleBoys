package oogasalad.model.players;

import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Item;
import oogasalad.model.utilities.tiles.enums.CellState;

public record PlayerRecord(int myHealth, int myCurrency, List<Item> itemList, Map<CellState, Integer> hitsMap, Board myBoard) {

}

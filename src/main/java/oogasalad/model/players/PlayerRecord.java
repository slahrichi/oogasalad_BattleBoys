package oogasalad.model.players;

import java.util.List;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Item;

public record PlayerRecord(int myHealth, int myCurrency, List<Item> itemList, Board myBoard) {

}

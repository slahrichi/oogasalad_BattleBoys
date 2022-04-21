package oogasalad.model.players;

import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Item;
import oogasalad.model.utilities.usables.Usable;

public record PlayerRecord(int myHealth, int myCurrency, Map<Usable, Integer> itemList, Board myBoard) {


}

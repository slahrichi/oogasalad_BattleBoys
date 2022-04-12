package oogasalad.model.players;

import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.tiles.enums.CellState;

public class EasyDecisionEngine extends DecisionEngine {

  public EasyDecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap) {
    super(coordinateList, enemyMap);
  }


  public Coordinate makeMove() {
    if (getDeque().size() != 0) {
      return getDeque().pollFirst();
    }
    else {
      Coordinate shot = getCoordinateList().get(getRandom().nextInt(getCoordinateList().size()));
      setLastShot(shot);
      return getLastShot();
    }
  }

  public void adjustStrategy(CellState result) {
    if (canBeRemoved(result)) {
      getCoordinateList().remove(getLastShot());
    }
    else {
      getDeque().addFirst(getLastShot());
    }

  }
}

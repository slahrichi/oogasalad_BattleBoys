package oogasalad.model.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.tiles.enums.CellState;

public class EasyDecisionEngine extends DecisionEngine {

  public EasyDecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap) {
    super(coordinateList, enemyMap);
  }

  public EngineRecord makeMove() {
    if (!getDeque().isEmpty()) {
      return getDeque().pollFirst();
    }
    else {
      int id = determineEnemy();
      Coordinate location = determineLocation();
      EngineRecord shot = new EngineRecord(location, id);
      setLastShot(shot);
      return shot;
    }
  }

  private int determineEnemy() {
    List<Integer> enemies = new ArrayList<>(getEnemyMap().keySet());
    return enemies.get(getRandom().nextInt(enemies.size()));
  }

  private Coordinate determineLocation() {
    return getCoordinateList().get(getRandom().nextInt(getCoordinateList().size()));
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

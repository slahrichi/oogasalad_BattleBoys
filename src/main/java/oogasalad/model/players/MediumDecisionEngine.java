package oogasalad.model.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.Piece;


public class MediumDecisionEngine extends DecisionEngine {

  private static final int[] ROW_DELTA = new int[]{-1, 0, 1, 1, -1, -1, 0, 1};
  private static final int[] COL_DELTA = new int[]{-1, 1, 0, 1, 0, 1, -1, -1};

  public MediumDecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap,
      Player player) {
    super(coordinateList, enemyMap, player);
  }

  public EngineRecord makeMove() {
    if (!getDeque().isEmpty()) {
      return getDeque().pollFirst();
    }
    else {
      int id = determineEnemy();
      setCurrentPlayer(id);
      List<Coordinate> list = getCoordinateMap().get(id);
      Coordinate location = determineLocation(list);
      EngineRecord shot = new EngineRecord(location, id);
      setLastShot(shot);
      return shot;
    }
  }

  private int determineEnemy() {
    List<Integer> enemies = new ArrayList<>(getEnemyMap().keySet());
    return enemies.get(getRandom().nextInt(enemies.size()));
  }

  private Coordinate determineLocation(List<Coordinate> list) {
    return list.get(getRandom().nextInt(list.size()));
  }


  public void adjustStrategy(CellState result) {
    if (wasSuccess(result)) {
      getDeque().addFirst(getLastShot());
      prepareBFS();
    }
    if (canBeRemoved(result)) {
      getCoordinateMap().get(getCurrentPlayer()).remove(getLastShot());
    }
  }

  @Override
  public Coordinate placePiece(List<Piece> pieceList) {
    return null;
  }

  private void prepareBFS() {
    for (int i = 0; i < ROW_DELTA.length; i++) {
      Coordinate c = new Coordinate(ROW_DELTA[i], COL_DELTA[i]);
      if (getCoordinateList().contains(c)) {
        getDeque().addLast(new EngineRecord(c, getCurrentPlayer()));
      }
    }
  }

  private boolean wasSuccess(CellState result) {
    return result == CellState.ISLAND_DAMAGED || result == CellState.SHIP_DAMAGED;
  }
}

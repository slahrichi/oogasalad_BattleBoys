package oogasalad.model.players;

import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;

public class HardDecisionEngine extends DecisionEngine {

  public HardDecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap,
  Player player) {
    super(coordinateList, enemyMap, player);
  }

  @Override
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
    return 0;
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

  private void prepareBFS() {
    for (Coordinate c : generateCoordinates()) {
      if (getCoordinateList().contains(c)) {
        getDeque().addLast(new EngineRecord(c, getCurrentPlayer()));
      }
    }
  }

  private boolean wasSuccess(CellState result) {
    return result == CellState.ISLAND_DAMAGED || result == CellState.SHIP_DAMAGED;
  }


  public Coordinate placePiece(List<Piece> pieceList) {
    return null;
  }

  @Override
  public void resetStrategy() {

  }
}

package oogasalad.model.players;

import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.weapons.BasicShot;
import oogasalad.model.utilities.usables.weapons.ClusterShot;

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
      EngineRecord shot = new EngineRecord(location, id, chooseWeapon());
      setLastShot(shot);
      return shot;
    }
  }

  private Usable chooseWeapon() {
    if (getDeque().isEmpty()) {
      return new BasicShot();
    }
    else {
      return new BasicShot();
    }
  }

  private int determineEnemy() {
    return 0;
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
    Coordinate lastShot = getLastShot().shot();
    for (Coordinate c : generateCoordinates()) {
      Coordinate neighbor = new Coordinate(lastShot.getRow() + c.getRow(),
          lastShot.getColumn() + c.getColumn());
      if (getCoordinateList().contains(neighbor)) {
        getDeque().addLast(new EngineRecord(neighbor, getCurrentPlayer(), new BasicShot()));
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

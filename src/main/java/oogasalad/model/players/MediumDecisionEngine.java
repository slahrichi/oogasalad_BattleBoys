package oogasalad.model.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.weapons.BasicShot;

/**
 * An AI for users of medium difficulty to play against. The AI randomly selects both points to
 * attack and enemies to attack. It leverages BFS to scan an area after a successful hit.
 * It also loses track of enemy boats after they move and exclusively uses BasicShots
 *
 * @author Matthew Giglio
 */
public class MediumDecisionEngine extends DecisionEngine {

  /**
   *
   * @param coordinateList list of coordinates from which the AI can choose for each enemy
   * @param enemyMap map relating each enemy to a board of moves the AI has made against them
   * @param player Player to which the DecisionEngine belongs
   */
  public MediumDecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap,
      Player player) {
    super(coordinateList, enemyMap, player);
  }

  /**
   * Decision logic for engine to make move against another player
   * @return EngineRecord containing the AI's selected shot location and enemy ID
   */
  public EngineRecord makeMove() {
    if (!getDeque().isEmpty()) {
      return getDeque().pollFirst();
    }
    else {
      int id = determineEnemy();
      setCurrentPlayer(id);
      List<Coordinate> list = getCoordinateMap().get(id);
      Coordinate location = determineLocation(list);
      EngineRecord shot = new EngineRecord(location, id, new BasicShot());
      setLastShot(shot);
      return shot;
    }
  }


  /**
   * Method to adjust strategy of engine given result of last shot
   * @param result Result of engine's last shot
   */
  public void adjustStrategy(CellState result) {
    if (wasSuccess(result)) {
      getDeque().addFirst(getLastShot());
      prepareBFS();
    }
    if (canBeRemoved(result)) {
      getCoordinateMap().get(getCurrentPlayer()).remove(getLastShot());
      getDeque().remove(getLastShot());
    }
  }

  /**
   * method to reset the strategy in the event that the boats move
   * the AI clears their deque and replenishes all potential coordinates
   */
  public void resetStrategy() {
    getDeque().clear();
    makeCoordinateMap();
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
    return result == CellState.ISLAND_DAMAGED || result == CellState.SHIP_DAMAGED ||
        result == CellState.SHIP_SUNKEN || result == CellState.ISLAND_SUNK;
  }
}

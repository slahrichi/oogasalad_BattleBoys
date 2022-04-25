package oogasalad.model.players;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.weapons.BasicShot;
import oogasalad.model.utilities.usables.weapons.ClusterShot;
import oogasalad.model.utilities.winconditions.WinCondition;


/**
 * An AI for users of hard difficulty to play against. The AI randomly selects both points to
 * attack and enemies to attack. It leverages BFS to scan an area after a successful hit.
 * It also loses track of enemy boats after they move, but exclusively uses Cluster Shots when
 * it has recently made a hit and will shot the whole area with them.
 *
 * @author Matthew Giglio
 */
public class HardDecisionEngine extends DecisionEngine {

  private static final int CLUSTER_SIZE = 3;

  /**
   *
   * @param coordinateList list of coordinates from which the AI can choose for each enemy
   * @param enemyMap map relating each enemy to a board of moves the AI has made against them
   * @param player Player to which the DecisionEngine belongs
   * @param conditionList list of conditions engine needs to consider
   */
  public HardDecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap,
  Player player, List<WinCondition> conditionList) {
    super(coordinateList, enemyMap, player, conditionList);
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
      return new ClusterShot(null, 0, createSquare());
    }
  }

  /**
   * each AI employs a different strategy after the results of a given move
   *
   * @param result result of AI's last move
   */
  public void adjustStrategy(CellState result) {
    if (getWants().contains(result)) {
      getDeque().addFirst(getLastShot());
      prepareBFS();
    }
    if (getAvoids().contains(result) || canBeRemoved(result)) {
      getCoordinateMap().get(getCurrentPlayer()).remove(getLastShot());
      getDeque().remove(getLastShot());
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

  private Map<Coordinate, Integer> createSquare() {
    Map<Coordinate, Integer> map = new HashMap<>();
    for (int i = 0; i < CLUSTER_SIZE; i++) {
      for (int j = 0; j < CLUSTER_SIZE; j++) {
        map.put(new Coordinate(i, j), 1);
      }
    }
    return map;
  }


}

package oogasalad.model.players;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;

public abstract class DecisionEngine {

  private List<Coordinate> myCoordinateList;
  private Deque<Coordinate> myDeque;
  private Map<Integer, MarkerBoard> myEnemyMap;

  public DecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap) {
    myCoordinateList = coordinateList;
    myDeque = new ArrayDeque<>();
    myEnemyMap = enemyMap;
  }

  public abstract Coordinate makeMove();

  protected List<Coordinate> getCoordinateList() {
    return myCoordinateList;
  }

  protected Deque getDeque() {
    return myDeque;
  }

  protected Map<Integer, MarkerBoard> getEnemyMap() {
    return myEnemyMap;
  }


}

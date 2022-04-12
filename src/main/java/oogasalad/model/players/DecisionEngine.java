package oogasalad.model.players;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Random;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.tiles.enums.CellState;

public abstract class DecisionEngine {

  private List<Coordinate> myCoordinateList;
  private Deque<Coordinate> myDeque;
  private Map<Integer, MarkerBoard> myEnemyMap;
  private Random myRandom;
  private Coordinate myLastShot;

  public DecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap) {
    myCoordinateList = coordinateList;
    myDeque = new ArrayDeque<>();
    myEnemyMap = enemyMap;
    myRandom = new Random(System.currentTimeMillis());
  }

  public abstract Coordinate makeMove();

  protected List<Coordinate> getCoordinateList() {
    return myCoordinateList;
  }

  protected Deque<Coordinate> getDeque() {
    return myDeque;
  }

  protected Map<Integer, MarkerBoard> getEnemyMap() {
    return myEnemyMap;
  }

  protected Random getRandom() {
    return myRandom;
  }

  protected Coordinate getLastShot() {
    return myLastShot;
  }

  protected void setLastShot(Coordinate shot) {
    myLastShot = shot;
  }

  protected boolean canBeRemoved(CellState result) {
    return result == CellState.SHIP_SUNKEN || result == CellState.WATER ||
        result == CellState.ISLAND_SUNK || result == CellState.WATER_HIT;
  }

}

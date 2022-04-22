package oogasalad.model.players;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;

public abstract class DecisionEngine {

  private Map<Integer, List<Coordinate>> myCoordinateMap;
  private List<Coordinate> myCoordinateList;
  private Deque<EngineRecord> myDeque;
  private Map<Integer, MarkerBoard> myEnemyMap;
  private Player myPlayer;
  private Random myRandom;
  private EngineRecord myLastShot;
  private int pieceIndex;
  private int currentPlayer;

  private static final int[] ROW_DELTA = new int[]{-1, 0, 1, 1, -1, -1, 0, 1};
  private static final int[] COL_DELTA = new int[]{-1, 1, 0, 1, 0, 1, -1, -1};


  public DecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap,
      Player player) {
    myDeque = new ArrayDeque<>();
    myEnemyMap = enemyMap;
    myCoordinateList = coordinateList;
    myCoordinateMap = makeCoordinateMap(coordinateList);
    myPlayer = player;
    myRandom = new Random(System.currentTimeMillis());
  }

  private Map<Integer, List<Coordinate>> makeCoordinateMap(List<Coordinate> list) {
    Map<Integer, List<Coordinate>> map = new HashMap<>();
    for (Integer id : myEnemyMap.keySet()) {
      map.put(id, new ArrayList<>(List.copyOf(list)));
    }
    return map;
  }

  public abstract EngineRecord makeMove();

  protected List<Coordinate> getCoordinateList() {
    return myCoordinateList;
  }

  protected Map<Integer, List<Coordinate>> getCoordinateMap() {
    return myCoordinateMap;
  }

  protected Deque<EngineRecord> getDeque() {
    return myDeque;
  }

  protected Map<Integer, MarkerBoard> getEnemyMap() {
    return myEnemyMap;
  }

  protected Player getPlayer() {
    return myPlayer;
  }

  protected Random getRandom() {
    return myRandom;
  }

  protected EngineRecord getLastShot() {
    return myLastShot;
  }

  protected void setLastShot(EngineRecord shot) {
    myLastShot = shot;
  }

  protected int getPieceIndex() {
    return pieceIndex;
  }

  protected void updatePieceIndex() {
    pieceIndex += 1;
  }

  protected boolean canBeRemoved(CellState result) {
    return result == CellState.SHIP_SUNKEN || result == CellState.WATER ||
        result == CellState.ISLAND_SUNK || result == CellState.WATER_HIT;
  }

  public abstract void adjustStrategy(CellState result);

  public abstract Coordinate placePiece(List<Piece> pieceList);

  protected int getCurrentPlayer() {
    return currentPlayer;
  }

  protected void setCurrentPlayer(int id) {
    currentPlayer = id;
  }

  public abstract void resetStrategy();

  protected List<Coordinate> generateCoordinates() {
    List<Coordinate> coordinates = new ArrayList<>();
    for (int i = 0; i < ROW_DELTA.length; i++) {
      coordinates.add(new Coordinate(ROW_DELTA[i], COL_DELTA[i]));
    }
    Collections.shuffle(coordinates);
    return coordinates;
  }
}

package oogasalad.model.players;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import oogasalad.model.utilities.Board;
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
    makeCoordinateMap();
    myPlayer = player;
    myRandom = new Random(System.currentTimeMillis());
  }

  protected void makeCoordinateMap() {
    myCoordinateMap = new HashMap<>();
    for (Integer id : myEnemyMap.keySet()) {
      myCoordinateMap.put(id, new ArrayList<>(List.copyOf(myCoordinateList)));
    }
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

  public Coordinate placePiece(List<Piece> pieceList) {
    Board board = getPlayer().getBoard();
    Piece piece = pieceList.get(getPieceIndex());
    Coordinate c = determineLocation(getCoordinateList());
    while (!board.hasValidPlacement(c, piece)) {
      c = determineLocation(getCoordinateList());
    }
    updatePieceIndex();
    return c;
  }

  protected int determineEnemy() {
    List<Integer> enemies = new ArrayList<>(getEnemyMap().keySet());
    return enemies.get(getRandom().nextInt(enemies.size()));
  }

  protected Coordinate determineLocation(List<Coordinate> list) {
    return list.get(getRandom().nextInt(list.size()));
  }

}

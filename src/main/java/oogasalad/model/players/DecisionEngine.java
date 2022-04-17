package oogasalad.model.players;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Random;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;

public abstract class DecisionEngine {

  private List<Coordinate> myCoordinateList;
  private Deque<EngineRecord> myDeque;
  private Map<Integer, MarkerBoard> myEnemyMap;
  private Player myPlayer;
  private Random myRandom;
  private EngineRecord myLastShot;
  private int pieceIndex;

  public DecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap,
      Player player) {
    myCoordinateList = coordinateList;
    myDeque = new ArrayDeque<>();
    myEnemyMap = enemyMap;
    myPlayer = player;
    myRandom = new Random(System.currentTimeMillis());
  }

  public abstract EngineRecord makeMove();

  protected List<Coordinate> getCoordinateList() {
    return myCoordinateList;
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
}

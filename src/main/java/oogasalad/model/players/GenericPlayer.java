package oogasalad.model.players;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.winconditions.WinState;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;


public abstract class GenericPlayer implements Player{

  private int myPiecesLeft;
  private int myCurrency;
  private Map<String, Integer> inventory; //change inventory to use id of usables
  private Map<Integer, MarkerBoard> myEnemyMap;
  private Map<CellState, Integer> myHitsMap; //new instance variable

  private Board myBoard;
  private int myId;

  private static final String PLAYER_PREFIX = "Player ";
  private String myName;

  public GenericPlayer(Board board, int id, Map<Integer, MarkerBoard> enemyMap) {
    myBoard = board;
    inventory = new HashMap<>();
    myCurrency = 0;
    myEnemyMap = enemyMap;
    myHitsMap = new HashMap<CellState, Integer>();
    myId = id;
    myName = PLAYER_PREFIX + (id + 1);
  }

  //changed make purchase to use new ID map
  @Override
  public void makePurchase(int price, String usableID) {
    if (price <= myCurrency) {
      inventory.put(usableID, inventory.getOrDefault(usableID, 0) + 1);
    }
  }

  public void removePiece(String id) {
    myBoard.removePiece(id);
  }

  public void removeAllPieces() {
    myBoard.removeAllPieces();
  }

  @Override
  public boolean placePiece(Piece s, Coordinate coordinate) {
    return myBoard.placePiece(coordinate,s);
  }

  @Override
  public void addGold(int amount) {
    myCurrency += amount;
  }

  @Override
  public void updateEnemyBoard(Coordinate c, int id, CellState state) {
    myEnemyMap.get(id).placeMarker(c, state);
  }

  @Override
  public int getNumPieces() {
    return myPiecesLeft;
  }

  public void determineHealth() {
    myPiecesLeft = 0;
    for (Piece p : myBoard.listPieces()) {
      if (p != null) {
        myPiecesLeft++;
      }
    }
  }

  public void setupBoard(CellState[][] board) {
    myBoard = new Board(board);
  }

  protected List<Coordinate> getValidCoordinates() {
    return myBoard.listCoordinates();
  }

  public WinState applyWinCondition(Function<PlayerRecord, WinState> lambda) {
    return lambda.apply(makeRecord());
  }

  //new method for moving pieces
  public void movePieces() {
    myBoard.moveAllPieces();
  }

  private PlayerRecord makeRecord() {
    return new PlayerRecord(myPiecesLeft, myCurrency, myHitsMap, inventory, myBoard);
  }

  public Board getBoard() {
    return myBoard;
  }

  public int getID() {
    return myId;
  }

  @Override
  public String getName() {
    return myName;
  }

  @Override
  public void setName(String name) {
    myName = name;
  }

  @Override
  public boolean canBeStruck(Coordinate c) {
    return myBoard.canBeStruck(c);
  }

  @Override
  public int getMyCurrency() {
    return myCurrency;
  }

  public Map<Integer, MarkerBoard> getEnemyMap() {
    return myEnemyMap;
  }

  //new Method for updating hits map
  @Override
  public void updateShot(CellState hitResult) {
    myHitsMap.putIfAbsent(hitResult, 0);
    myHitsMap.put(hitResult, myHitsMap.get(hitResult)+1);
  }
}

package oogasalad.model.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Item;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.WinConditions.WinState;
import oogasalad.model.utilities.tiles.enums.CellState;

public abstract class GenericPlayer implements Player{

  private int myPiecesLeft;
  private int myCurrency;
  private List<Item> itemList;
  private Map<Integer, MarkerBoard> myEnemyMap;
  private Board myBoard;
  private int myId;

  private static final String PLAYER_PREFIX = "Player ";
  private String myName;

  public GenericPlayer(Board board, int id, Map<Integer, MarkerBoard> enemyMap) {
    myBoard = board;
    itemList = new ArrayList<>();
    myCurrency = 0;
    myEnemyMap = enemyMap;
    myId = id;
    myName = PLAYER_PREFIX + (id + 1);
  }

  @Override
  public void makePurchase(int amount, Item item) {
    if (item.getPrice() <= myCurrency) {
      myCurrency -= item.getPrice();
      itemList.add(item);
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

  private PlayerRecord makeRecord() {
    return new PlayerRecord(myPiecesLeft, myCurrency, itemList, myBoard);
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
}

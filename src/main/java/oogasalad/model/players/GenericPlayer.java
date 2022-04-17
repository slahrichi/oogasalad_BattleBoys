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
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;

public abstract class GenericPlayer implements Player{

  private int myHealth;
  private int myCurrency;
  private List<Item> itemList;
  private Map<Integer, MarkerBoard> myEnemyMap;
  private Board myBoard;
  private int myId;

  public GenericPlayer(Board board, int id, Map<Integer, MarkerBoard> enemyMap) {
    myBoard = board;
    itemList = new ArrayList<>();
    myCurrency = 0;
    myEnemyMap = enemyMap;
    myId = id;
  }

  @Override
  public void makePurchase(int amount, Item item) {
    if (item.getPrice() <= myCurrency) {
      myCurrency -= item.getPrice();
      itemList.add(item);
    }
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
  public int getHealth() {
    return myHealth;
  }

  public void determineHealth() {
    myHealth = 0;
    for (Piece p : myBoard.listPieces()) {
      if (p != null) {
        myHealth++;
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
    return new PlayerRecord(myHealth, myCurrency, itemList, myBoard);
  }

  public Board getBoard() {
    return myBoard;
  }

  public int getID() {
    return myId;
  }

  @Override
  public boolean canBeStruck(Coordinate c) {
    return myBoard.canBeStruck(c);
  }
  public int getMyCurrency(){return myCurrency;}
  public Map<Integer, MarkerBoard> getEnemyMap() {
    return myEnemyMap;
  }
}

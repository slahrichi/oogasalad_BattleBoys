package oogasalad.model.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Item;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

public abstract class GenericPlayer implements Player{

  private int myHealth;
  private int myCurrency;
  private List<Item> itemList;
  private Map<Integer, Board> myEnemyMap;
  private Board myBoard;
  private int myId;

  public GenericPlayer(Board board, int id, Map<Integer, Board> enemyMap) {
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
  public void strike(Coordinate c) {
    if (myBoard.checkCell(c) != null) {
      myBoard.place(c, null);
      myHealth--;
    }
  }

  @Override
  public int getHealth() {
    return myHealth;
  }

  public void determineHealth() {
    myHealth = 0;
    for (CellInterface c : myBoard.listPieces()) {
      if (c != null) {
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

  public int applyWinCondition(Function<PlayerRecord,Integer> lambda) {
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
}

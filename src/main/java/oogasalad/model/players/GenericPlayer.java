package oogasalad.model.players;

import java.util.ArrayList;
import java.util.List;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Item;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.Cell;

public abstract class GenericPlayer implements Player{

  private int myHealth;
  private int myCurrency;
  private List<Item> itemList;
  private Board myBoard;
  private int id;

  public GenericPlayer(Board board, int id) {
    myBoard = board;
    itemList = new ArrayList<>();
    myCurrency = 0;
    //determineHealth();
  }

  @Override
  public void makePurchase(int amount, Item item) {
    if (item.getPrice() <= myCurrency) {
      myCurrency -= item.getPrice();
      itemList.add(item);
    }
  }

  @Override
  public void placePiece(Piece s) {
    for (Cell c : s.getCellList()) {
      myBoard.place(c.getCoordinates(), c);
    }
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
    for (Cell c : myBoard.listPieces()) {
      if (c != null) {
        myHealth++;
      }
    }
  }

  public void setupBoard(int rows, int cols) {
    myBoard = new Board(rows, cols);
  }

  protected List<Coordinate> getValidCoordinates() {
    return myBoard.listCoordinates();
  }
}

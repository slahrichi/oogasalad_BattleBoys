package oogasalad.model.players;

import java.util.ArrayList;
import java.util.List;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Cell;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Item;
import oogasalad.model.utilities.Piece;

public class HumanPlayer implements Player {

  private int myID;
  private int myHealth;
  private int myCurrency;
  private List<Item> itemList;
  private Board myBoard;

  public HumanPlayer(Board board) {
    myBoard = board;
    itemList = new ArrayList<>();
    myCurrency = 0;
    determineHealth();
  }

  private void determineHealth() {
    for (Cell c : myBoard.listPieces()) {
      if (c != null) {
        myHealth++;
      }
    }
  }

  public int getID() {
    return myID;
  }

  public void playTurn() {

  }

  public void makePurchase(int amount, Item item) {
    if (item.getPrice() <= myCurrency) {
      myCurrency -= item.getPrice();
      itemList.add(item);
    }

  }


  public void placePiece(Piece s) {
    for (Cell c : s.getCellList()) {
      myBoard.place(c.getPosition(), c);
    }
  }

  public void addGold(int amount) {
    myCurrency += amount;
  }

  public void strike(Coordinate c) {
    if (myBoard.checkCell(c) != null) {
      myBoard.place(c, null);
      myHealth--;
    }

  }

  public int getHealth() {
    return myHealth;
  }
}

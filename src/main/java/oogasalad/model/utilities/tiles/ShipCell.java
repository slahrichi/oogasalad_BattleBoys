package oogasalad.model.utilities.tiles;

import java.util.List;
import java.util.function.Function;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;


public class ShipCell implements Cell {

  private Coordinate myCoordinate;
  private int myHealthBar;
  private Piece AssignedPiece;
  private CellState currentState;
  private int myGoldValue;
  private int id;

  public ShipCell(int x, int y, int health, Piece ship, int goldValue){
    myCoordinate = new Coordinate(y,x);
    myHealthBar = health;
    AssignedPiece = ship;
    currentState = CellState.HEALTHY;
    myGoldValue = goldValue;
  }

  public ShipCell(Coordinate c, int id){
    myCoordinate = c;
    this.id = id;
  }

  public ShipCell(int x, int y, Piece ship, int goldValue){
    this(x,y,1, ship,goldValue);
  }

  @Override
  public int hit() {
    myHealthBar --;
    if (myHealthBar == 0) {
      currentState = CellState.SUNKEN;
     // AssignedPiece.registerDamage(this);
      return myGoldValue;
    } else {
      currentState = CellState.DAMAGED;
      return 0;
    }
  }

  @Override
  public List<Function> boardUpdate() {
    return null;
  }

  @Override
  public List<Function> playerUpdate() {
    return null;
  }

  @Override
  public boolean canCarryObject() {
    return false;
  }

  @Override
  public void updateCoordinates(int row, int col) {
    myCoordinate = new Coordinate(row,col);
  }

  @Override
  public Coordinate getCoordinates() {
    return myCoordinate;
  }

  @Override
  public int getCellState() {
    return currentState.ordinal();
  }

  public Piece getAssignedShip(){
    return AssignedPiece;
  }

}

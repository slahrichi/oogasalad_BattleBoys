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

  public ShipCell(Coordinate coord, int health, Piece ship, int id, int goldValue){
    myCoordinate = coord;
    myHealthBar = health;
    AssignedPiece = ship;
    currentState = CellState.HEALTHY;
    myGoldValue = goldValue;
    this.id = id;
  }

  public ShipCell(Coordinate c, int id){
    this(c, 1, null, id, 100);
  }
  public ShipCell(int col, int row, int id){
    this(new Coordinate(row, col), 1, null, id, 100;
  }

  public ShipCell(int row, int col, Piece ship, int id, int goldValue){
    this(new Coordinate(row, col), 1, null, id, 100);
  }

  @Override
  public int hit() {
    myHealthBar --;
    if (myHealthBar == 0) {
      currentState = CellState.SUNKEN;
      //AssignedPiece.registerDamage(this);
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

  public Piece getAssignedShip(){
    return AssignedPiece;
  }

}

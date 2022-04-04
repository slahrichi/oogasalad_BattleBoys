package oogasalad.model.utilities.tiles;

import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;



public class ShipCell implements Cell {

  private Coordinate myCoordinate;
  private int myHealthBar;
  private Piece AssignedPiece;
  private cellStates currentState;
  private int myGoldValue;

  public ShipCell(int x, int y, int health, Piece ship, int goldValue){
    myCoordinate = new Coordinate(y,x);
    myHealthBar = health;
    AssignedPiece = ship;
    currentState = cellStates.HEALTHY;
    myGoldValue = goldValue;
  }

  public ShipCell(Coordinate c, int id){

  }

  public ShipCell(int row, int col, Piece ship, int goldValue){
    this(row, col,1, ship, goldValue);
  }
  @Override
  public int hit() {
    myHealthBar --;
    if (myHealthBar == 0) {
      // deadPart() doesn't exist
//      AssignedPiece.deadPart(this);
      currentState = cellStates.SUNKEN;
      return myGoldValue;
    } else{
      currentState = cellStates.DAMAGED;
      return -1;
    }
  }

  @Override
  public void update() {
    return;
  }

  @Override
  public boolean canCarryObject() {
    return false;
  }


  @Override
  public void updateCoordinates(int x, int y) {
    myCoordinate = new Coordinate(y,x);
  }

  @Override
  public Coordinate getCoordinates() {
    return myCoordinate;
  }

  public Piece getAssignedShip(){
    return AssignedPiece;
  }
}

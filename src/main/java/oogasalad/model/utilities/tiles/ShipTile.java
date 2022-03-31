package oogasalad.model.utilities.tiles;

import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;



public class ShipTile implements Tile {

  private Coordinate myCoordinate;
  private int myHealthBar;
  private Piece AssignedPiece;
  private tileStates currentState;
  private int myGoldValue;

  public ShipTile(int x, int y, int health, Piece ship, int goldValue){
    myCoordinate = new Coordinate(y,x);
    myHealthBar = health;
    AssignedPiece = ship;
    currentState = tileStates.HEALTHY;
    myGoldValue = goldValue;
  }

  public ShipTile(int x, int y, Piece ship, int goldValue){
    this(x,y,1, ship,goldValue);
  }
  @Override
  public int hit() {
    myHealthBar --;
    if (myHealthBar == 0) {
      AssignedPiece.deadPart(this);
      currentState = tileStates.SUNKEN;
      return myGoldValue;
    } else{
      currentState = tileStates.DAMAGED;
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

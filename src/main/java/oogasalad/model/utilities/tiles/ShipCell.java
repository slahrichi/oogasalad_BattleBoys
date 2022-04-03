package oogasalad.model.utilities.tiles;

import java.util.List;
import java.util.function.Function;
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

  public ShipCell(int x, int y, Piece ship, int goldValue){
    this(x,y,1, ship,goldValue);
  }

  public ShipCell(int x, int y){
    this(x,y, 1, null, 0);
  }

  @Override
  public int hit() {
    myHealthBar --;
    if (myHealthBar == 0) {
      currentState = cellStates.SUNKEN;
      return myGoldValue;
    } else{
      currentState = cellStates.DAMAGED;
      return 0;
    }
  }

  @Override
  public List<Function> update() {
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

package oogasalad.model.utilities.tiles;

import java.util.List;
import java.util.function.Function;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;


public class ShipCell implements CellInterface {

  private Coordinate myCoordinate;
  private Coordinate myRelativeCoordinate;
  private int myHealthBar;
  private Piece AssignedPiece;
  private CellState currentState;
  private int myGoldValue;
  private int id;



  public ShipCell(int health, Coordinate relativeCoordinate, int goldValue, String ID) {
    myHealthBar = health;
    myGoldValue = goldValue;
    myRelativeCoordinate = relativeCoordinate;
    currentState = CellState.SHIP_HEALTHY;
  }

  public void placeAt(Coordinate absoluteCoord) {
    myCoordinate = Coordinate.sum(absoluteCoord, myRelativeCoordinate);
  }
  /*
  public ShipCell(Coordinate coord, int health, Piece ship, int id, int goldValue) {
    myCoordinate = coord;
  }
  public ShipCell(int row, int col, int health, Piece ship, int goldValue) {
    myCoordinate = new Coordinate(row, col);

    myHealthBar = health;
    AssignedPiece = ship;
    currentState = CellState.SHIP_HEALTHY;
    myGoldValue = goldValue;
    this.id = id;
  }

  public ShipCell(Coordinate c, int id){
    this(c, 1, null, id, 100);
  }
  public ShipCell(int col, int row, int id){
    this(new Coordinate(row, col), 1, null, id, 100);
  }


  public ShipCell(int row, int col, Piece ship, int id, int goldValue) {
    this(new Coordinate(row, col), 1, null, id, 100);
  }

  public ShipCell(int row, int col, Piece ship, int goldValue){
    this(row, col,1, ship, goldValue);
  }
   */

  @Override
  public int hit() {
    myHealthBar --;
    if (myHealthBar == 0) {
      currentState = CellState.SHIP_SUNKEN;
      //AssignedPiece.registerDamage(this);
      return myGoldValue;
    } else {
      currentState = CellState.SHIP_DAMAGED;
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
    myCoordinate = new Coordinate(row, col);
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

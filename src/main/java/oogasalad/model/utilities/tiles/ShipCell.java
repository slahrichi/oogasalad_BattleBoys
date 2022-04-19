package oogasalad.model.utilities.tiles;

import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.Modifiers.GoldAdder;
import oogasalad.model.utilities.tiles.enums.CellState;


public class ShipCell extends Cell implements CellInterface {

  public static final CellState SHIP_CELL_INTIAL_STATE = CellState.SHIP_HEALTHY;

  //private Coordinate myCoordinate;
  private Coordinate myRelativeCoordinate;
  //private int myHealthBar;
  private transient Piece myShip;
  //private CellState currentState;
  private int myGoldValue;
  private int id;
  //private ArrayList<Modifiers> myModifiers = new ArrayList<>();



// Use this Constructor to create ships in the real game
  public ShipCell(int health, Coordinate relativeCoordinate, int goldValue, Piece ship) {
    super(null, SHIP_CELL_INTIAL_STATE, health);
    //myHealthBar = health;
    myGoldValue = goldValue;
    myRelativeCoordinate = relativeCoordinate;
    //currentState = CellState.SHIP_HEALTHY;
    myShip = ship;
    addModifier(new GoldAdder(myGoldValue));
  }

  public ShipCell(ShipCell parent) {
    super(null,parent.getCellState(),parent.getHealth());
    //myHealthBar = parent.myHealthBar;
    myGoldValue = parent.myGoldValue;
    addModifier(new GoldAdder(myGoldValue));
    myRelativeCoordinate = parent.myRelativeCoordinate;
    //currentState = CellState.SHIP_HEALTHY;
    myShip = parent.myShip;
  }

//Use this only for testing purposes
  public ShipCell(int health, Coordinate relativeCoordinate, int goldValue, String ID) {
    super(null, SHIP_CELL_INTIAL_STATE, health);
    //myHealthBar = health;
    myGoldValue = goldValue;
    myRelativeCoordinate = relativeCoordinate;
    //currentState = CellState.SHIP_HEALTHY;
    addModifier(new GoldAdder(myGoldValue));
  }

  public void placeAt(Coordinate absoluteCoord) {
    setMyCoordinate(Coordinate.sum(absoluteCoord, myRelativeCoordinate));
  }

  @Override
  public CellState hit(int dmg) {
    System.out.println("Health = " + getHealth());
    addToHealthBar(-1*dmg); //change this to whatever ship damage
    System.out.println("Health AFter Hit = " + getHealth());
    if (getHealth() <= 0) {
      setCellState(CellState.SHIP_SUNKEN);
      if(myShip!=null) myShip.registerDamage(this);
    } else {
      setCellState(CellState.SHIP_DAMAGED);
    }
    return getCellState();
  }

  @Override
  public boolean canCarryObject() {
    return false;
  }

  @Override
  public void moveCell(Coordinate nextMovement) {

  }

  public Coordinate getRelativeCoordinate(){
    return myRelativeCoordinate;
  }

  public Piece getAssignedShip(){
    return myShip;
  }

  @Override
  public boolean equals(Object o) {
    if(o == null) return false;
    if(o == this) return true;
    if(!(o instanceof ShipCell)) return false;
    ShipCell other = (ShipCell)o;
    //just need to check myCoordinate, currentState, and myModifiers
    if(!(getCoordinates() == null & other.getCoordinates() == null))
      if(!getCoordinates().equals(other.getCoordinates())) return false;
    if(getCellState() != other.getCellState()) return false;
    if(!getMyModifiers().containsAll(other.getMyModifiers())) return false;
    return true;

  }

}

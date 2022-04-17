package oogasalad.model.utilities.tiles;

import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.Modifiers.GoldAdder;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;


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
  public CellState hit() {
    System.out.println("Health = " + getHealth());
    addToHealthBar(-1); //change this to whatever ship damage
    System.out.println("Health AFter Hit = " + getHealth());
    if (getHealth() <= 0) {
      //currentState = CellState.SHIP_SUNKEN;
      setCellState(CellState.SHIP_SUNKEN);
      if(myShip!=null) myShip.registerDamage(this);
    } else {
      //currentState = CellState.SHIP_DAMAGED;
      setCellState(CellState.SHIP_DAMAGED);
    }
    return getCellState();
  }

  @Override
  public boolean canCarryObject() {
    return false;
  }

  public Coordinate getRelativeCoordinate(){
    return myRelativeCoordinate;
  }

  public Piece getAssignedShip(){
    return myShip;
  }


  /*
  @Override
  public void addModifier(Modifiers myMod){myModifiers.add(myMod);}

  @Override
  public List<Modifiers> update() {
    ArrayList<Modifiers> returnMods = new ArrayList<>();
    for(Modifiers mod: myModifiers){
      if(mod.checkConditions(this)) returnMods.add(mod);
    }
    for(Modifiers currMod: returnMods){
      if(currMod.getClass().getSimpleName().equals("CellModifier")){
        try {
          currMod.modifierFunction().accept(this);
          returnMods.remove(currMod);
        }catch(Exception e){
        }
      }
    }
    return returnMods;
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
  public CellState getCellState() {
    return currentState;
  }

  @Override
  public int getHealth() {
    return myHealthBar;
  }

   */

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

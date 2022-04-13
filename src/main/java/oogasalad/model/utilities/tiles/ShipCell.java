package oogasalad.model.utilities.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.Modifiers.GoldAdder;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.enums.CellState;


public class ShipCell implements CellInterface {

  private Coordinate myCoordinate;
  private Coordinate myRelativeCoordinate;
  private int myHealthBar;
  private transient  Piece myShip;
  private CellState currentState;
  private int myGoldValue;
  private int id;
  private ArrayList<Modifiers> myModifiers = new ArrayList<>();



// Use this Constructor to create ships in the real game
  public ShipCell(int health, Coordinate relativeCoordinate, int goldValue, Piece ship) {
    myHealthBar = health;
    myGoldValue = goldValue;
    myRelativeCoordinate = relativeCoordinate;
    currentState = CellState.SHIP_HEALTHY;
    myShip = ship;
    myModifiers.add(new GoldAdder(myGoldValue));
  }

  public ShipCell(ShipCell parent) {
    myHealthBar = parent.myHealthBar;
    myGoldValue = parent.myGoldValue;
    myModifiers.add(new GoldAdder(myGoldValue));
    myRelativeCoordinate = parent.myRelativeCoordinate;
    currentState = CellState.SHIP_HEALTHY;
    myShip = parent.myShip;
  }
//Use this only for testing purposes
  public ShipCell(int health, Coordinate relativeCoordinate, int goldValue, String ID) {
    myHealthBar = health;
    myGoldValue = goldValue;
    myModifiers.add(new GoldAdder(myGoldValue));
    myRelativeCoordinate = relativeCoordinate;
    currentState = CellState.SHIP_HEALTHY;
  }

  public void placeAt(Coordinate absoluteCoord) {
    myCoordinate = Coordinate.sum(absoluteCoord, myRelativeCoordinate);
  }

  @Override
  public CellState hit() {
    myHealthBar --;
    if (myHealthBar <= 0) {
      currentState = CellState.SHIP_SUNKEN;
      if(myShip!=null) myShip.registerDamage(this);
    } else {
      currentState = CellState.SHIP_DAMAGED;

    }
    return currentState;
  }



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

  public Coordinate getRelativeCoordinate(){
    return myRelativeCoordinate;
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
  public CellState getCellState() {
    return currentState;
  }

  @Override
  public int getHealth() {
    return myHealthBar;
  }

  public Piece getAssignedShip(){
    return myShip;
  }

}

package oogasalad.model.utilities.tiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.enums.CellState;

public abstract class Cell implements CellInterface{
  private Coordinate myCoordinate;
  private ArrayList<Modifiers> myModifiers = new ArrayList<>();
  private CellState currentState;
  private int myHealthBar;

  public Cell(Coordinate coordinate, CellState state, int health) {
    myCoordinate = coordinate;
    currentState = state;
    myHealthBar = health;
  }

  protected void setCellState(CellState newState) {
    currentState = newState;
  }

  protected void addToHealthBar(int addAmt) {
    myHealthBar += addAmt;
  }

  protected void setMyCoordinate(Coordinate newCoordinate) {
    myCoordinate = newCoordinate;
  }

  protected List<Modifiers> getMyModifiers() {
    return Collections.unmodifiableList(myModifiers);
  }

  @Override
  public abstract CellState hit(int dmg);

  @Override
  public List<Modifiers> update() {
    ArrayList<Modifiers> returnMods = new ArrayList<>();
    for(Modifiers mod: myModifiers){
      if(mod.checkConditions(this)) returnMods.add(mod);
    }
    for(Modifiers currMod: returnMods){
      try {
        currMod.modifierFunction(this).accept(this);
      }catch(Exception e){
        }
      }
    return returnMods;
  }

  @Override
  public List<Modifiers> getCellModifiers(){
    return myModifiers;
  }

  @Override
  public abstract boolean canCarryObject();

  //I checked and this method's only used in testing can we remove this method and used protected setter instead?
  @Override
  public void updateCoordinates(int row, int col) {
    myCoordinate = new Coordinate(row, col);
  }

  @Override
  public void moveCoordinate(Coordinate amtToMove) {
    myCoordinate = Coordinate.sum(myCoordinate, amtToMove);
  }

  @Override
  public Coordinate getCoordinates() {
    return myCoordinate;
  }

  @Override
  public void addModifier(Modifiers myMod) {
    myModifiers.add(myMod);
  }

  @Override
  public CellState getCellState() {
    return currentState;
  }

  @Override
  public int getHealth() {
    return myHealthBar;
  }
}

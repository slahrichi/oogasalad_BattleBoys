package oogasalad.model.utilities.tiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Modifiers.GoldAdder;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.enums.CellState;

/**
 * Cell class that represents the individual cells on the baord. Keeps track of cell health, coordinate, and state
 *
 * @Author Brandon Bae, Prajwal Jagadish
 */
public abstract class Cell implements CellInterface{
  private Coordinate myCoordinate;
  private List<Modifiers> myModifiers = new ArrayList<>();
  private List<GoldAdder> goldAdders = new ArrayList<>();
  private CellState currentState;
  private String id;
  private int myHealthBar;

  /**
   * Constructor for cell
   * @param coordinate coordinate to place cell at
   * @param state The initial CellState to represent the cell
   * @param health Amount of hits the cell takes before being destroyed
   */
  public Cell(Coordinate coordinate, CellState state, int health) {
    myCoordinate = coordinate;
    currentState = state;
    myHealthBar = health;
  }

  public Cell(Coordinate coordinate, CellState state, int health, String id) {
    myCoordinate = coordinate;
    currentState = state;
    myHealthBar = health;
    this.id = id;
  }

  protected void setCellState(CellState newState) {
    currentState = newState;
  }

  //adds specified amount to the healthbar
  protected void addToHealthBar(int addAmt) {
    myHealthBar += addAmt;
  }

  //sets the coordinate of the cell to the specified coordinate
  protected void setMyCoordinate(Coordinate newCoordinate) {
    myCoordinate = newCoordinate;
  }

  //returns list of current modifiers
  protected List<Modifiers> getMyModifiers() {
    return Collections.unmodifiableList(myModifiers);
  }

  /**
   * hit the cell for specified amount of damage
   * @param dmg amount of damage to give the cell
   * @return The resulting cell state of the cell after the hit
   */
  @Override
  public abstract CellState hit(int dmg);

  /**
   * Applies any modifiers that have triggered and sends them up the chain
   * @return any modifiers that have triggered that need to be sent up the chain
   */
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

  /**
   * get the modifiers
   * @return myModifiers
   */
  @Override
  public List<Modifiers> getCellModifiers(){
    return myModifiers;
  }

  /**
   * Method determining if the cell can hold another type of piece or cell.
   * @return boolean representing whether a cell can carry another type of piece
   */
  @Override
  public abstract boolean canCarryObject();

  /**
   * updates coordinates to specified parameters
   * @param row row of new coordinate location
   * @param col col of new coordinate location
   */
  @Override
  public void updateCoordinates(int row, int col) {
    myCoordinate = new Coordinate(row, col);
  }

  /**
   * Moves the cell by the specified parameter amount. Used by moving ships
   * @param amtToMove add the amtToMove coordinate to my coordinate to get new cell coordinate
   */
  @Override
  public void moveCoordinate(Coordinate amtToMove) {
    myCoordinate = Coordinate.sum(myCoordinate, amtToMove);
  }

  /**
   * returns myCoordinate
   * @return myCoordiante
   */
  @Override
  public Coordinate getCoordinates() {
    return myCoordinate;
  }

  /**
   * Adds Modifier to modifier list
   * @param myMod modifier to add
   */
  @Override
  public void addModifier(Modifiers myMod) {
    myModifiers.add(myMod);
    if(myMod instanceof GoldAdder)
      goldAdders.add((GoldAdder) myMod);
  }

  /**
   * Getter for current cell state
   * @return currentState
   */
  @Override
  public CellState getCellState() {
    return currentState;
  }

  /**
   * getter for current health
   * @return myHealthBar
   */
  @Override
  public int getHealth() {
    return myHealthBar;
  }

  @Override
  public List<GoldAdder> getGoldModifiers(){
    return goldAdders;
  }

  @Override
  public String getId(){
    return id;
  }
}

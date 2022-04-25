package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.model.utilities.tiles.CellInterface;

/**
 * Purpose - Places a debuff on a cell so that it loses health over time
 * Assumptions - Modifier lambda is passed valid board
 * Parameters - dmg = Damage per turn, count - how many turns it lasts
 * Dependencies - java.util, Modifiers, BoardConsumer, Board, ShipCell,
 * @Author - Prajwal Jagadish
 */
public class Burner extends Modifiers{


  private int count;
  private int dmg;
  public Burner(int dmg, int count){
    this.count = count;
    this.dmg   = dmg;
  }

  /**
   * @param cell - a genertic class that implements a cellInterface
   * @return returns the consumer if a valid instance of a cell is passed in
   */
  @Override
  public Consumer modifierFunction(CellInterface cell){
    return createConsumer();
  }

  /**
   *
   * @param cell - The cell with the modifier on it.
   * @return Everytime checkcondition is called it will decrement the counter until the it is under 0
   * where the modifier won't apply anymore
   */
  public Boolean checkConditions(CellInterface cell){
    count--;
    return count>=0;
  }

  /**
   *
   * @returns a generic  modifier that will hit the cell and will be executed in the cell level of
   * the hierarchy
   */
  @Override
  protected Consumer createConsumer() {
    return new CellConsumer() {
      @Override
      public void accept(CellInterface cellInterface) {
        cellInterface.hit(dmg);
      }
    };
  }

  /**
   *
   * @returns a string describing the modifier
   */
  public String toString(){
    return "Burn " + dmg + " for " + count + " turns";
  };

}
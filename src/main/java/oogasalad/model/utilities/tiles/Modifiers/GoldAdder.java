package oogasalad.model.utilities.tiles.Modifiers;

import java.nio.channels.MulticastChannel;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;

import oogasalad.model.players.Player;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;
/**
 * Purpose - Adds gold in the player that hit the shot
 * Assumptions - Modifier lambda is passed valid players
 * Parameters - gold - how much gold to add into the player inventory
 * Dependencies - java.util, Modifiers, Player, Cell,
 * @Author - Prajwal Jagadish
 */
public class GoldAdder extends Modifiers{

  private int myGold;

  private int multiplier =1;

  public GoldAdder(int gold){
    myGold = gold;
  }

  /**
   * Adds a multiplier to the goldAdder so that different score multipliers can be applied
   * @param factor
   */
  public void setMultiplier(int factor){
    multiplier = factor;
  }

  /**
   *
   * @param players just generic players can be given
   * @returns the valid consumer at the right level in the heirarchy
   */
  @Override
  public Consumer modifierFunction(Player[] players){
    return createConsumer();
  }

  /**
   *
   * @param cell the cell where the modifier exists
   * @return If conditions have been met
   */
  @Override
  public Boolean checkConditions(CellInterface cell) {
    if (allowableStates.contains(cell.getCellState()) && !hasBeenAppliedAlready){
      hasBeenAppliedAlready = true;
      return true;
    }
    return false;
  }

  /**
   *
   * @return a consumer that adds gold to a player
   */
  @Override
  protected Consumer createConsumer() {
    PlayerConsumer consumer = a->{
      a[0].addGold(myGold*multiplier);
    };
    return consumer;
  }

  /**
   *
   * @return info about modifier
   */
  @Override
  public String toString() {
    return "GoldAdder " +myGold;
  }
}

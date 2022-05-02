package oogasalad.model.utilities.tiles.Modifiers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

/**
 * Usage: The abstract class that represents a modifier in the game that changes how the game is played by changing some state.
 * @Author Prajwal Jagadish
 */
public abstract class Modifiers {
  boolean hasBeenAppliedAlready = false;
  Consumer myConsumer;

  protected HashSet<CellState> allowableStates = new HashSet<>(Arrays.asList(
      new CellState[]{CellState.SHIP_SUNKEN, CellState.ISLAND_SUNK}));

  /**
   * The default conditions for when the modifier should be applied
   * @param cell the cell that contains the modifier
   * @return a boolean that states of the conditions have been met and the modifier should be applied
   */
  public Boolean checkConditions(CellInterface cell){
    if ((allowableStates.contains(cell.getCellState()) && !hasBeenAppliedAlready)) {
      hasBeenAppliedAlready = true;
      return true;
    }
    return false;
  }

  /**
   * A protected method that should be overriden by the subclasses to create the consumer that the modifier represents
   * @return
   */
  protected abstract Consumer createConsumer();

  /**
   *
   * @return A String that represent what the modifier is doing
   */
  public abstract String toString();

  /**
   * Creates a blank consumer and passes it back
   * Should be overriden to return the contained consumer if the modifier is trying to fire at the cell level
   * @param cell the cell that the modifier is contained in
   * @returns a consumer that can be applied
   */
  public Consumer modifierFunction(CellInterface cell){
    return (a)->{
      return;
    };
  }


  /**
   * Creates a blank consumer and passes it back
   * Should be overriden to return the contained consumer if the modifier is trying to fire at the board level
   * @param board the board that the modifier is contained in
   * @returns a consumer that can be applied
   */
  public Consumer modifierFunction(Board board){
    return (a)->{
      return;
    };
  }

  /**
   * Creates a blank consumer and passes it back
   * Should be overriden to return the contained consumer if the modifier is trying to affect the players of the game
   * @param players an array of players that represent the player that took the shot and the player that was shot at
   * @returns a consumer that can be applied
   */
  public Consumer modifierFunction(Player[] players){
    return (a)->{
      return;
    };
  }

  /**
   * Creates a blank consumer and passes it back
   * Should be overriden to return the contained consumer if the modifier is trying to fire at the cell level
   * @param gm the gamemanager instance of the current game.
   * @returns a consumer that can be applied
   */
  public Consumer modifierFunction(GameManager gm){
    return (a)->{
      return;
    };
  }

}

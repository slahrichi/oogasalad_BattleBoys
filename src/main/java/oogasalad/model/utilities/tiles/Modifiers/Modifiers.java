package oogasalad.model.utilities.tiles.Modifiers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

public abstract class Modifiers {

  boolean hasBeenAppliedAlready = false;
  Consumer myConsumer;
  protected HashSet<CellState> allowableStates = new HashSet<>(Arrays.asList(
      new CellState[]{CellState.SHIP_SUNKEN, CellState.ISLAND_SUNK}));

  public Boolean checkConditions(CellInterface cell){
    if ((allowableStates.contains(cell.getCellState()) && !hasBeenAppliedAlready)) {
      hasBeenAppliedAlready = true;
      return true;
    }
    return false;
  }

  protected abstract Consumer createConsumer();

  public abstract String toString();

  public Consumer modifierFunction(CellInterface cell){
    return (a)->{
      return;
    };
  }


  public Consumer modifierFunction(Board board){
    return (a)->{
      return;
    };
  }


  public Consumer modifierFunction(Player[] players){
    return (a)->{
      return;
    };
  }

  public Consumer modifierFunction(GameManager gm){
    return (a)->{
      return;
    };
  }


}

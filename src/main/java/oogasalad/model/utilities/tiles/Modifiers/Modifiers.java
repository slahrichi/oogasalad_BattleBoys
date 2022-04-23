package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.tiles.CellInterface;

public abstract class Modifiers {


  Consumer myConsumer;

  public abstract Boolean checkConditions(CellInterface cell);

  public abstract String toString();
  public void setMyConsumer(Consumer mod){ myConsumer = mod;}

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

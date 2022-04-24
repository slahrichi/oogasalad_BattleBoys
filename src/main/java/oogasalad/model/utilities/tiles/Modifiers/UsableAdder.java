package oogasalad.model.utilities.tiles.Modifiers;

import java.util.Arrays;
import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.usables.Usable;

public class UsableAdder extends Modifiers{
  Usable usableToAdd;


  public UsableAdder(Usable usable){
    usableToAdd = usable;
  }

  public UsableAdder(String usableCreator)  {
    String[] fields = usableCreator.split(" ");
    String usableName = fields[0];
    String[] parameters  = Arrays.copyOfRange(fields, 1, fields.length);
    try {
      Class reflectedObj = (Class<Usable>) Class.forName(usableName);
    }catch(Exception e){

    }
    setMyConsumer(new PlayerConsumer() {
      @Override
      public void accept(Player[] players) {
        players[0].addUsableToInventory(usableToAdd);
      }
    });
  }

  @Override
  public Consumer modifierFunction(Player[] players){
    return myConsumer;
  }
  @Override
  public String toString() {
    return "added " + usableToAdd.getMyID() + " to inventory";
  }
}

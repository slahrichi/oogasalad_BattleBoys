package oogasalad.model.utilities.tiles.Modifiers;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.weapons.Weapon;

public class UsableAdder extends Modifiers {

  String usableId;

  public UsableAdder(String id) {
    usableId = id;
  }

  @Override
  protected Consumer createConsumer() {
    Consumer ret = new PlayerConsumer() {
      @Override
      public void accept(Player[] players) {
        players[0].addIDtoInventory(usableId);
      }
    };
    return ret;
  }

  @Override
  public String toString() {
    return "Added " + usableId + " to Inventory";
  }

  @Override
  public Consumer modifierFunction(Player[] players) {
    return myConsumer;
  }
}



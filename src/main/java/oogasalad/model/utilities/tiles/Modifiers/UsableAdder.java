package oogasalad.model.utilities.tiles.Modifiers;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.weapons.Weapon;
/**
 * Purpose - Adds a usable into the players inventory.
 * Assumptions - Modifier lambda is passed valid player
 * Parameters - usableId - The Id of the usable wanting to be Added
 * Dependencies - java.util, Modifiers, player, usable, inventory, shop, parser, GameManager,
 * @Author - Prajwal Jagadish
 */


public class UsableAdder extends Modifiers {

  String usableId;

  public UsableAdder(String id) {
    usableId = id;
  }

  /**
   * Creates consumer that adds an item to the currents players inventory
   * @return Consumer
   */
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

  /**
   * Consumer is only passed back at the player level.
   * @param players a generic player array that way the consumer if fired at the right level
   * @return the consumer
   */
  @Override
  public Consumer modifierFunction(Player[] players) {
    return myConsumer;
  }
}



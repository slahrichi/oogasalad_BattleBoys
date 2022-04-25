package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.tiles.CellInterface;
/**
 * Purpose - Updates the front end view of the class
 * Assumptions - Modifier lambda is passed valid game manager
 * Parameters - count - how many turns it lasts
 * Dependencies - java.util, Modifiers, Gamemanagaer Consumer, CellInterface, Gamemanager,
 * @Author - Prajwal Jagadish
 */
public class FrontEndUpdater extends Modifiers{

  private int count;


  public FrontEndUpdater(int count){
    this.count = count;
  }

  /**
   *
   * @param cell Pass in the current cell where the modifier is attached to
   * @return if there are still usages left
   */
  public Boolean checkConditions(CellInterface cell){
    count--;
    return count>=0;
  }

  @Override
  /**
   * Creates a consumer which tells the gamemanager to refresh the front end view
   */
  protected Consumer createConsumer() {
    return new GameManagerConsumer() {
      @Override
      public void accept(GameManager gameManager) {
        gameManager.getGameViewManager().sendUpdatesToView(gameManager.getIDMap().get(gameManager.getCurrentPlayer()));
      }
    };
  }

  /**
   *
   * @return information about the modifiers
   */
  public String toString(){
    return "Update Front End";
  };

  /**
   *
   * @param players any Player array so that the consumer is passed back at the right level of heirarchy
   * @return the stored consumer
   */
  @Override
  public Consumer modifierFunction(Player[] players){
    return createConsumer();
  }

}

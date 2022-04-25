package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.winconditions.WinCondition;
/**
 * Purpose - Adds a winconditions
 * Assumptions - Modifier lambda is passed valid gamemanager
 * Parameters - The win condition that was added
 * Dependencies - java.util, Modifiers, WinCondition, GameManager,
 * @Author - Prajwal Jagadish
 */
public class NewWinCondition extends Modifiers {
  WinCondition condition;

  public NewWinCondition(WinCondition condition) {
    this.condition = condition;
  }


  @Override
  /**
   * Creates a consumer that adds a wincondition .
   * @return Consumer
   */
  protected Consumer createConsumer() {
    Consumer ret = new GameManagerConsumer() {
      @Override
      public void accept(GameManager gameManager) {
        gameManager.getConditionHandler().addWinCondition(condition);
      }
    };
    return ret;
  }

  @Override
  public String toString() {
    return "Added " + condition.toString() + " as Win Condition";
  }

  /**
   * makes it only pass back the consumer if a gamemanager is passed in
   */
  @Override
  public Consumer modifierFunction(GameManager gm) {
    return createConsumer();
  }
}

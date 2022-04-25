package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.utilities.winconditions.WinCondition;
/**
 * Purpose - Replaces all the winconditions with a new win condition
 * Assumptions - Modifier lambda is passed valid gamemanager
 * Parameters - The win condition that was added
 * Dependencies - java.util, Modifiers, WinCondition, GameManager,
 * @Author - Prajwal Jagadish
 */
public class ReplaceWinCondition extends Modifiers {
  WinCondition condition;

  public ReplaceWinCondition(WinCondition condition) {
    this.condition = condition;
  }


  /**
   * Creates a consumer that replaces the wincondition with another one.
   * @return
   */
  @Override
  protected Consumer createConsumer() {
    Consumer ret = new GameManagerConsumer() {
      @Override
      public void accept(GameManager gameManager) {
        gameManager.getConditionHandler().replaceWinCondition(condition);
      }
    };
    return ret;
  }

  @Override
  public String toString() {
    return "Added " + condition.toString() + " as Win Condition";
  }

  @Override
  /**
   * makes it only pass back the consumer if a gamemanager is passed in
   */
  public Consumer modifierFunction(GameManager gm) {
    return createConsumer();
  }
}

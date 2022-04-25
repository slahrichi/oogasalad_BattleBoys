package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.utilities.winconditions.WinCondition;

public class ReplaceWinCondition extends Modifiers {
  WinCondition condition;

  public ReplaceWinCondition(WinCondition condition) {
    this.condition = condition;
  }


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
  public Consumer modifierFunction(GameManager gm) {
    return createConsumer();
  }
}

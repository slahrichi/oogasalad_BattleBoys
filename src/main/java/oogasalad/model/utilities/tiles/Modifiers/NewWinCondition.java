package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.winconditions.WinCondition;

public class NewWinCondition extends Modifiers {
  WinCondition condition;

  public NewWinCondition(WinCondition condition) {
    this.condition = condition;
  }


  @Override
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

  @Override
  public Consumer modifierFunction(GameManager gm) {
    return createConsumer();
  }
}

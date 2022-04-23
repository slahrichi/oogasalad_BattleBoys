package oogasalad.model.utilities.tiles.Modifiers;

import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.tiles.CellInterface;

public class FrontEndUpdater extends Modifiers{

  private int count;


  public FrontEndUpdater(int count){
    this.count = count;
    setMyConsumer(new GameManagerConsumer() {
      @Override
      public void accept(GameManager gameManager) {
        gameManager.getGameViewManager().sendUpdatesToView(gameManager.getPlayerList().get(gameManager.getCurrentPlayer()));
      }
    });
  }


  public Boolean checkConditions(CellInterface cell){
    count--;
    return count>=0;
  }

  public String toString(){
    return "Update Front End";
  };

  @Override
  public Consumer modifierFunction(Player[] players){
    return myConsumer;
  }

}

package oogasalad.model.utilities.tiles.Modifiers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

public class Mine extends Modifiers{

  private int dmg;



  @Override
  protected Consumer createConsumer() {
    Consumer ret = new PlayerConsumer() {
      @Override
      public void accept(Player[] players) {
        Board myBoard = players[0].getBoard();
        List<Coordinate> aliveShipCells = new ArrayList<>();
        CellState[][] currentState = myBoard.getCurrentBoardState();
        for(int i = 0; i< currentState.length; i++) {
          for (int j = 0; j < currentState[0].length; j++) {
            if (currentState[i][j] == CellState.SHIP_DAMAGED
                || currentState[i][j] == CellState.SHIP_HEALTHY) {
              aliveShipCells.add(new Coordinate(i, j));
            }
          }
        }
        Random rand = new Random();
        myBoard.hit(aliveShipCells.get(rand.nextInt(aliveShipCells.size())), dmg);
      }
    };

    return ret;
  }

  @Override
  public Consumer modifierFunction(GameManager gm){
    return createConsumer();
  }

  @Override
  public String toString() {
    return "A mine was hit and " + dmg +  " damage was done";
  }
}

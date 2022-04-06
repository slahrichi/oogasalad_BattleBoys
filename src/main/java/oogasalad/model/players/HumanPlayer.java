package oogasalad.model.players;

import java.util.Map;
import oogasalad.model.utilities.Board;

public class HumanPlayer extends GenericPlayer {


  public HumanPlayer(Board board, int id, Map<Integer, Board> enemyMap) {
    super(board, id, enemyMap);
  }

  //purposefully empty - this is handled by the view
  @Override
  public void playTurn() {

  }

  @Override
  public void setupBoard(int rows, int cols) {

  }

}

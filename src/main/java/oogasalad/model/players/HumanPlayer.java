package oogasalad.model.players;

import oogasalad.model.utilities.Board;

public class HumanPlayer extends GenericPlayer {


  public HumanPlayer(Board board, int id) {
    super(board, id);
  }

  //purposefully empty - this is handled by the view
  @Override
  public void playTurn() {

  }

  @Override
  public void setupBoard(int rows, int cols) {

  }

}

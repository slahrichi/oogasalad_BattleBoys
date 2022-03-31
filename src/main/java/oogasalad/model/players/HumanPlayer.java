package oogasalad.model.players;

import oogasalad.model.utilities.Board;

public class HumanPlayer extends GenericPlayer {


  public HumanPlayer(Board board) {
    super(board);
  }


  //purposefully empty - this is handled by the view
  @Override
  public void playTurn() {

  }

}

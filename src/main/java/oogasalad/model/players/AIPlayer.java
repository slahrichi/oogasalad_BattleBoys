package oogasalad.model.players;

import java.util.Collection;
import java.util.List;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;

public class AIPlayer extends GenericPlayer{

  private List<Player> players;

  public AIPlayer(Board board, List<Player> players) {
    super(board);
    this.players = players;
  }

  @Override
  public void playTurn() {
    Player enemy = players.get(0);
    Coordinate location = new Coordinate(1,1);
    enemy.strike(location);
  }
}

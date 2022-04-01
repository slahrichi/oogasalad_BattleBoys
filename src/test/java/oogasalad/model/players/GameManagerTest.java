package oogasalad.model.players;

import java.util.ArrayList;
import java.util.Arrays;
import oogasalad.model.GameManager;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTest {

  private Player p1;
  private Player p2;

  @BeforeEach
  void setup() {
    Board b1 = new Board(5, 5);
    Board b2 = new Board(5, 5);
    p1 = new HumanPlayer(b1);
    p2 = new AIPlayer(b2);
  }

  @Test
  void testGameManager() {
    GameManager gm = new GameManager(new ArrayList<>(Arrays.asList(p1, p2)));
    gm.executeMove(1, new Coordinate(0, 0));
    assertEquals(gm.getPlayerList().get(0), p1);
  }

}

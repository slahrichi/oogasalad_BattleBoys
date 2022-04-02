package oogasalad.model.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.model.GameSetup;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameSetupTest {
/*
  private List<Player> playerList;
  private Player p1;
  private Player p2;

  @BeforeEach
  void setup() {
    Board b1 = new Board(5, 5);
    Board b2 = new Board(5, 5);
    p1 = new HumanPlayer(b1);
    p2 = new AIPlayer(b2);
    playerList = new ArrayList<>(Arrays.asList(p1, p2));

  }

  @Test
  void testBasicSetup() {
    Map<Player, List<Piece>> pieceMap = new HashMap<>();
    Piece piece = new StaticPiece(new ArrayList<>(Arrays.asList(new ShipCell(0,
        0, null, 0))));
    pieceMap.put(p1, new ArrayList<>(Arrays.asList(piece)));
    pieceMap.put(p2, new ArrayList<>());
    GameSetup gs = new GameSetup(playerList, pieceMap, 5, 5);
    assertEquals(p1.getHealth(), 1);
    assertEquals(p2.getHealth(), 0);
  }

*/

}

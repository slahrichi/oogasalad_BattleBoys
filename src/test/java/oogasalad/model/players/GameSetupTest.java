package oogasalad.model.players;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.Game;
import oogasalad.PlayerData;
import oogasalad.model.GameSetup;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameSetupTest {
  private PlayerData pd;


  @BeforeEach
  void setup() {
    List<String> playerTypes = new ArrayList<>(Arrays.asList("HumanPlayer", "HumanPlayer"));
    pd = new PlayerData(playerTypes, null, new int[10][10]);

  }

  @Test
  void testBasicSetup() {
    GameSetup gs = new GameSetup(pd);
    assertEquals(gs.getPlayerList().size(), 2);
    assertEquals(gs.getPlayerList().get(0).getClass(), HumanPlayer.class);
  }

}

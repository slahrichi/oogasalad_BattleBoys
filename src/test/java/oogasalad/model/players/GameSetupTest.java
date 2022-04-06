package oogasalad.model.players;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.Game;
import oogasalad.PlayerData;
import oogasalad.model.GameSetup;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
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
    List<Coordinate> coordinateList = new ArrayList<>(Arrays.asList(new Coordinate(0, 0)));
    List<ShipCell> dummyShipCellList = new ArrayList<>();
    dummyShipCellList.add(new ShipCell(1, new Coordinate(0,0), 0, "0"));
    StaticPiece dummyShip = new StaticPiece(dummyShipCellList, coordinateList, "0");
    List<Piece> pieceList = new ArrayList<>();
    pieceList.add(dummyShip);
    pd = new PlayerData(playerTypes, pieceList, new int[10][10]);

  }

  @Test
  void testBasicSetup() {
    GameSetup gs = new GameSetup(pd);
    assertEquals(gs.getPlayerList().size(), 2);
    assertEquals(gs.getPlayerList().get(0).getClass(), HumanPlayer.class);
  }

  @Test
  void testCoordinateChoice() {
    GameSetup gs = new GameSetup(pd);
    gs.propertyChange(new PropertyChangeEvent(null, null, null,
        new Coordinate(0, 0)));
  }

}

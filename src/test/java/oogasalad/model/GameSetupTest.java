package oogasalad.model;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.Game;
import oogasalad.PlayerData;
import oogasalad.model.GameSetup;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.WaterCell;
import oogasalad.view.SetupView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class GameSetupTest extends DukeApplicationTest {
  private PlayerData pd;
  private PlayerData badPd;
  private GameSetup gs;

  @BeforeEach
  void setup() {
    int[][] dummyBoard = new int[][]{{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},
        {1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1}};
    List<String> playerTypes = new ArrayList<>(Arrays.asList("HumanPlayer", "HumanPlayer"));
    List<String> badPlayer = new ArrayList<>(Arrays.asList("HumanPlllayer"));
    List<Coordinate> coordinateList = new ArrayList<>(Arrays.asList(new Coordinate(0, 1),
        new Coordinate(1, 0), new Coordinate(1, 1)));
    List<ShipCell> dummyShipCellList = new ArrayList<>();
    dummyShipCellList.add(new ShipCell(1, new Coordinate(0,1), 0, "0"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    StaticPiece dummyShip = new StaticPiece(dummyShipCellList, coordinateList, "0");
    List<Piece> pieceList = new ArrayList<>();
    pieceList.add(dummyShip);
    pd = new PlayerData(playerTypes, pieceList, dummyBoard);
    badPd = new PlayerData(badPlayer, pieceList, dummyBoard);

  }

  @Test
  void testBasicSetup() {
    javafxRun(() -> gs = new GameSetup(pd));
    assertEquals(gs.getPlayerList().size(), 2);
    assertEquals(gs.getPlayerList().get(0).getClass(), HumanPlayer.class);
  }

  @Test
  void testCoordinateChoice() {
    javafxRun(() -> gs = new GameSetup(pd));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), null, null,
        new Coordinate(0, 0))));
    assertEquals(gs.getPlayerList().get(0).getBoard().
        checkCell(new Coordinate(0, 1)).getClass(), ShipCell.class);
    assertEquals(gs.getPlayerList().get(0).getBoard().
        checkCell(new Coordinate(1, 0)).getClass(), ShipCell.class);
    assertEquals(gs.getPlayerList().get(0).getBoard().
        checkCell(new Coordinate(1, 1)).getClass(), ShipCell.class);
  }

  @Test
  void testInvalidInput() {
    javafxRun(() -> gs = new GameSetup(badPd));
    assertEquals(gs.getPlayerList().get(0), null);
  }

  @Test
  void testInvalidCoordinate() {
    javafxRun(() -> gs = new GameSetup(pd));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), null, null,
        new Coordinate(-1, 0))));
    assertEquals(gs.getPlayerList().get(0).getBoard().
        checkCell(new Coordinate(0, 1)).getClass(), WaterCell.class);
    assertEquals(gs.getPlayerList().get(0).getBoard().
        checkCell(new Coordinate(1, 0)).getClass(), WaterCell.class);
    assertEquals(gs.getPlayerList().get(0).getBoard().
        checkCell(new Coordinate(1, 1)).getClass(), WaterCell.class);
  }

}

package oogasalad.model;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oogasalad.GameData;
import oogasalad.PlayerData;
import oogasalad.controller.GameSetup;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.WaterCell;
import oogasalad.model.utilities.tiles.enums.CellState;
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
    CellState[][] cellBoard = new CellState[dummyBoard.length][dummyBoard[0].length];
    for (int i = 0; i < cellBoard.length; i++) {
      for (int j = 0; j < cellBoard[0].length; j++) {
        cellBoard[i][j] = CellState.of(dummyBoard[i][j]);
      }
    }
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

    pd = new PlayerData(playerTypes, pieceList, cellBoard);
    badPd = new PlayerData(badPlayer, pieceList, cellBoard);

  }

  // Commented out these tests because GameSetup now takes in GameData with the players already
  // constructed with their boards

  @Test
  void testBasicSetup() {
//    javafxRun(() -> gs = new GameSetup(pd));
//    assertEquals(gs.getPlayerList().size(), 2);
//    assertEquals(gs.getPlayerList().get(0).getClass(), HumanPlayer.class);
  }

  @Test
  void testCoordinateChoice() {
//    javafxRun(() -> gs = new GameSetup(pd));
//    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), null, null,
//        new Coordinate(0, 0))));
//    assertEquals(gs.getPlayerList().get(0).getBoard().
//        checkCell(new Coordinate(0, 1)).getClass(), ShipCell.class);
//    assertEquals(gs.getPlayerList().get(0).getBoard().
//        checkCell(new Coordinate(1, 0)).getClass(), ShipCell.class);
//    assertEquals(gs.getPlayerList().get(0).getBoard().
//        checkCell(new Coordinate(1, 1)).getClass(), ShipCell.class);
  }

  @Test
  void testInvalidInput() {
//    javafxRun(() -> gs = new GameSetup(badPd));
//    assertEquals(gs.getPlayerList().get(0), null);
  }

  @Test
  void testInvalidCoordinate() {
//    javafxRun(() -> gs = new GameSetup(pd));
//    assertThrows(NullPointerException.class, () -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), null, null,
//        new Coordinate(-1, 0)))) ;
//    assertEquals(gs.getPlayerList().get(0).getBoard().
//        checkCell(new Coordinate(0, 1)).getClass(), WaterCell.class);
//    assertEquals(gs.getPlayerList().get(0).getBoard().
//        checkCell(new Coordinate(1, 0)).getClass(), WaterCell.class);
//    assertEquals(gs.getPlayerList().get(0).getBoard().
//        checkCell(new Coordinate(1, 1)).getClass(), WaterCell.class);
  }

}

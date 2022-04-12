package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import oogasalad.GameData;
import oogasalad.model.players.AIPlayer;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import util.DukeApplicationTest;

public class GameSetupTest extends DukeApplicationTest {
  private GameData gd;
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
    Map<Integer, MarkerBoard> enemyMap1 = Map.of(1, new MarkerBoard(cellBoard));
    Player p1 = new HumanPlayer(new Board(cellBoard), 0, enemyMap1);
    Map<Integer, MarkerBoard> enemyMap2 = Map.of(0, new MarkerBoard(cellBoard));
    Player p2 = new AIPlayer(new Board(cellBoard), 1, enemyMap2);
    List<Player> playerList = new ArrayList<>(Arrays.asList(p1, p2));
    List<Coordinate> coordinateList = new ArrayList<>(Arrays.asList(new Coordinate(0, 1),
        new Coordinate(1, 0), new Coordinate(1, 1)));
    List<ShipCell> dummyShipCellList = new ArrayList<>();
    dummyShipCellList.add(new ShipCell(1, new Coordinate(0,1), 0, "0"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    StaticPiece dummyShip = new StaticPiece(dummyShipCellList, coordinateList, "0");
    List<Piece> pieceList = new ArrayList<>();
    pieceList.add(dummyShip);
    gd = new GameData(playerList, cellBoard, pieceList, new ArrayList<>());
  }

  // Commented out these tests because GameSetup now takes in GameData with the players already
  // constructed with their boards

  @Test
  void testBasicSetup() {
    javafxRun(() -> gs = new GameSetup(gd));
    assertEquals(gs.getPlayerList().size(), 2);
    assertEquals(gs.getPlayerList().get(0).getClass(), HumanPlayer.class);
    assertEquals(gs.getPlayerList().get(0).getClass(), AIPlayer.class);
  }

  @Test
  void testInvalidMethodName() {
    javafxRun(() -> gs = new GameSetup(gd));
    assertThrows(NullPointerException.class, () -> gs.propertyChange(new PropertyChangeEvent
        (gs.getSetupView(), null, null, new Coordinate(-1, 0)))) ;
  }

  @Test
  void testCoordinateChoice() {
    javafxRun(() -> gs = new GameSetup(gd));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
       new Coordinate(0, 0))));
    assertEquals(gs.getPlayerList().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.SHIP_HEALTHY);
    assertEquals(gs.getPlayerList().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.SHIP_HEALTHY);
    assertEquals(gs.getPlayerList().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.SHIP_HEALTHY);
  }


  @Test
  void testInvalidCoordinate() {
    javafxRun(() -> gs = new GameSetup(gd));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        new Coordinate(-1, 0))));
    assertEquals(gs.getPlayerList().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.WATER);
    assertEquals(gs.getPlayerList().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.WATER);
    assertEquals(gs.getPlayerList().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.WATER);
  }

}

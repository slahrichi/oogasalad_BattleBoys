package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oogasalad.GameData;
import oogasalad.model.players.AIPlayer;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;
import oogasalad.view.SetupView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import util.DukeApplicationTest;

public class GameSetupTest extends DukeApplicationTest {
  private GameData gd1;
  private GameData gd2;
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
    PlayerFactoryRecord pfr = PlayerFactory.initializePlayers(cellBoard, new ArrayList<>(
        Arrays.asList("HumanPlayer", "AIPlayer")), new ArrayList<>(Arrays.asList("None", "Easy")));
    List<Coordinate> coordinateList = new ArrayList<>(Arrays.asList(new Coordinate(0, 1),
        new Coordinate(1, 0), new Coordinate(1, 1)));
    List<ShipCell> dummyShipCellList = new ArrayList<>();
    dummyShipCellList.add(new ShipCell(1, new Coordinate(0,1), 0, "0"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    StaticPiece dummyShip1 = new StaticPiece(dummyShipCellList, coordinateList, "0");
    StaticPiece dummyShip2 = new StaticPiece(dummyShipCellList, coordinateList, "1");
    List<Piece> pieceList1 = new ArrayList<>();
    List<Piece> pieceList2 = new ArrayList<>();
    pieceList1.add(dummyShip1);
    pieceList2.add(dummyShip1);
    pieceList2.add(dummyShip2);
    gd1 = new GameData(pfr.playerList(), cellBoard, pieceList1, new ArrayList<>(), pfr.engineMap());
    gd2 = new GameData(pfr.playerList(), cellBoard, pieceList2, new ArrayList<>(), pfr.engineMap());
  }

  // Commented out these tests because GameSetup now takes in GameData with the players already
  // constructed with their boards

  @Test
  void testBasicSetup() {
    javafxRun(() -> gs = new GameSetup(gd1));
    assertEquals(gd1.players().size(), 2);
    assertEquals(gd1.players().get(0).getClass(), HumanPlayer.class);
    assertEquals(gd1.players().get(1).getClass(), AIPlayer.class);
  }

  @Test
  void testInvalidMethodName() {
    javafxRun(() -> gs = new GameSetup(gd1));
    assertThrows(NullPointerException.class, () -> gs.propertyChange(new PropertyChangeEvent
        (new SetupView(gd1.board()), null, null, new Coordinate(-1, 0)))) ;
  }

  @Test
  void testCoordinateChoice() {
    javafxRun(() -> gs = new GameSetup(gd1));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
       new Coordinate(0, 0))));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.SHIP_HEALTHY);
  }


  @Test
  void testInvalidCoordinate() {
    javafxRun(() -> gs = new GameSetup(gd1));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        new Coordinate(-1, 0))));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.WATER);
  }

  @Test
  void testMultiplePieces() {
    javafxRun(() -> gs = new GameSetup(gd2));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        new Coordinate(0, 0))));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.SHIP_HEALTHY);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        new Coordinate(0, 2))));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][3],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][2],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][3],
        CellState.SHIP_HEALTHY);
  }

  @Test
  void testMoveToGame() {
    javafxRun(() -> gs = new GameSetup(gd1));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        new Coordinate(0, 0))));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "moveToNextPlayer", null,
        null)));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        new Coordinate(0, 0))));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "moveToNextPlayer", null,
        null)));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(1).getBoard().getCurrentBoardState()[0][1],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(1).getBoard().getCurrentBoardState()[1][0],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(1).getBoard().getCurrentBoardState()[1][1],
        CellState.SHIP_HEALTHY);

  }

}

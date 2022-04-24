package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.paint.Color;
import oogasalad.model.players.AIPlayer;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.items.Item;
import oogasalad.model.utilities.usables.weapons.Weapon;
import oogasalad.model.utilities.winconditions.WinCondition;
import oogasalad.view.SetupView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import util.DukeApplicationTest;

/**
 * Comprehensive testing for GameSetup
 *
 * @author Matthew Giglio
 */

public class GameSetupTest extends DukeApplicationTest {
  private GameData gd1;
  private GameData gd2;
  private GameSetup gs;
  private Game game;

  private final ResourceBundle myResources = ResourceBundle.getBundle("/languages/English");

  @BeforeEach
  void setup() {
    CellState[][] cellBoard = new CellState[8][8];
    for (int i = 0; i < cellBoard.length; i++) {
      for (int j = 0; j < cellBoard[0].length; j++) {
        cellBoard[i][j] = CellState.WATER;
      }
    }
    PlayerFactoryRecord pfr = PlayerFactory.initializePlayers(cellBoard, new ArrayList<>(
        Arrays.asList("HumanPlayer", "AIPlayer")), new HashMap<>(), 100, new ArrayList<>(Arrays.asList("None", "Easy")));
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

    gd1 = new GameData(pfr.playerList(), pieceList1, cellBoard, pfr.engineMap(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(), 1, 0, 100);
    gd2 = new GameData(pfr.playerList(), pieceList2, cellBoard, pfr.engineMap(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(), 1, 0, 100);
  }

  // Commented out these tests because GameSetup now takes in GameData with the players already
  // constructed with their boards

  @Test
  void testBasicSetup() throws InterruptedException{
    javafxRun(() -> gs = new GameSetup(gd1, myResources));
    Thread.sleep(2000);
    assertEquals(gd1.players().size(), 2);
    assertEquals(gd1.players().get(0).getClass(), HumanPlayer.class);
    assertEquals(gd1.players().get(1).getClass(), AIPlayer.class);
  }

  @Test
  void testInvalidMethodName() throws InterruptedException{
    javafxRun(() -> gs = new GameSetup(gd1, myResources));
    Thread.sleep(2000);
    assertThrows(NullPointerException.class, () -> gs.propertyChange(new PropertyChangeEvent
        (new SetupView(gd1.board(), myResources), "meow", null, "0 0"))) ;
  }

  @Test
  void testCoordinateChoice() throws InterruptedException{
    javafxRun(() -> gs = new GameSetup(gd1, myResources));
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
       "0 0")));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.SHIP_HEALTHY);
  }


  @Test
  void testInvalidCoordinate() throws InterruptedException {
    javafxRun(() -> gs = new GameSetup(gd1, myResources));
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        "-1 0")));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.WATER);
  }

  @Test
  void testMultiplePieces() throws InterruptedException {
    javafxRun(() -> gs = new GameSetup(gd2, myResources));
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        "0 0")));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.SHIP_HEALTHY);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        "0 2")));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][3],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][2],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][3],
        CellState.SHIP_HEALTHY);
  }

  @Test
  void testMoveToGame() throws InterruptedException {
    javafxRun(() -> {
      gs = new GameSetup(gd1, myResources);
    });
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "moveToNextPlayer", null,
        null)));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.SHIP_HEALTHY);
  }

  @Test
  void testRemovePiece() throws InterruptedException {
    javafxRun(() -> gs = new GameSetup(gd1, myResources));
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "removePiece", null,
        null)));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.WATER);
  }

  @Test
  void testRemoveAllPieces() throws InterruptedException{
    javafxRun(() -> gs = new GameSetup(gd2, myResources));
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        "0 2")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "removeAllPieces", null,
        null)));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][3],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][2],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][3],
        CellState.WATER);
  }

  @Test
  void testRemoveOnePieceOutOfMultiple() throws InterruptedException {
    javafxRun(() -> gs = new GameSetup(gd2, myResources));
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "placePiece", null,
        "0 2")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(), "removePiece", null,
        null)));
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][1],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][0],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][1],
        CellState.SHIP_HEALTHY);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[0][3],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][2],
        CellState.WATER);
    assertEquals(gd1.players().get(0).getBoard().getCurrentBoardState()[1][3],
        CellState.WATER);
  }

}

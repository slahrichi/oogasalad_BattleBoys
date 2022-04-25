package oogasalad.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.paint.Color;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.weapons.BasicShot;
import oogasalad.model.utilities.winconditions.HaveXGoldWinCondition;
import oogasalad.model.utilities.winconditions.LoseXShipsLossCondition;
import oogasalad.model.utilities.winconditions.WinCondition;
import oogasalad.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

/**
 * Comprehensive testing for GameManager
 *
 * @author Matthew Giglio
 */

public class GameManagerTest extends DukeApplicationTest {

  private List<Player> playerList;
  private Map<Player, DecisionEngine> engineMap;
  private CellState[][] cellBoard;
  private ResourceBundle myResources = ResourceBundle.getBundle("/languages/English");
  private List<Piece> pieceList;
  private List<Piece> pieceList2;
  private List<CellState[][]> list;
  private List<WinCondition> wc;
  private GameSetup gs;
  private GameManager gm;
  private String info = "0 1 1";
  private Map<CellState, Color> dummyColorMap;


  @BeforeEach
  void setup() {
    dummyColorMap = new HashMap<>();
    dummyColorMap.put(CellState.NOT_DEFINED, Color.TRANSPARENT);
    dummyColorMap.put(CellState.WATER, Color.BLUE);
    dummyColorMap.put(CellState.WATER_HIT, Color.WHITE);
    dummyColorMap.put(CellState.SHIP_HEALTHY, Color.BLACK);
    dummyColorMap.put(CellState.SHIP_DAMAGED, Color.ORANGE);
    dummyColorMap.put(CellState.SHIP_SUNKEN, Color.RED);
    dummyColorMap.put(CellState.SHIP_HOVER, Color.GRAY);
    dummyColorMap.put(CellState.SCANNED, Color.PINK);
    dummyColorMap.put(CellState.ISLAND_HEALTHY, Color.YELLOW);
    dummyColorMap.put(CellState.ISLAND_DAMAGED, Color.GREEN);
    dummyColorMap.put(CellState.ISLAND_SUNK, Color.PURPLE);
    int[][] board = new int[][]{{1, 1, 1}, {0, 1, 1}};
    cellBoard = new CellState[board.length][board[0].length];
    for (int i = 0; i < cellBoard.length; i++) {
      for (int j = 0; j < cellBoard[0].length; j++) {
        cellBoard[i][j] = CellState.WATER;
      }
    }
    Map<String, Integer> inventory = new HashMap<String, Integer>();
    PlayerFactoryRecord pfr = PlayerFactory.initializePlayers(cellBoard, new ArrayList<>(
        Arrays.asList("HumanPlayer", "HumanPlayer")), inventory, 100,
        new ArrayList<>(Arrays.asList("None", "None")), new ArrayList<>());
    playerList = pfr.playerList();
    engineMap = pfr.engineMap();
    WinCondition c = new LoseXShipsLossCondition(1);
    wc = new ArrayList<>(Arrays.asList(c));
    List<Coordinate> coordinateList = new ArrayList<>(Arrays.asList(new Coordinate(0, 1),
        new Coordinate(1, 0), new Coordinate(1, 1)));
    List<ShipCell> dummyShipCellList = new ArrayList<>();
    List<ShipCell> dummyShipCellList2 = new ArrayList<>();
    dummyShipCellList.add(new ShipCell(1, new Coordinate(0, 1), 0, "0"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0, 1), 0, "0"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1, 0), 0, "1"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1, 1), 0, "2"));
    StaticPiece dummyShip1 = new StaticPiece(dummyShipCellList, coordinateList, "0");
    StaticPiece dummyShip2 = new StaticPiece(dummyShipCellList2, coordinateList, "0");

    pieceList = new ArrayList<>(Arrays.asList(dummyShip1));
    pieceList2 = new ArrayList<>(Arrays.asList(dummyShip2));
    list = new ArrayList<>(Arrays.<CellState[][]>asList(cellBoard));
  }

  @Test
  void testGameManagerBasic() throws InterruptedException {
    GameData gd = new GameData(playerList, pieceList, cellBoard, engineMap, wc, new HashMap<>(),
        new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(),
        1, 0, 100);
    javafxRun(() -> gs = new GameSetup(gd, myResources));
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    javafxRun(() -> {
      gm = new GameManager(gd, myResources);
      gm.createScene();
    });
    assertEquals(gd.engineMap().size(), 0);
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), dummyColorMap, myResources), "handleShot", null, info)));

    assertEquals(gd.players().get(1).getBoard().getCurrentBoardState()[0][1],
        CellState.SHIP_SUNKEN);
    Thread.sleep(2000);
    assertEquals(2, gd.players().size());
  }

  @Test
  void testInvalidInputs() {
    GameData gd = new GameData(playerList, pieceList, cellBoard, engineMap, wc, new HashMap<>(),
        new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(),
        1, 0, 100);
    javafxRun(() -> gm = new GameManager(gd, myResources));
    assertThrows(NullPointerException.class, () -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<>(),
        new HashMap<>(), new ArrayList<>(), dummyColorMap, myResources),
        "invalidMethod", null, info)));
  }

  @Test
  void testMultiplePieces() throws InterruptedException {

    GameData gd = new GameData(playerList, pieceList, cellBoard, engineMap, wc, new HashMap<>(),
        new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(),
        1, 0, 100);
    javafxRun(() -> gs = new GameSetup(gd, myResources));
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    javafxRun(() -> {
      gm = new GameManager(gd, myResources);
      gm.createScene();
    });
    assertEquals(gd.engineMap().size(), 0);
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), dummyColorMap, myResources), "handleShot", null, info)));

    assertEquals(gd.players().get(1).getBoard().getCurrentBoardState()[0][1],
        CellState.SHIP_SUNKEN);
    Thread.sleep(2000);
    assertEquals(gd.players().size(), 2);
  }

  @Test
  void testAI() throws InterruptedException {
    PlayerFactoryRecord pfr = PlayerFactory.initializePlayers(cellBoard, new ArrayList<>(
        Arrays.asList("HumanPlayer", "AIPlayer")), new HashMap<>(), 100,
        new ArrayList<>(Arrays.asList("None", "Easy")), new ArrayList<>(Arrays.asList
            (new LoseXShipsLossCondition(3))));
    GameData gd = new GameData(pfr.playerList(), pieceList2, cellBoard, pfr.engineMap(), wc,
        new HashMap<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
        new HashMap<>(), new ArrayList<>(), 1, 0, 100);
    javafxRun(() -> gs = new GameSetup(gd, myResources));
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    javafxRun(() -> {
      gm = new GameManager(gd, myResources);
      gm.createScene();
    });
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<>(),
        new HashMap<>(), new ArrayList<>(), dummyColorMap, myResources), "handleShot", null, info)));
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<>(),
        new HashMap<>(), new ArrayList<>(), dummyColorMap, myResources), "endTurn", null, info)));
    Thread.sleep(3000);
    assertEquals(wasStruckByAI(gd.players().get(0)), true);
  }

  @Test
  void testWinStateCondition() throws InterruptedException {
    GameData gd = new GameData(playerList, pieceList, cellBoard, engineMap, new ArrayList<>(Arrays.asList(
        new HaveXGoldWinCondition(0))), new HashMap<>(), new ArrayList<>(),
        new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(),
        1, 0, 100);
    javafxRun(() -> gs = new GameSetup(gd, myResources));
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    javafxRun(() -> {
      gm = new GameManager(gd, myResources);
      gm.createScene();
    });
    assertEquals(gd.engineMap().size(), 0);
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), dummyColorMap, myResources), "handleShot", null, info)));
    Thread.sleep(2000);
    assertEquals(gd.players().size(), 2);
  }

  @Test
  void testBuyItem() throws InterruptedException {
    PlayerFactoryRecord pfr = PlayerFactory.initializePlayers(cellBoard, new ArrayList<>(
            Arrays.asList("HumanPlayer", "AIPlayer")), new HashMap<String, Integer>(),
        100, new ArrayList<>(Arrays.asList("None", "Easy")), new ArrayList<>(Arrays.asList
            (new LoseXShipsLossCondition(3))));
    List<Usable> usables = new ArrayList<>(Arrays.asList(new BasicShot()));
    GameData gd = new GameData(playerList, pieceList2, cellBoard, engineMap, new ArrayList<>(Arrays.asList(
        new HaveXGoldWinCondition(0))), new HashMap<>(), new ArrayList<>(),
        new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(
            Arrays.asList(new BasicShot())
    ),
        1, 0, 100);
    javafxRun(() -> gs = new GameSetup(gd, myResources));
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    javafxRun(() -> {
      gm = new GameManager(gd, myResources);
      gm.createScene();
    });
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), dummyColorMap, myResources), "buyItem", null, "Basic Shot")));
    assertEquals(2147483647, pfr.playerList().get(0).getMyInventory().get("Basic Shot"));
  }

  private boolean wasStruckByAI(Player player) {
    Map<Integer, MarkerBoard> enemyMap = player.getEnemyMap();
    MarkerBoard b = enemyMap.get(1);
    CellState[][] board = b.getBoard();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        System.out.println(board[i][j]);
        if (board[i][j] == CellState.WATER_HIT || board[i][j] == CellState.SHIP_SUNKEN) {
          return true;
        }
      }
    }
    return false;
  }


}

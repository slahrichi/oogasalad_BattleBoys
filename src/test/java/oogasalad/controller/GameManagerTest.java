package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.ResourceBundle;
import oogasalad.GameData;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.WinConditions.HaveXGoldWinCondition;
import oogasalad.model.utilities.WinConditions.LoseXShipsLossCondition;
import oogasalad.model.utilities.WinConditions.WinCondition;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.view.GameView;
import oogasalad.view.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

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
  private Info info = new Info(0, 1, 1);


  @BeforeEach
  void setup() {
    int[][] board = new int[][]{{1, 1, 1}, {0, 1, 1}};
    cellBoard = new CellState[board.length][board[0].length];
    for (int i = 0; i < cellBoard.length; i++) {
      for (int j = 0; j < cellBoard[0].length; j++) {
        cellBoard[i][j] = CellState.WATER;
      }
    }
    PlayerFactoryRecord pfr = PlayerFactory.initializePlayers(cellBoard, new ArrayList<>(
        Arrays.asList("HumanPlayer", "HumanPlayer")), new ArrayList<>(Arrays.asList("None", "Easy")));
    playerList = pfr.playerList();
    engineMap = pfr.engineMap();
    WinCondition c = new LoseXShipsLossCondition(1);
    wc = new ArrayList<>(Arrays.asList(c));
    List<Coordinate> coordinateList = new ArrayList<>(Arrays.asList(new Coordinate(0, 1),
        new Coordinate(1, 0), new Coordinate(1, 1)));
    List<ShipCell> dummyShipCellList = new ArrayList<>();
    List<ShipCell> dummyShipCellList2 = new ArrayList<>();
    dummyShipCellList.add(new ShipCell(1, new Coordinate(0,1), 0, "0"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0,1), 0, "0"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    StaticPiece dummyShip1 = new StaticPiece(dummyShipCellList, coordinateList, "0");
    StaticPiece dummyShip2 = new StaticPiece(dummyShipCellList2, coordinateList, "0");

    pieceList = new ArrayList<>(Arrays.asList(dummyShip1));
    pieceList2 = new ArrayList<>(Arrays.asList(dummyShip2));
    list = new ArrayList<>(Arrays.<CellState[][]>asList(cellBoard));
  }

  @Test
  void testGameManagerBasic() throws InterruptedException {
    GameData gd = new GameData(playerList, cellBoard, pieceList, wc, engineMap);
    javafxRun(() -> gs = new GameSetup(gd, myResources));
    writeTo(lookup("#player-name").query(), "Matthew");
    clickOn(lookup("#ok-button").query());
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
        new HashMap<>(), myResources), "handleShot", null, info)));
    assertEquals(gd.players().get(1).getBoard().getCurrentBoardState()[0][1], CellState.SHIP_SUNKEN);
    Thread.sleep(2000);
    assertEquals(gd.players().size(), 1);
  }

  @Test
  void testInvalidInputs() {
    GameData gd = new GameData(playerList, cellBoard, pieceList, wc, engineMap);
    javafxRun(() -> {
      gm = new GameManager(gd, myResources);
      gm.createScene();
    });
    assertThrows(NullPointerException.class, () -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), myResources),
        "invalidMethod", null, info)));
  }

  @Test
  void testMultiplePieces() throws InterruptedException {
    GameData gd = new GameData(playerList, cellBoard, pieceList2, wc, engineMap);
    javafxRun(() -> gs = new GameSetup(gd, myResources));
    writeTo(lookup("#player-name").query(), "Matthew");
    clickOn(lookup("#ok-button").query());
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
        new HashMap<>(), myResources), "handleShot", null, info)));
    assertEquals(gd.players().get(1).getBoard().getCurrentBoardState()[0][1], CellState.SHIP_SUNKEN);
    Thread.sleep(2000);
    assertEquals(gd.players().size(), 2);
  }

  @Test
  void testAI() throws InterruptedException {
    PlayerFactoryRecord pfr = PlayerFactory.initializePlayers(cellBoard, new ArrayList<>(
        Arrays.asList("HumanPlayer", "AIPlayer")), new ArrayList<>(Arrays.asList("None", "Easy")));
    GameData gd = new GameData(pfr.playerList(), cellBoard, pieceList2, wc, pfr.engineMap());
    javafxRun(() -> {
      gs = new GameSetup(gd, myResources);
      gs.createScene();
        }
    );
    writeTo(lookup("#player-name").query(), "Matthew");
    clickOn(lookup("#ok-button").query());
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
        new HashMap<>(), myResources), "handleShot", null, info)));
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), myResources), "endTurn", null, info)));
    Thread.sleep(3000);
    assertEquals(wasStruckByAI(gd.players().get(0)), true);
  }

  @Test
  void testWinStateCondition() throws InterruptedException {
    GameData gd = new GameData(playerList, cellBoard, pieceList, new ArrayList<>(Arrays.asList(
        new HaveXGoldWinCondition(0))), engineMap);
    javafxRun(() -> gs = new GameSetup(gd, myResources));
    writeTo(lookup("#player-name").query(), "Matthew");
    clickOn(lookup("#ok-button").query());
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
        new HashMap<>(), myResources), "handleShot", null, info)));
    Thread.sleep(2000);
    assertEquals(gd.players().size(), 2);
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

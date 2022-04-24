package oogasalad.model.players;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import oogasalad.controller.GameData;
import oogasalad.controller.GameManager;
import oogasalad.controller.GameSetup;
import oogasalad.controller.PlayerFactory;
import oogasalad.controller.PlayerFactoryRecord;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class MediumDecisionEngineTest extends DukeApplicationTest {

  private DecisionEngine engine;
  private CellState[][] cellBoard;
  private Map<Player, DecisionEngine> engineMap;
  private GameSetup gs;
  private List<Piece> pieceList;
  private List<Piece> pieceList2;
  private List<Player> playerList;
  private GameManager gm;
  private List<CellState[][]> list;
  private String info = "0 0 1";

  private final ResourceBundle myResources = ResourceBundle.getBundle("/languages/English");

  @BeforeEach
  void setup() {
    cellBoard = new CellState[1][4];
    for (int i = 0; i < cellBoard.length; i++) {
      for (int j = 0; j < cellBoard[0].length; j++) {
        cellBoard[i][j] = CellState.WATER;
      }
    }
    PlayerFactoryRecord pfr = PlayerFactory.initializePlayers(cellBoard, new ArrayList<>(
        Arrays.asList("HumanPlayer", "AIPlayer")), new HashMap<>(), 100, new ArrayList<>(Arrays.asList("None", "Medium")));
    List<Player> engineList = new ArrayList<>(pfr.engineMap().keySet());
    playerList = pfr.playerList();
    engineMap = pfr.engineMap();
    engine = pfr.engineMap().get(engineList.get(0));
    List<Coordinate> coordinateList = new ArrayList<>(Arrays.asList(new Coordinate(0, 1),
        new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3)));
    List<ShipCell> cellList = new ArrayList<>();
    List<ShipCell> cellList2 = new ArrayList<>();
    cellList.add(new ShipCell(1, new Coordinate(0,0), 0, "0"));
    cellList.add(new ShipCell(1, new Coordinate(0,1), 0, "1"));
    cellList.add(new ShipCell(1, new Coordinate(0,2), 0, "2"));
    cellList.add(new ShipCell(1, new Coordinate(0,3), 0, "3"));
    cellList2.add(new ShipCell(2, new Coordinate(0,0), 0, "0"));
    cellList2.add(new ShipCell(2, new Coordinate(0,1), 0, "1"));
    cellList2.add(new ShipCell(2, new Coordinate(0,2), 0, "2"));
    cellList2.add(new ShipCell(2, new Coordinate(0,3), 0, "3"));

    StaticPiece ship = new StaticPiece(cellList, coordinateList, "0");
    StaticPiece ship2 = new StaticPiece(cellList2, coordinateList, "0");
    pieceList = new ArrayList<>(Arrays.asList(ship));
    pieceList2 = new ArrayList<>(Arrays.asList(ship2));
    list = new ArrayList();
    list.add(cellBoard);
  }

  @Test
  void testPlacePiece() throws InterruptedException {
    GameData gd = new GameData(playerList, pieceList, cellBoard, engineMap, new ArrayList<>(), new HashMap<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(), 1, 0, 100);
    javafxRun(() -> {
          gs = new GameSetup(gd, myResources);
        }
    );
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    Player ai = playerList.get(1);
    CellState[][] board = ai.getBoard().getCurrentBoardState();
    assertEquals(CellState.SHIP_HEALTHY, board[0][1]);
    assertEquals(CellState.SHIP_HEALTHY, board[0][2]);
  }

  @Test
  void testStrategyAdjustmentWithMultipleHealth() throws InterruptedException {
    GameData gd = new GameData(playerList, pieceList2, cellBoard, engineMap, new ArrayList<>(), new HashMap<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(), 1, 0, 100);
    javafxRun(() -> {
          gs = new GameSetup(gd, myResources);
        }
    );
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    Player ai = playerList.get(1);
    CellState[][] board = ai.getBoard().getCurrentBoardState();
    javafxRun(() -> {
      gm = new GameManager(gd, myResources);
      gm.createScene();
    });
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), myResources), "handleShot", null, info)));
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList(), myResources), "endTurn", null, info)));
    Thread.sleep(2000);
    Coordinate c = findCoordinateStruck().get(0);
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList(), myResources), "handleShot", null, info)));
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList(), myResources), "endTurn", null, info)));
    Thread.sleep(2000);
    CellState[][] enemyBoard = playerList.get(0).getBoard().getCurrentBoardState();
    assertEquals(CellState.SHIP_SUNKEN, enemyBoard[c.getRow()][c.getColumn()]);
  }

  @Test
  void testStrategyAdjustmentWithBFS() throws InterruptedException {
    GameData gd = new GameData(playerList, pieceList, cellBoard, engineMap, new ArrayList<>(), new HashMap<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(), 1, 0, 100);
    javafxRun(() -> {
          gs = new GameSetup(gd, myResources);
        }
    );
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    Player ai = playerList.get(1);
    CellState[][] board = ai.getBoard().getCurrentBoardState();
    javafxRun(() -> {
      gm = new GameManager(gd, myResources);
      gm.createScene();
    });
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), myResources), "handleShot", null, info)));
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), myResources), "endTurn", null, info)));
    Thread.sleep(1500);
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), myResources), "handleShot", null, info)));
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), myResources), "endTurn", null, info)));
    CellState[][] enemyBoard = playerList.get(0).getBoard().getCurrentBoardState();
    Thread.sleep(1500);
    List<Coordinate> list = findCoordinateStruck();
    assertEquals(1, Math.max(1, Math.abs(list.get(0).getColumn() - list.get(1).getColumn())));
  }

  private List<Coordinate> findCoordinateStruck() {
    List<Coordinate> list = new ArrayList<>();
    CellState[][] board = playerList.get(0).getBoard().getCurrentBoardState();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        if (board[i][j] == CellState.SHIP_SUNKEN || board[i][j] == CellState.SHIP_DAMAGED) {
          list.add(new Coordinate(i, j));
        }
      }
    }
    return list;
  }

}

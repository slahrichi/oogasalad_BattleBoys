package oogasalad.model.utilities.WinConditions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.apache.logging.log4j.Marker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoseXShipsLossConditionTest {

  WinCondition testCondition;
  Player testPlayer;
  Board testBoard;
  @BeforeEach
  void setup() {
    Piece staticPiece = makeStaticPiece();
    testBoard = makeBoard();
    testPlayer = new HumanPlayer(testBoard, 0, new HashMap<Integer, MarkerBoard>());
    testPlayer.placePiece(staticPiece, new Coordinate(0,0));
    testCondition = new LoseXShipsLossCondition(1);
  }

  private Board makeBoard() {
    int[][] board = new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    CellState[][]cellBoard = new CellState[board.length][board[0].length];
    for (int i = 0; i < cellBoard.length; i++) {
      for (int j = 0; j < cellBoard[0].length; j++) {
        cellBoard[i][j] = CellState.values()[board[i][j]];
      }
    }
    return new Board(cellBoard);
  }

  private Piece makeStaticPiece() {
    List<ShipCell> ship1Cells = new ArrayList<ShipCell>();
    List<Coordinate> ship1RelCoords = new ArrayList<Coordinate>();

    ship1Cells.add(new ShipCell(1, new Coordinate(0, 0), 1, "0"));
    ship1RelCoords.add(new Coordinate(0, 0));

    return new StaticPiece(ship1Cells, ship1RelCoords, "0");
  }
  private Piece makeStaticPiece2() {
    List<ShipCell> ship1Cells = new ArrayList<ShipCell>();
    List<Coordinate> ship1RelCoords = new ArrayList<Coordinate>();

    ship1Cells.add(new ShipCell(1, new Coordinate(0, 0), 1, "0"));
    ship1RelCoords.add(new Coordinate(0, 0));

    return new StaticPiece(ship1Cells, ship1RelCoords, "1");
  }

  @Test
  void testNoneSunkCondition() {
    WinState playerWinState = testCondition.updateWinner(testPlayer);
    assertEquals(playerWinState, WinState.NEUTRAL);
  }

  @Test
  void testBasicSunkCondition() {
    testPlayer.getBoard().hit(new Coordinate(0,0),1);
    WinState playerWinState = testCondition.updateWinner(testPlayer);
    assertEquals(playerWinState, WinState.LOSE);
  }

  @Test
  void multipleSunkCondition() {
    testCondition = new LoseXShipsLossCondition(2);
    testPlayer.placePiece(makeStaticPiece2(), new Coordinate(0,1));
    WinState playerWinState;

    testBoard.hit(new Coordinate(0,0),1);
    playerWinState = testCondition.updateWinner(testPlayer);
    assertEquals(playerWinState, WinState.NEUTRAL);

    testBoard.hit(new Coordinate(0,1),1);
    playerWinState = testCondition.updateWinner(testPlayer);
    assertEquals(playerWinState, WinState.LOSE);
  }
}
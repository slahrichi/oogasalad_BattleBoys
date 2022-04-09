package oogasalad.model.players;

import java.util.ArrayList;
import java.util.List;
import oogasalad.GameData;
import oogasalad.controller.GameManager;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTest {

  private Player p1;
  private Player p2;
  private CellState[][] cellBoard;

  @BeforeEach
  void setup() {
    int[][] board = new int[][]{{1, 1, 1}, {0, 1, 1}};
    cellBoard = new CellState[board.length][board[0].length];
    for (int i = 0; i < cellBoard.length; i++) {
      for (int j = 0; j < cellBoard[0].length; j++) {
        cellBoard[i][j] = CellState.WATER;
      }
    }
    Board b1 = new Board(cellBoard);
    Board b2 = new Board(cellBoard);
    p1 = new HumanPlayer(b1, 0, null);
    p2 = new AIPlayer(b2, 1, null);
  }

  @Test
  void testGameManager() {
    GameManager gm = new GameManager(new GameData(List.of(p1, p2), cellBoard, new ArrayList<Piece>()));
    gm.executeMove(1, new Coordinate(0, 0));
    assertEquals(gm.getPlayerList().get(0), p1);
  }

}

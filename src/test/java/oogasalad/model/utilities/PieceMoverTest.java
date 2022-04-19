package oogasalad.model.utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PieceMoverTest {

  //private Player p1;
  //private Player p2;

  private CellState[][] cellBoard;
  private Board testBoard;
  private Piece staticPiece;
  private Piece movingPiece;

  //private GameManager gm;

  @BeforeEach
  void setup() {
    int[][] board = new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    cellBoard = new CellState[board.length][board[0].length];
    for (int i = 0; i < cellBoard.length; i++) {
      for (int j = 0; j < cellBoard[0].length; j++) {
        cellBoard[i][j] = CellState.values()[board[i][j]];
      }
    }
    testBoard = new Board(cellBoard);
    //Board b2 = new Board(cellBoard);
    //p1 = new HumanPlayer(b1, 0, null);
    //p2 = new HumanPlayer(b2, 1, null);

    //List<Piece> testPieceList = new ArrayList<Piece>();

    staticPiece = makeStaticPiece();
    movingPiece = makeMovingPiece();

    //gm = new GameManager(new GameData(List.of(p1, p2), cellBoard, testPieceList, new ArrayList<WinCondition>()));
  }

  private Piece makeStaticPiece() {
    List<ShipCell> ship1Cells = new ArrayList<ShipCell>();
    List<Coordinate> ship1RelCoords = new ArrayList<Coordinate>();

    ship1Cells.add(new ShipCell(1, new Coordinate(0, 0), 1, "0"));
    ship1RelCoords.add(new Coordinate(0, 0));

    ship1Cells.add(new ShipCell(1, new Coordinate(1, 0), 1, "0"));
    ship1RelCoords.add(new Coordinate(1, 0));

    ship1Cells.add(new ShipCell(1, new Coordinate(2, 0), 1, "0"));
    ship1RelCoords.add(new Coordinate(2, 0));

    return new StaticPiece(ship1Cells, ship1RelCoords, "0");
  }

  private Piece makeMovingPiece() {
    List<ShipCell> ship2Cells = new ArrayList<ShipCell>();
    List<Coordinate> ship2RelCoords = new ArrayList<Coordinate>();
    List<Coordinate> patrolPath = new ArrayList<Coordinate>();
    patrolPath.add(new Coordinate(0,1));

    ship2Cells.add(new ShipCell(1, new Coordinate(0, 0), 1, "1"));
    ship2RelCoords.add(new Coordinate(0, 0));

    return new MovingPiece(ship2Cells, ship2RelCoords,patrolPath, "1");
  }

  @Test
  void basicMovementTest() {
    testBoard.placePiece(new Coordinate(0, 0), movingPiece);
    assertEquals(movingPiece.getCellList().get(0).getCoordinates(), new Coordinate(0,0));
    testBoard.moveAllPieces();
    assertEquals(movingPiece.getCellList().get(0).getCoordinates(), new Coordinate(0,1));
  }

  @Test
  void backwardsPathTest() {
    testBoard.placePiece(new Coordinate(0, 0), movingPiece);
    assertEquals(movingPiece.getCellList().get(0).getCoordinates(), new Coordinate(0,0));
    testBoard.moveAllPieces();
    assertEquals(movingPiece.getCellList().get(0).getCoordinates(), new Coordinate(0,1));
    testBoard.moveAllPieces();
    assertEquals(movingPiece.getCellList().get(0).getCoordinates(), new Coordinate(0,0));
    testBoard.moveAllPieces();
    assertEquals(movingPiece.getCellList().get(0).getCoordinates(), new Coordinate(0,1));

  }

  @Test
  void edgeGridMovementTest() {
    testBoard.placePiece(new Coordinate(0, 2), movingPiece);
    assertEquals(movingPiece.getCellList().get(0).getCoordinates(), new Coordinate(0,2));
    testBoard.moveAllPieces();
    assertEquals(movingPiece.getCellList().get(0).getCoordinates(), new Coordinate(0,2));
    testBoard.moveAllPieces();
    assertEquals(movingPiece.getCellList().get(0).getCoordinates(), new Coordinate(0,1));
  }

  @Test
  void adjacentShipMovementTest() {
    testBoard.placePiece(new Coordinate(0, 1), movingPiece);
    testBoard.placePiece(new Coordinate(0,2), staticPiece);
    testBoard.moveAllPieces();
    assertEquals(movingPiece.getCellList().get(0).getCoordinates(), new Coordinate(0,1));
  }
}

package oogasalad.model.utilities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {

  //private Player p1;
  //private Player p2;

  private CellState[][] cellBoard;
  private Board testBoard;
  private StaticPiece ship1;
  private StaticPiece ship2;

  //private GameManager gm;

  @BeforeEach
  void setup() {
    int[][] board = new int[][]{{1, 1, 1}, {0, 1, 0}, {1, 1, 1}};
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

    ship1 = makeShip1();
    ship2 = makeShip2();

    //gm = new GameManager(new GameData(List.of(p1, p2), cellBoard, testPieceList, new ArrayList<WinCondition>()));
  }

  private StaticPiece makeShip1() {
    List<ShipCell> ship1Cells = new ArrayList<ShipCell>();
    List<Coordinate> ship1RelCoords = new ArrayList<Coordinate>();

    ship1Cells.add(new ShipCell(1,new Coordinate(0,0), 1, "0"));
    ship1RelCoords.add(new Coordinate(0,0));

    ship1Cells.add(new ShipCell(1,new Coordinate(1,0), 1, "0"));
    ship1RelCoords.add(new Coordinate(1, 0));

    ship1Cells.add(new ShipCell(1,new Coordinate(2,0), 1, "0"));
    ship1RelCoords.add(new Coordinate(2,0));

    return new StaticPiece(ship1Cells, ship1RelCoords,"0");
  }

  private StaticPiece makeShip2() {
    List<ShipCell> ship2Cells = new ArrayList<ShipCell>();
    List<Coordinate> ship2RelCoords = new ArrayList<Coordinate>();

    ship2Cells.add(new ShipCell(1,new Coordinate(0,0), 1, "1"));
    ship2RelCoords.add(new Coordinate(0,0));

    return new StaticPiece(ship2Cells, ship2RelCoords,"1");
  }

  @Test
  void successfulPiecePlace() {
    assertTrue(testBoard.placePiece(new Coordinate(0,0), ship2));
    assertTrue(testBoard.placePiece(new Coordinate(0,1), ship1));
    assertEquals(testBoard.listPieces().size(), 2);
  }

  @Test
  void badLocationPlace() {
    assertFalse(testBoard.placePiece(new Coordinate(0,0), ship1));
    assertFalse(testBoard.placePiece(new Coordinate(1,0), ship2));
    assertEquals(testBoard.listPieces().size(), 0);
  }

  @Test
  void shipOverlapPlace() {
    assertTrue(testBoard.placePiece(new Coordinate(1,1), ship2));
    assertEquals(testBoard.listPieces().size(), 1);
    assertFalse(testBoard.placePiece(new Coordinate(0,1), ship1));
    assertEquals(testBoard.listPieces().size(), 1);
  }

  @Test
  void hitShipTest() {
    testBoard.placePiece(new Coordinate(0,1), ship1);
    testBoard.hit(new Coordinate(1,1));
    testBoard.listPieces().get(0).updateShipHP();
    assertEquals(testBoard.listPieces().get(0).getCellList().size(),2);
  }

  @Test
  void sinkShipTest() {
    testBoard.placePiece(new Coordinate(0,1), ship1);
    testBoard.hit(new Coordinate(0,1));
    testBoard.hit(new Coordinate(1,1));
    testBoard.hit(new Coordinate(2,1));
    assertEquals(testBoard.listPieces().size(), 0);
  }
}
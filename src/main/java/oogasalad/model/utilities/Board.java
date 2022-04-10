package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.WaterCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Board {

  private Map<Coordinate, CellInterface> boardMap;
  private CellState[][] myBoardSetup;
  private Map<String, Piece> myPieces;

  private ResourceBundle exceptions;
  private static AtomicInteger nextID = new AtomicInteger();
  private int id;
  //private static final Logger LOG = LogManager.getLogger(Board.class.getName());
  private static final String RESOURCES_PACKAGE = "/";
  private static final String EXCEPTIONS = "BoardExceptions";
  private static final String WRONG_TOP_LEFT = "wrongTopLeft";
  private static final String WRONG_NEIGHBOR = "wrongNeighbor";
  private int myRows;
  private int myCols;
  private int myCurrTurnGold;
  private int myNumShipsSunk;



  public Board(CellState[][] boardSetup) {
    myBoardSetup = boardSetup;
    myRows = boardSetup.length;
    myCols = boardSetup[0].length;
    myPieces = new HashMap<>();
    myCurrTurnGold = 0;
    myNumShipsSunk = 0;
    initialize(boardSetup);
    exceptions = ResourceBundle.getBundle(RESOURCES_PACKAGE+EXCEPTIONS);
  }


  private void initialize(CellState[][] boardSetup) {
    boardMap = new HashMap<Coordinate, CellInterface>();
    for (int i = 0; i < boardSetup.length; i++) {
      for (int j = 0; j < boardSetup[0].length; j++) {
        if(boardSetup[i][j] == CellState.WATER){
          boardMap.put(new Coordinate(i, j), new WaterCell(new Coordinate(i,j)));
        }
      }
    }
  }

  /**
   * @param c Coordinate to check
   * @return whether any Cell can be placed at the given Coordinate
   */

  private boolean canPlaceAt(Coordinate c){
    return boardMap.get(c)!=null && boardMap.get(c).getCellState()==CellState.WATER;
    //return !checkCell(c).equals(null); // this is not correct yet
  }

  /**
   * places a ship on the grid given a topLeft position and coordinates of all Cells making the ship
   * @param topLeft the coordinate of the topLeft Cell of the ship
   * @param piece Piece to place at coordinate topLeft
   */
  public boolean placePiece(Coordinate topLeft, Piece piece){
    if (!hasValidPlacement(topLeft, piece)) {
      return false;
    }
    addPieceCellsToBoard(topLeft, piece);
    return true;
  }

  private void addPieceCellsToBoard(Coordinate topLeft, Piece piece) {
    piece.placeCellsAt(topLeft);
    myPieces.put(piece.getID(), piece); //can make this observer listener in future
    for(ShipCell c: piece.getCellList()) {
      place(c.getCoordinates(), c);
    }
    piece.initializeHPList();
  }

  private boolean hasValidPlacement(Coordinate topLeft, Piece piece) {
    for (Coordinate relative : piece.getRelativeCoords()) {
      if (!canPlaceAt(Coordinate.sum(topLeft, relative))) {
        return false;
      }
    }
    return true;
  }


  public void place(Coordinate c, CellInterface cell) {
    boardMap.put(c, cell);
  }

  public List<CellInterface> listPieces() {
    return new ArrayList<>(boardMap.values());
  }

  public List<Coordinate> listCoordinates() { return new ArrayList<>(boardMap.keySet()); }

  public CellState[][] getCurrentBoardState() {
    CellState[][] currStateArray = new CellState[myRows][myCols];
    for(Coordinate c : boardMap.keySet()) {
      currStateArray[c.getRow()][c.getColumn()] = boardMap.get(c).getCellState();
    }
    for (int i = 0; i < currStateArray.length; i++) {
      for (int j = 0; j < currStateArray[0].length; j++) {
        if (currStateArray[i][j] == null) {
          currStateArray[i][j] = CellState.NOT_DEFINED;
        }
      }
    }
    return currStateArray;
  }

  public CellState hit(Coordinate c) {
    int numStartPieces = myPieces.size();
    CellState hitState = boardMap.get(c).hit();
    myNumShipsSunk = numStartPieces-myPieces.size();
    return hitState;
  }

  public void addPiece(String id, Piece newPiece){
    myPieces.put(id, newPiece);
    return;
  }

  public int getNumPiecesSunk() {
    return myNumShipsSunk;
  }

  public boolean canBeStruck(Coordinate c) {
    return boardMap.get(c).getHealth() != 0;
  }


}

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
  private Map<String, Integer> maxElementsMap;
  private Map<String, Integer> elementHitMap;
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



  public Board(CellState[][] boardSetup) {
    myBoardSetup = boardSetup;
    myRows = boardSetup.length;
    myCols = boardSetup[0].length;
    myPieces = new HashMap<>();
    initialize(boardSetup);
    exceptions = ResourceBundle.getBundle(RESOURCES_PACKAGE+EXCEPTIONS);
  }


  void initialize(CellState[][] boardSetup) {
    boardMap = new HashMap<Coordinate, CellInterface>();
    for (int i = 0; i < boardSetup.length; i++) {
      for (int j = 0; j < boardSetup[0].length; j++) {
        if(boardSetup[i][j] == CellState.WATER){
          boardMap.put(new Coordinate(i, j), new WaterCell(new Coordinate(i,j)));
        }
      }
    }
  }

  /*
  public void putShip(Coordinate topLeft, Collection<Coordinate> relativeCoords){
    try {
      id = nextID.incrementAndGet(); //each new ship (new topLeft) gets a new ID
      place(topLeft, new ShipCell(topLeft, id));
    }
    catch (Exception e) {
      System.out.println(exceptions.getString(WRONG_TOP_LEFT));
      //LOG.error(exceptions.getString(WRONG_TOP_LEFT));
    }
    for (Coordinate neighbor : relativeCoords) {
      Coordinate actualCoords = Coordinate.sum(topLeft, neighbor);
      if (canPlaceAt(actualCoords)){
        place(actualCoords, new ShipCell(actualCoords, id));
      }
      else{
        System.out.println(exceptions.getString(WRONG_NEIGHBOR));
        break;
        //LOG.error(exceptions.getString(WRONG_NEIGHBOR));
      }
    }
  }

   */

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

  //might not be necessary
  public CellInterface checkCell(Coordinate c) {
    return boardMap.get(c);
  }

  public int getNumHit(String cellType) {
    return elementHitMap.get(cellType);
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

  public int hit(Coordinate c) {
   return boardMap.get(c).hit();
  }

  public void addPiece(String id, Piece newPiece){
    myPieces.put(id, newPiece);
    return;
  }

  public Board copyOf() {
    return new Board(myBoardSetup);
  }

  public boolean canBeStruck(Coordinate c) {
    return boardMap.get(c).getHealth() != 0;
  }


}

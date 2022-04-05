package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.WaterCell;
//import org.apache.log4j.Logger;
//import org.apache.log4j.LogManager;

public class Board {

  private Map<Coordinate, CellInterface> boardMap;
  private CellInterface[][] boardArray;
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



  public Board(int[][] boardSetup) {
    myRows = boardSetup.length;
    myCols = boardSetup[0].length;
    initialize(boardSetup);
    exceptions = ResourceBundle.getBundle(RESOURCES_PACKAGE+EXCEPTIONS);
  }


  void initialize(int[][] boardSetup) {
    boardMap = new HashMap<Coordinate, CellInterface>();
    boardArray = new CellInterface[boardSetup.length][boardSetup[0].length];
    for (int i = 0; i < boardSetup.length; i++) {
      for (int j = 0; j < boardSetup[0].length; j++) {
        boardMap.put(new Coordinate(i, j), null); //deprecate map implementation
        boardArray[i][j] = new WaterCell();
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
    return !checkCell(c).equals(null); // this is not correct yet
  }

  /**
   * places a ship on the grid given a topLeft position and coordinates of all Cells making the ship
   * @param topLeft the coordinate of the topLeft Cell of the ship
   * @param ship Piece to place at coordinate topLeft
   */
  public boolean putShip(Coordinate topLeft, Piece ship){
    for (Coordinate relative : ship.getRelativeCoords()) {
      if(!canPlaceAt(Coordinate.sum(topLeft,relative))){
        return false;
      }
    }

    List<ShipCell> shipCells = new ArrayList<ShipCell>();
    for (Coordinate relative : ship.getRelativeCoords()) {
      ShipCell newShipCell = new ShipCell(Coordinate.sum(topLeft,relative));
      place(Coordinate.sum(topLeft,relative), newShipCell);
      shipCells.add(newShipCell);
    }

    ship.updateShipCells(shipCells);
    return true;
  }


  public void place(Coordinate c, CellInterface cell) {
    boardMap.put(c, cell);
  }

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

  public int[][] getCurrentBoardState() {
    int[][] currStateArray = new int[myRows][myCols];
    for(Coordinate c : boardMap.keySet()) {
      currStateArray[c.getRow()][c.getColumn()] = boardMap.get(c).getCellState();
    }
    return getCurrentBoardState();
  }

  public int hit(Coordinate c) {
   return boardMap.get(c).hit();
  }

  public void addPiece(String id, Piece newPiece){
    myPieces.put(id, newPiece);
    return;
  }


}

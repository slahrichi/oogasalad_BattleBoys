package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.ShipCell;
//import org.apache.log4j.Logger;
//import org.apache.log4j.LogManager;

public class Board {

  private Map<Coordinate, Cell> boardMap;
  private Map<String, Integer> maxElementsMap;
  private Map<String, Integer> elementHitMap;

  private ResourceBundle exceptions;
  private static AtomicInteger nextID = new AtomicInteger();
  private int id;
  //private static final Logger LOG = LogManager.getLogger(Board.class.getName());
  private static final String RESOURCES_PACKAGE = "/";
  private static final String EXCEPTIONS = "BoardExceptions";
  private static final String WRONG_TOP_LEFT = "wrongTopLeft";
  private static final String WRONG_NEIGHBOR = "wrongNeighbor";





  public Board(int rows, int cols) {
    initialize(rows, cols);
    exceptions = ResourceBundle.getBundle(RESOURCES_PACKAGE+EXCEPTIONS);
  }

  void initialize(int rows, int cols) {
    boardMap = new HashMap<>();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        boardMap.put(new Coordinate(i, j), null);
      }
    }
  }

  /**
   * places a ship on the grid given a topLeft position and coordinates of all Cells making the ship
   * @param topLeft the coordinate of the topLeft Cell of the ship
   * @param relativeCoords list holding relative Coordinates of the other Cells making the ship
   */
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

  /**
   * @param c Coordinate to check
   * @return whether any Cell can be placed at the given Coordinate
   */
  private boolean canPlaceAt(Coordinate c){
    return !checkCell(c).equals(null); // this is not correct yet
  }

  public void place(Coordinate c, Cell cell) {
    boardMap.put(c, cell);
  }

  public Cell checkCell(Coordinate c) {
    return boardMap.get(c);
  }

  public int getNumHit(String cellType) {
    return elementHitMap.get(cellType);
  }


  public List<Cell> listPieces() {
    return new ArrayList<>(boardMap.values());
  }

  public List<Coordinate> listCoordinates() { return new ArrayList<>(boardMap.keySet()); }
}

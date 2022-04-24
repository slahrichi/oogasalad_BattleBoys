package oogasalad.model.utilities;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.WaterCell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Overall Board class that is the model representation of each player's personal board. This includes information such as all valid cells
 * on the board, all pieces on the board.
 *
 * Assumptions:
 * - Board will be made up of cells
 * - Board only has finite edge conditions
 *
 * Dependencies:
 * - CellInterface: Cells build up the
 *
 * @author Brandon Bae, Prajwal Jagadish, Saad Lahrichi
 */
public class Board {

  private static final Logger LOG = LogManager.getLogger(Board.class);
  private Map<Coordinate, CellInterface> boardMap;
  private Map<String, Piece> myPieces;
  private List<IslandCell> islandsInPlay;
  private ResourceBundle logMessages;

  private static final String RESOURCES_PACKAGE = "/";
  private static final String BACKEND_MESSAGES = "BackendLogMessages";
  private int myRows;
  private int myCols;
  private int myNumShipsSunk;

  public Board(CellState[][] boardSetup) {
    myRows = boardSetup.length;
    myCols = boardSetup[0].length;
    myPieces = new HashMap<>();
    myNumShipsSunk = 0;
    initialize(boardSetup);
    logMessages = ResourceBundle.getBundle(RESOURCES_PACKAGE+BACKEND_MESSAGES);
  }


  private void initialize(CellState[][] boardSetup) {
    boardMap = new HashMap<>();
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
      LOG.info(logMessages.getString("failedPiecePlaceInfo"), topLeft.getRow(), topLeft.getColumn(), piece.getID());
      return false;
    }
    addPieceCellsToBoard(topLeft, piece);
    return true;
  }

  private void addPieceCellsToBoard(Coordinate topLeft, Piece piece) {
    piece.placeCellsAt(topLeft);
    myPieces.put(piece.getID(), piece); //can make this observer listener in future
    LOG.info(String.format(logMessages.getString("successPiecePlaceInfo"), piece.getID(), topLeft.getRow(), topLeft.getColumn()));
    for(ShipCell c: piece.getCellList()) {
      boardMap.put(c.getCoordinates(),c);
    }
    piece.initializeHPList();
  }

  public boolean hasValidPlacement(Coordinate topLeft, Piece piece) {
    for (Coordinate relative : piece.getRelativeCoords()) {
      if (!canPlaceAt(Coordinate.sum(topLeft, relative))) {
        return false;
      }
    }
    return true;
  }

  public List<Piece> listPieces() {
    return new ArrayList<>(myPieces.values());
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

  public CellState hit(Coordinate c, int dmg) {
    int numStartPieces = myPieces.keySet().size();
    CellState hitState = boardMap.get(c).hit(dmg);

    Set<String> originalKeySet = new HashSet<>(myPieces.keySet());
    for(String key: originalKeySet) {
      Piece currPiece = myPieces.get(key);
      currPiece.updateShipHP();
      if(currPiece.checkDeath()) {
        LOG.info(String.format(logMessages.getString("pieceSunkInfo"), currPiece.getID()));
        myPieces.remove(key);
      }
    }

    myNumShipsSunk += numStartPieces-myPieces.keySet().size();
    return hitState;
  }

  public Piece getPiece(int id){
    Piece myPiece = myPieces.get(id);
    return myPiece;
  }
  public int getNumPiecesSunk() {
    return myNumShipsSunk;
  }

  public boolean checkBoundedCoordinate(Coordinate coord){return boardMap.containsKey(coord);}

  public boolean canBeStruck(Coordinate c) {
    return boardMap.get(c).getHealth() != 0;
  }

  public int[] getSize(){return new int[]{myRows, myCols};}

  public CellInterface getCell(Coordinate c){
    return boardMap.get(c);
  }



  public void randomizeIslands(List<IslandCell> islands){
    islandsInPlay = islands;
    List<Coordinate> freeCells = new ArrayList<>();
    for(Coordinate c: boardMap.keySet()){
      if(boardMap.get(c).canCarryObject()){
        freeCells.add(c);
      }
    }
    Random rand = new Random();
    for(IslandCell island: islandsInPlay){
       int index = rand.nextInt(freeCells.size());
       island.updateCoordinates(freeCells.get(index).getRow(), freeCells.get(index).getColumn());
       boardMap.replace(freeCells.get(index), island);
       freeCells.remove(index);
    }
  }


  public boolean setIslandsInPlay(List<IslandCell> islands){
    if(islandsInPlay == null) {
      return false;
    }
    else{
      this.islandsInPlay = islands;
      for(IslandCell island: islands){
        boardMap.replace(island.getCoordinates(), island);
      }
      return true;
    }
  }

  public boolean addIsland(Coordinate c, IslandCell island){
    if(boardMap.containsKey(c)&& boardMap.get(c).canCarryObject()){
      boardMap.replace(c, island);
      island.updateCoordinates(c.getRow(), c.getColumn());
      islandsInPlay.add(island);
      return true;
    }
    return false;
  }

  public List<Modifiers> update() {
    ArrayList<Modifiers> retModifers = new ArrayList<>();
    for(CellInterface cell: boardMap.values()){
      List<Modifiers> cellMods = cell.update();
      for(Modifiers mod: cellMods){
        mod.modifierFunction(this).accept(this);
      }
      retModifers.addAll(cellMods);
    }
    return retModifers;
  }

  public void removePiece(String ID) {
    Piece pieceToRemove = myPieces.get(ID);
    myPieces.remove(ID);
    pieceToRemove.removeFromBoard(boardMap);
  }

  public void removeAllPieces() {
    Iterator<String> itr = myPieces.keySet().iterator();
    while (itr.hasNext()) {
      myPieces.get(itr.next()).removeFromBoard(boardMap);
      itr.remove();
    }
  }

  public void moveAllPieces() {
    for(String key: myPieces.keySet()) {
      myPieces.get(key).movePiece(boardMap);
    }

  }
}

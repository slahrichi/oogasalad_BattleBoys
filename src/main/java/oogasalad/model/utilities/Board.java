package oogasalad.model.utilities;


import java.util.*;

import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.tiles.Modifiers.GoldAdder;
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
 * - CellInterface: The board is built up by a Map of coordinates to cells, Is an abstraction of what occupies that part of that board and its state
 * - Pieces: Player place pieces onto a board
 * - Modifiers: Modifiers can have effects that change board parameters when a cell with a modifier is destroyed
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

  /**
   * Constructor for board. Translates a 2d array of initial cellstates to the Map<Coordinate, CellInterface> that the board uses
   * to represent its cell's locations.
   *
   * Assumptions:
   * - boardSetup parameter will have only NOT_DEFINED or WATER cell states as when a board is created nothing is on the board
   *
   * @param boardSetup a 2d array of initial cellstates used to build the available cells on the board
   */
  public Board(CellState[][] boardSetup) {
    myRows = boardSetup.length;
    myCols = boardSetup[0].length;
    myPieces = new HashMap<>();
    myNumShipsSunk = 0;
    initialize(boardSetup);
    logMessages = ResourceBundle.getBundle(RESOURCES_PACKAGE+BACKEND_MESSAGES);
  }

  //helper method that helps translate the 2d array of cellstates to the Map representation the Board uses
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

  //helper method that helps determine if a cell can be placed at the given coordinate. Checks if the cell at the coordinate
  //and if it can carry an object
  private boolean canPlaceAt(Coordinate c){
    return boardMap.get(c)!=null && boardMap.get(c).canCarryObject();
  }

  /**
   * Checks and then places a ship on the grid given an absolute coordinate and relative coordinates of all Cells
   * making the ship
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

  //helper method for placePiece public method that places the given pieces cell based off the given absolute coordinate
  private void addPieceCellsToBoard(Coordinate topLeft, Piece piece) {
    piece.placeCellsAt(topLeft);
    myPieces.put(piece.getID(), piece); //can make this observer listener in future
    LOG.info(String.format(logMessages.getString("successPiecePlaceInfo"), piece.getID(), topLeft.getRow(), topLeft.getColumn()));
    for(ShipCell c: piece.getCellList()) {
      boardMap.put(c.getCoordinates(),c);
    }
    //piece.initializeHPList();
  }

  /**
   * Method that checks if a piece can be placed at the given absolute coordinate
   * @param topLeft absolute coordinate representing the top left corner of the piece
   * @param piece the piece to place
   * @return boolean representing if the piece can be placed at the given coordinate
   */
  public boolean hasValidPlacement(Coordinate topLeft, Piece piece) {
    for (Coordinate relative : piece.getRelativeCoords()) {
      if (!canPlaceAt(Coordinate.sum(topLeft, relative))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Method that returns all pieces on the board
   * Safe getter method as we return a copy of myPieces.values as an ArrayList meaning that the myPieces map cannot
   * be altered or changed by other classes that may use this method.
   * @return List copy of all pieces in the pieces map
   */
  public List<Piece> listPieces() {
    return new ArrayList<>(myPieces.values());
  }

  /**
   * Method that returns all valid coordinates on the board
   * Safe getter method as we return a copy of boardMap.keySet() as an ArrayList meaning that the boardMap cannot
   * be altered or changed by other classes that may use this method.
   * @return List copy of all valid coordinates on the board
   */
  public List<Coordinate> listCoordinates() { return new ArrayList<>(boardMap.keySet()); }

  /**
   * Translates and returns the boardMap to a 2d array of CellStates. This is used by the frontend where it is easier to
   * convert an array to a visual representation of the board
   * @return 2d array of cellstates representative of the current board.
   */
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

  /**
   * Method that hits the cell at the given coordinate
   *
   * Assumption:
   * - given coordinate is a valid board coordinate
   *
   * @param c coordinate of the cell to hit
   * @param dmg the amount of damage the hit should do
   * @return resulting CellState of the cell after it is hit
   */
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

  /**
   * Returns the piece with the given id
   * @param id ID of piece to get
   * @return Piece with the given ID
   */
  public Piece getPiece(int id){
    Piece myPiece = myPieces.get(id);
    return myPiece;
  }

  /**
   * Returns the number of pieces sunk so far
   * myNumShipsSunk is calculated in the hit method
   * @return num pieces sunk so far
   */
  public int getNumPiecesSunk() {
    return myNumShipsSunk;
  }

  /**
   * Method that checks if the given coordinate is a valid coordinate on the board
   * @param coord coordinate to check
   * @return boolean representing if the coordinate is valid on the board
   */
  public boolean checkBoundedCoordinate(Coordinate coord){return boardMap.containsKey(coord);}

  /**
   * Method that checks if the cell at the coordinate has hp =
   * @param c coordinate of cell to check
   * @return boolean representing if the cell has HP or not
   */
  public boolean canBeStruck(Coordinate c) {
    return boardMap.get(c).getHealth() != 0;
  }

  /**
   * gets the width and height of the board
   * @return array of format: [row, col] of board
   */
  public int[] getSize(){return new int[]{myRows, myCols};}

  /**
   * gets cell at given coordinate
   * @param c coordinate of cell to get
   * @return cell at given coordinate
   */
  public CellInterface getCell(Coordinate c){
    return boardMap.get(c);
  }

  /**
   * Randomly Places islands on the board
   * @param islands islands to place
   */
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

  /**
   * sets the islands on the board
   * @param islands islands to set the board to
   * @return  boolean representing if islands are enabled for this board
   */
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

  /**
   * Checks and then places an island at a given coordinate
   * @param c coordinate to place island at
   * @param island island to place
   * @return boolean representing if island was able to placed or not
   */
  public boolean addIsland(Coordinate c, IslandCell island){
    if(boardMap.containsKey(c)&& boardMap.get(c).canCarryObject()){
      boardMap.replace(c, island);
      island.updateCoordinates(c.getRow(), c.getColumn());
      islandsInPlay.add(island);
      return true;
    }
    return false;
  }

  /**
   * Method that checks if any cells on the board have any modifiers to pass up the hierarchy. Any active modifiers
   * are then applied to the board
   * @return active modifiers to be applied higher up in the hierarchy (game manager).
   */
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

  /**
   * Remove a specified piece from the board
   * Assumption:
   * - piece is placed on the board: Used only in the setup stage after a ship has been placed
   * @param ID ID of ship to remove from the board
   */
  public void removePiece(String ID) {
    Piece pieceToRemove = myPieces.get(ID);
    myPieces.remove(ID);
    pieceToRemove.removeFromBoard(boardMap);
  }

  /**
   * Removes all placed pieces on the board
   */
  public void removeAllPieces() {
    Iterator<String> itr = myPieces.keySet().iterator();
    while (itr.hasNext()) {
      myPieces.get(itr.next()).removeFromBoard(boardMap);
      itr.remove();
    }
  }

  /**
   * Makes all pieces move based off their predefined movement
   */
  public void moveAllPieces() {
    for(String key: myPieces.keySet()) {
      myPieces.get(key).movePiece(boardMap);
    }

  }

  public void applyMultiplier(int multiplier) {
    for(Coordinate coord: boardMap.keySet()){
      for(GoldAdder adder: boardMap.get(coord).getGoldModifiers())
        adder.setMultiplier(multiplier);
    }
  }
}

package oogasalad.model.players;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import oogasalad.controller.GameManager;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.winconditions.WinState;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public abstract class GenericPlayer implements Player{

  private static final Logger LOG = LogManager.getLogger(GameManager.class);
  private int myPiecesLeft;
  private int myCurrency;
  private Map<String, Double> myInventory;
  private Map<Integer, MarkerBoard> myEnemyMap;
  private Map<CellState, Integer> myHitsMap; //new instance variable

  private Board myBoard;
  private int myId;
  private static final String PLAYER_PREFIX = "Player ";
  private static final String STRIPE = "stripe";
  private String myName;
  private int multiplier = 1;

  public GenericPlayer(Board board, int id, Map<String, Double> inventory, int startingGold, Map<Integer, MarkerBoard> enemyMap) {
    myBoard = board;
    myInventory = new HashMap<>(inventory);
    myInventory.put("Basic Shot", Double.MAX_VALUE);
    myCurrency = startingGold;
    myEnemyMap = enemyMap;
    myHitsMap = new HashMap<>();
    myId = id;
    myName = PLAYER_PREFIX + (id + 1);
  }

  //changed make purchase to use new ID map
  @Override
  public void makePurchase(int price, String usableID) {
    if (price <= myCurrency) {
      String genericItem = usableID.replace(STRIPE, "");
      LOG.info(String.format("Bought %s for %d gold. Remaining Gold: %d", genericItem, price, myCurrency));
      myInventory.put(genericItem, myInventory.getOrDefault(genericItem, 0.0) + 1);
      if (!usableID.startsWith(STRIPE)) {
        myCurrency -= price;
      }
    }
  }

  public Map<String, Double> getMyInventory() {
    return myInventory;
  }

  public void removePiece(String id) {
    myBoard.removePiece(id);
  }

  public void removeAllPieces() {
    myBoard.removeAllPieces();
  }

  @Override
  public boolean placePiece(Piece s, Coordinate coordinate) {
    return myBoard.placePiece(coordinate,s);
  }

  @Override
  public void addGold(int amount) {
    myCurrency += multiplier* amount;
  }

  @Override
  public void updateEnemyBoard(Coordinate c, int id, CellState state) {
    myEnemyMap.get(id).placeMarker(c, state);
  }

  @Override
  public int getNumPieces() {
    return myPiecesLeft;
  }

  public void determineHealth() {
    myPiecesLeft = 0;
    for (Piece p : myBoard.listPieces()) {
      if (p != null) {
        myPiecesLeft++;
      }
    }
  }

  public void setupBoard(CellState[][] board) {
    myBoard = new Board(board);
  }

  protected List<Coordinate> getValidCoordinates() {
    return myBoard.listCoordinates();
  }

  public WinState applyWinCondition(Function<PlayerRecord, WinState> lambda) {
    return lambda.apply(makeRecord());
  }

  //new method for moving pieces
  public void movePieces() {
    myBoard.moveAllPieces();
  }

  private PlayerRecord makeRecord() {
    return new PlayerRecord(myPiecesLeft, myCurrency, myHitsMap, myInventory, myBoard);
  }

  public Board getBoard() {
    return myBoard;
  }

  public int getID() {
    return myId;
  }

  @Override
  public String getName() {
    return myName;
  }

  @Override
  public void setName(String name) {
    myName = name;
  }

  @Override
  public boolean canBeStruck(Coordinate c) {
    return myBoard.canBeStruck(c);
  }

  @Override
  public int getMyCurrency() {
    return myCurrency;
  }

  public Map<Integer, MarkerBoard> getEnemyMap() {
    return myEnemyMap;
  }

  //new Method for updating hits map
  @Override
  public void updateShot(CellState hitResult) {
    myHitsMap.putIfAbsent(hitResult, 0);
    myHitsMap.put(hitResult, myHitsMap.get(hitResult)+1);
  }

  public void addUsableToInventory(Usable usable){
    if(myInventory.containsKey(usable.getMyID()))
      myInventory.put(usable.getMyID(), myInventory.get(usable.getMyID()) + 1);
    else
      myInventory.put(usable.getMyID(), 1.0);
  }

  public void addIDtoInventory(String ID){
   try {
     if (myInventory.containsKey(ID))
       myInventory.put(ID, myInventory.get(ID) + 1);
     else
       myInventory.put(ID, 1.0);
   }catch(Exception e){}
  }
  public void setMultiplier(int factor){ multiplier =factor;}
}

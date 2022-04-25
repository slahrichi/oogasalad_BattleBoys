package oogasalad.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.view.GameView;
import oogasalad.view.UsableRecord;

/**
 * A manager that handles the updating of individual Piece objects and how they appear on the board.
 * The class received information from the GameManager and exists so that the GameManager can focus
 * primarily on the rules of the game as opposed to having to deal with procuring all necessary
 * information for visual updates.
 *
 * @author Matthew Giglio, Minjun Kwak
 */

public class GameViewManager {

  private static final String BASIC_SHOT_ID = "Basic Shot";
  private static final String BASIC_SHOT_CLASSNAME = "BasicShot";

  private GameView view;
  private Map<Integer, Player> idMap;
  private List<Player> playerList;
  private ResourceBundle myResources;
  private Map<String, Usable> usablesIDMap;

  public GameViewManager(GameData data, Map<String, Usable> usablesIDMap, Map<Integer, Player> idMap, ResourceBundle resourceBundle) {
  /**
   * @param data         GameData object storing information pertinent to game rules
   * @param idMap        Map relating a player id to the Player themselves
   * @param allowedShots the number of shots a player can make per turn
   */
    this.idMap = idMap;
    this.playerList = data.players();
    this.myResources = resourceBundle;
    this.usablesIDMap = usablesIDMap;
    setupGameView(data);
  }

  // Creates a GameView class to show the view of the game and initialize it with the starting elements
  private void setupGameView(GameData data) {
    List<CellState[][]> boards = createFirstPlayerBoards(data);
    Collection<Collection<Coordinate>> coords = createInitialPieces(data.pieces());
    view = new GameView(boards, coords, generateIDToNames(), convertMapToUsableRecord(playerList.get(0).getMyInventory()), data.cellStateColorMap(), myResources);
    view.setShopUsables(data.allUsables());
    view.updateLabels(data.shotsPerTurn(), playerList.get(0).getNumPieces(), playerList.get(0).getMyCurrency());
  }

  // Create an ID map to get the name of a player given their ID
  private Map<Integer, String> generateIDToNames() {
    Map<Integer, String> idToName = new HashMap<>();
    for (Player p : playerList) {
      idToName.put(p.getID(), p.getName());
    }
    return idToName;
  }

  // Converts a map of usable ID to the stock of that usable to a UsableRecord object so the view can
  // display the object properly
  public List<UsableRecord> convertMapToUsableRecord(Map<String, Integer> inventoryMap) {
    List<UsableRecord> inventory = new ArrayList<>();
    String basicShotID = BASIC_SHOT_ID;
    String basicShotClassName = BASIC_SHOT_CLASSNAME;
    int basicShotStock = Integer.MAX_VALUE;
    inventory.add(new UsableRecord(basicShotID, basicShotClassName, basicShotStock));
    for (String id : inventoryMap.keySet()) {
      if (!id.equals(BASIC_SHOT_ID)) {
        inventory.add(new UsableRecord(id, usablesIDMap.get(id).getClass().getSimpleName(), inventoryMap.get(id)));
      }
    }
    return inventory;
  }

  // Creates the list of boards that the first player should be seeing when the game starts
  private List<CellState[][]> createFirstPlayerBoards(GameData data) {
    List<CellState[][]> boards = new ArrayList<>();
    Player firstPlayer = data.players().get(0);
    boards.add(firstPlayer.getBoard().getCurrentBoardState());
    for (int i = 0; i < firstPlayer.getEnemyMap().size(); i++) {
      boards.add(data.board());
    }
    return boards;
  }

  // Creates the list of pieces that the first player should be seeing is remaining on their board
  // when the game starts
  private Collection<Collection<Coordinate>> createInitialPieces(List<Piece> pieces) {
    Collection<Collection<Coordinate>> pieceCoords = new ArrayList<>();
    for (Piece piece : pieces) {
      pieceCoords.add(piece.getRelativeCoords());
    }
    return pieceCoords;
  }

  /**
   * Getter method for the GameView created by this class
   */
  GameView getView() {
    return view;
  }

  /**
   * Updates the view with the current state of the board, player, and pieces
   * @param player The current player's turn, for whom the view should be updated for
   */
  public void sendUpdatesToView(Player player) {
    List<CellState[][]> boardList = new ArrayList<>();
    List<Integer> idList = new ArrayList<>();
    List<Collection<Collection<Coordinate>>> pieceList = new ArrayList<>();
    addToBoardElements(player.getBoard().getCurrentBoardState(), player.getID(),
        player, boardList, idList, pieceList);
    Map<Integer, MarkerBoard> enemyMap = player.getEnemyMap();
    for (int id : player.getEnemyMap().keySet()) {
      addToBoardElements(enemyMap.get(id).getBoard(), id, idMap.get(id), boardList, idList,
          pieceList);
    }
    List<UsableRecord> inventory = convertMapToUsableRecord(player.getMyInventory());
    view.update(boardList, idList, pieceList, inventory);
  }

  // Helper method for the sendUpdatesToView() method, adds corresponding elements to each list
  private void addToBoardElements(CellState[][] board, int id, Player player, List<CellState[][]>
      boardList, List<Integer> idList, List<Collection<Collection<Coordinate>>> pieceList) {
    boardList.add(board);
    idList.add(id);
    pieceList.add(convertPiecesToCoords(player.getBoard().listPieces()));
  }

  // Converts a Piece object to its relative coordinates to be used by the View
  private Collection<Collection<Coordinate>> convertPiecesToCoords(List<Piece> piecesLeft) {
    Collection<Collection<Coordinate>> coords = new ArrayList<>();
    for (Piece piece : piecesLeft) {
      coords.add(piece.getRelativeCoords());
    }
    return coords;
  }

  /**
   * Updates the pieces left pane in the view
   * @param piecesLeft list of remaining pieces that the player has left
   */
  void updatePiecesLeft(List<Piece> piecesLeft) {
    Collection<Collection<Coordinate>> coords = convertPiecesToCoords(piecesLeft);
    view.updatePiecesLeft(coords);
  }
}

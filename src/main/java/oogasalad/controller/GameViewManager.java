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

  private GameView view;
  private Map<Integer, Player> idMap;
  private List<Player> playerList;
  private ResourceBundle myResources;
  private Map<String, Usable> usablesIDMap;

  private static final String BASIC_SHOT = "Basic Shot";
  private static final String BASIC_SHOT_CLASS = "BasicShot";
  private static final int BASIC_SHOT_STOCK = Integer.MAX_VALUE;

  /**
   * @param data         GameData object storing information pertinent to game rules
   * @param usablesIDMap map containing usables and their IDs
   * @param idMap        Map relating a player id to the Player themselves
   * @param allowedShots the number of shots a player can make per turn
   * @param resourceBundle bundle containing specifications given the language
   */
  public GameViewManager(GameData data, Map<String, Usable> usablesIDMap, Map<Integer, Player>
      idMap, int allowedShots, ResourceBundle resourceBundle) {
    this.idMap = idMap;
    this.playerList = data.players();
    this.myResources = resourceBundle;
    this.usablesIDMap = usablesIDMap;
    setupGameView(data, allowedShots);

  }

  private void setupGameView(GameData data, int allowedShots) {
    List<CellState[][]> boards = createFirstPlayerBoards(data);
    Collection<Collection<Coordinate>> coords = createInitialPieces(data.pieces());
    view = new GameView(boards, coords, generateIDToNames(), convertMapToUsableRecord(
        playerList.get(0).getMyInventory()), myResources);
    view.setShopUsables(data.allUsables());
    view.updateLabels(allowedShots, playerList.get(0).getNumPieces(), playerList.get(0).getMyCurrency());
  }

  private Map<Integer, String> generateIDToNames() {
    Map<Integer, String> idToName = new HashMap<>();
    for (Player p : playerList) {
      idToName.put(p.getID(), p.getName());
    }
    return idToName;
  }

  /**
   *
   * @param inventoryMap map relating usable ID to the count of usable
   * @return List of Usable records containing info for said usables
   */
  public List<UsableRecord> convertMapToUsableRecord(Map<String, Integer> inventoryMap) {
    List<UsableRecord> inventory = new ArrayList<>();
    inventory.add(new UsableRecord(BASIC_SHOT, BASIC_SHOT_CLASS, BASIC_SHOT_STOCK));
    for (String id : inventoryMap.keySet()) {
      if (!id.equals(BASIC_SHOT)) {
        inventory.add(new UsableRecord(id, usablesIDMap.get(id).getClass().getSimpleName(),
            inventoryMap.get(id)));
      }
    }
    return inventory;
  }

  private List<CellState[][]> createFirstPlayerBoards(GameData data) {
    List<CellState[][]> boards = new ArrayList<>();
    Player firstPlayer = data.players().get(0);
    boards.add(firstPlayer.getBoard().getCurrentBoardState());
    for (int i = 0; i < firstPlayer.getEnemyMap().size(); i++) {
      boards.add(data.board());
    }
    return boards;
  }

  private Collection<Collection<Coordinate>> createInitialPieces(List<Piece> pieces) {
    Collection<Collection<Coordinate>> pieceCoords = new ArrayList<>();
    for (Piece piece : pieces) {
      pieceCoords.add(piece.getRelativeCoords());
    }
    return pieceCoords;
  }

  GameView getView() {
    return view;
  }

  /**
   * Sends updated results of backend to be visualized in frontend
   *
   * @param player current Player
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

  private void addToBoardElements(CellState[][] board, int id, Player player, List<CellState[][]>
      boardList, List<Integer> idList, List<Collection<Collection<Coordinate>>> pieceList) {
    boardList.add(board);
    idList.add(id);
    pieceList.add(convertPiecesToCoords(player.getBoard().listPieces()));
  }

  private Collection<Collection<Coordinate>> convertPiecesToCoords(List<Piece> piecesLeft) {
    Collection<Collection<Coordinate>> coords = new ArrayList<>();
    for (Piece piece : piecesLeft) {
      coords.add(piece.getRelativeCoords());
    }
    return coords;
  }

  /**
   * @param piecesLeft list of remaining pieces that the player has left
   */
  void updatePiecesLeft(List<Piece> piecesLeft) {
    Collection<Collection<Coordinate>> coords = convertPiecesToCoords(piecesLeft);
    view.updatePiecesLeft(coords);
  }
}

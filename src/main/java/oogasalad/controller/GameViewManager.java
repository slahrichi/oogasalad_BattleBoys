package oogasalad.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.GameData;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.GameView;

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

  /**
   * @param data         GameData object storing information pertinent to game rules
   * @param idMap        Map relating a player id to the Player themselves
   * @param allowedShots the number of shots a player can make per turn
   */
  public GameViewManager(GameData data, Map<Integer, Player> idMap, int allowedShots) {
    this.idMap = idMap;
    playerList = data.players();
    setupGameView(data, allowedShots);
  }

  private void setupGameView(GameData data, int allowedShots) {
    List<CellState[][]> boards = createFirstPlayerBoards(data);
    Collection<Collection<Coordinate>> coords = createInitialPieces(data.pieces());
    view = new GameView(boards, coords, generateIDToNames());
    view.updateLabels(allowedShots, playerList.get(0).getNumPieces(),
        playerList.get(0).getMyCurrency());
  }

  private Map<Integer, String> generateIDToNames() {
    Map<Integer, String> idToName = new HashMap<>();
    for (Player p : playerList) {
      idToName.put(p.getID(), p.getName());
    }
    return idToName;
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

  void sendUpdatedBoardsToView(int playerIndex) {
    List<CellState[][]> boardList = new ArrayList<>();
    List<Integer> idList = new ArrayList<>();
    List<Collection<Collection<Coordinate>>> pieceList = new ArrayList<>();
    Player currentPlayer = playerList.get(playerIndex);
    addToBoardElements(currentPlayer.getBoard().getCurrentBoardState(), currentPlayer.getID(),
        currentPlayer, boardList, idList, pieceList);
    Map<Integer, MarkerBoard> enemyMap = currentPlayer.getEnemyMap();
    for (int id : currentPlayer.getEnemyMap().keySet()) {
      addToBoardElements(enemyMap.get(id).getBoard(), id, idMap.get(id), boardList, idList,
          pieceList);
    }
    view.moveToNextPlayer(boardList, idList, pieceList);
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

package oogasalad.model;

import java.util.List;
import java.util.Map;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Piece;

public class GameSetup {

  private List<Player> playerList;
  private Map<Player, List<Piece>> pieceMap;

  public GameSetup(List<Player> playerList, Map<Player, List<Piece>> pieceMap, int rows, int cols) {
    this.playerList = playerList;
    this.pieceMap = pieceMap;
    initializeGame(rows, cols);
  }

  private void initializeGame(int rows, int cols) {
    setupBoards(rows, cols);
    placePieces();
  }

  private void setupBoards(int rows, int cols) {
    for (Player p : playerList) {
      p.setupBoard(rows, cols);
    }
  }

  private void placePieces() {
    for (Player p : playerList) {
      List<Piece> list = pieceMap.get(p);
      for (Piece piece : list) {
        p.placePiece(piece);
      }
    }
  }
}

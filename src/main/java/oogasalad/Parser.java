package oogasalad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;

public class Parser {

  public Parser() {

  }

  public PlayerData parse(File file) {
    List<String> players = new ArrayList<>();
    List<Piece> pieces = new ArrayList<>();
    int[][] board = new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    CellState[][] cellBoard = new CellState[board.length][board[0].length];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        cellBoard[i][j] = CellState.values()[board[i][j]];
      }
    }
    return new PlayerData(players, pieces, cellBoard);
  }
}

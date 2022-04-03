package oogasalad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Piece;

public class Parser {

  public Parser() {

  }

  public PlayerData parse(File file) {
    List<String> players = new ArrayList<>();
    List<Piece> pieces = new ArrayList<>();
    int[][] board = new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    return new PlayerData(players, pieces, board);
  }
}

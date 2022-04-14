package oogasalad.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.tiles.enums.CellState;

public class PlayerFactory {

  private CellState[][] myBoard;
  private int myRange;
  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String INVALID_PLAYER = "Invalid player type given";

  public PlayerFactory(CellState[][] board) {
    myBoard = board;
  }

  public List<Player> createPlayerList(List<String> playerTypes) {
    myRange = playerTypes.size();
    List<Player> playerList = new ArrayList<>();
    for (int i = 0; i < playerTypes.size(); i++) {
      playerList.add(createPlayer(playerTypes.get(i), myBoard, i));
    }
    return playerList;
  }

  private Player createPlayer(String playerType, CellState[][] board, int id) {
    Board b = new Board(board);
    MarkerBoard mb = new MarkerBoard(board);
    Map<Integer, MarkerBoard> enemyMap = createEnemyMap(mb, id);
    Player p = null;
    try {
      p = (Player) Class.forName(FILEPATH + playerType).getConstructor(Board.class, int.class,
              Map.class)
          .newInstance(b, id, enemyMap);
    } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
        IllegalAccessException | NoSuchMethodException e) {
      throw new NullPointerException(INVALID_PLAYER);
    }
    return p;
  }

  private Map<Integer, MarkerBoard> createEnemyMap(MarkerBoard mb, int id) {
    Map<Integer, MarkerBoard> boardMap = new TreeMap<>();
    for (int i = 0; i < myRange; i++) {
      if (i == id) continue;
      boardMap.put(i, mb.copyOf());
    }
    return boardMap;
  }

}

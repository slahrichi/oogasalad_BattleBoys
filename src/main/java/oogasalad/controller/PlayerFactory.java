package oogasalad.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.EasyDecisionEngine;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.tiles.enums.CellState;

public class PlayerFactory {

  private static CellState[][] myBoard;
  private static int myRange;
  private static int engineIndex;
  private static Map<Integer, DecisionEngine> engineMap;
  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String INVALID_PLAYER = "Invalid player type given";


  public static PlayerFactoryRecord initializePlayers(CellState[][] board, List<String> playerTypes,
      List<String> decisionEngines) {
    myBoard = board;
    myRange = playerTypes.size();
    engineMap = new HashMap<>();
    List<Player> playerList = new ArrayList<>();
    for (int i = 0; i < playerTypes.size(); i++) {
      playerList.add(createPlayer(playerTypes.get(i), myBoard, i));
    }
    return new PlayerFactoryRecord(playerList, engineMap);
  }

  private static Player createPlayer(String playerType, CellState[][] board, int id) {
    Board b = new Board(board);
    MarkerBoard mb = new MarkerBoard(board);
    Map<Integer, MarkerBoard> enemyMap = createEnemyMap(mb, id);
    Player p = null;
    try {
      p = (Player) Class.forName(FILEPATH + playerType).getConstructor(Board.class, int.class,
              Map.class)
          .newInstance(b, id, enemyMap);
      createEngineIfAI(playerType, id, enemyMap);
    } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
        IllegalAccessException | NoSuchMethodException e) {
      throw new NullPointerException(INVALID_PLAYER);
    }
    return p;
  }

  private static void createEngineIfAI(String playerType, int id, Map<Integer, MarkerBoard> enemyMap) {
    if (playerType.equals("AIPlayer")) {
      engineMap.put(id, new EasyDecisionEngine(null, enemyMap));
    }
  }

  private static Map<Integer, MarkerBoard> createEnemyMap(MarkerBoard mb, int id) {
    Map<Integer, MarkerBoard> boardMap = new TreeMap<>();
    for (int i = 0; i < myRange; i++) {
      if (i == id) continue;
      boardMap.put(i, mb.copyOf());
    }
    return boardMap;
  }

}

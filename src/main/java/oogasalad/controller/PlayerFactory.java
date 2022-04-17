package oogasalad.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;


/**
 * Class for generating Player objects and a Map of DecisionEngines for all the AIPlayers
 *
 * @author Matthew Giglio
 */
public class PlayerFactory {

  private static CellState[][] myBoard;
  private static int myRange;
  private static Map<Player, DecisionEngine> engineMap;
  private static List<String> myDifficulties;
  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String AI_PLAYER = "AIPlayer";
  private static final String ENGINE = "DecisionEngine";
  private static final String INVALID_PLAYER = "Invalid player type given";
  private static final String INVALID_DIFFICULTY = "Invalid difficulty given";


  /**
   *
   * @param board CellState[][] used to initialize the board states of each player
   * @param playerTypes list of strings representing player type between human or AI
   * @param decisionEngines list of difficulty levels for each AIPlayer's DecisionEngine
   * @return
   */
  public static PlayerFactoryRecord initializePlayers(CellState[][] board, List<String> playerTypes,
      List<String> decisionEngines) {
    myBoard = board;
    myRange = playerTypes.size();
    engineMap = new HashMap<>();
    myDifficulties = decisionEngines;
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
    Player p;
    try {
      p = (Player) Class.forName(FILEPATH + playerType).getConstructor(Board.class, int.class,
              Map.class)
          .newInstance(b, id, enemyMap);
      if (playerType.equals(AI_PLAYER)) {
        createEngine(p, id, enemyMap);
      }
    } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
        IllegalAccessException | NoSuchMethodException e) {
      throw new NullPointerException(INVALID_PLAYER);
    }
    return p;
  }

  private static Map<Integer, MarkerBoard> createEnemyMap(MarkerBoard mb, int id) {
    Map<Integer, MarkerBoard> boardMap = new TreeMap<>();
    for (int i = 0; i < myRange; i++) {
      if (i == id) continue;
      boardMap.put(i, mb.copyOf());
    }
    return boardMap;
  }

  private static void createEngine(Player player, int id, Map<Integer, MarkerBoard> enemyMap) {
      try {
        String difficulty = myDifficulties.get(id);
        DecisionEngine ds = (DecisionEngine) Class.forName(FILEPATH + difficulty + ENGINE)
            .getConstructor(List.class, Map.class).newInstance(
                player.getBoard().listCoordinates(), enemyMap);
        engineMap.put(player, ds);
      }
      catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
          IllegalAccessException | NoSuchMethodException e) {
        throw new NullPointerException(INVALID_DIFFICULTY);
      }
  }

}

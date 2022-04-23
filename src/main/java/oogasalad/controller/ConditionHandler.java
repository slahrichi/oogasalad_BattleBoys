package oogasalad.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.winconditions.WinCondition;
import oogasalad.model.utilities.winconditions.WinState;
import oogasalad.view.GameView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class for checking win/loss conditions and applying modifiers to given Players. The GameManager
 * calls upon the ConditionHandler after each move to determine if the game can be ended
 *
 * @author Matthew Giglio, Brandon Bae
 */
public class ConditionHandler {

  private Queue<Player> playerQueue;
  private Map<Integer, Player> idMap;
  private List<WinCondition> winConditions;
  private GameView view;
  private GameViewManager manager;
  private int movePieces;
  private Map<Player, Integer> turnMap;

  private static final String PLAYER_MODIFIER = "PlayerModifier";
  private static final Logger LOG = LogManager.getLogger(ConditionHandler.class);

  /**
   * @param playerQueue    queue of players
   * @param idMap         map relating player id to Player object
   * @param winConditions list of win conditions
   * @param view          GameView object displaying the game
   */

  public ConditionHandler(Queue<Player> playerQueue, Map<Integer, Player> idMap,
      List<WinCondition> winConditions, GameView view, GameViewManager manager, int movePieces) {
    this.playerQueue = playerQueue;
    this.idMap = idMap;
    this.winConditions = winConditions;
    this.view = view;
    this.movePieces = movePieces;
    this.manager = manager;
    createTurnMap();
  }

  private void createTurnMap() {
    turnMap = new HashMap<>();
    for (Player p : playerQueue) {
      turnMap.put(p, 0);
    }
  }

  /**
   * GameManager calls upon the method after each move has been made to apply modifiers to both the
   * player who made the move and the player who had the move made against them
   *
   * @param currPlayer  current Player
   * @param enemyPlayer enemy Player
   */
  List<Modifiers> applyModifiers(Player currPlayer, Player enemyPlayer) {
    List<Modifiers> mods = enemyPlayer.getBoard().update();
    for (Modifiers mod : mods) {
        Player[] players = {currPlayer, enemyPlayer};
        try {
          mod.modifierFunction(players).accept(players);
        } catch (Exception e) {
        }
      }
    return mods;
  }

  /**
   * method to check whether any of the win conditions have been satisfied
   */
  void applyWinConditions() {
    for (WinCondition condition : winConditions) {
      checkCondition(condition);
    }
    if (playerQueue.size() == 1) {
      moveToWinGame(playerQueue.peek());
    }
  }

  private void checkCondition(WinCondition condition) {
    Set<Integer> playerIds = new HashSet<>(idMap.keySet());
    for (int id : playerIds) {
      Player currPlayer = idMap.get(id);
      WinState currPlayerWinState = condition.updateWinner(currPlayer);
      LOG.info(String.format("Player %d's WinState %s", id+1, currPlayerWinState));
      checkWinState(currPlayer, currPlayerWinState, id);
    }
  }

  private void checkWinState(Player player, WinState state, int id) {
    if (state.equals(WinState.WIN)) {
      moveToWinGame(player);
    } else if (state.equals(WinState.LOSE)) {
      removePlayer(player, id);
      view.displayLosingScreen(player.getName());
      manager.sendUpdatesToView(playerQueue.peek());
      view.switchToMainScreen();
    }
  }

  private void moveToWinGame(Player player) {
    int id = player.getID();
    LOG.info(String.format("Player %d wins!", id+1));
    view.displayWinningScreen(idMap.get(id).getName());
  }

  private void removePlayer(Player player, int id) {
    LOG.info("Player " + (id+1) + " lost");
    playerQueue.remove(player);
    idMap.remove(id);
    turnMap.remove(player);
    for (int i = 0; i < playerQueue.size(); i++) {
      Player p = playerQueue.poll();
      p.getEnemyMap().remove(id);
      playerQueue.add(p);
    }
  }

  void updateTurns(Player player) {
    turnMap.put(player, turnMap.get(player) + 1);
  }

  void resetTurnMap() {
    for (Player p : turnMap.keySet()) {
      turnMap.put(p, 0);
    }
  }

  boolean canMovePieces() {
    int turnCount = 0;
    for (Player p : turnMap.keySet()) {
      if (turnMap.get(p) == movePieces) {
        turnCount++;
      }
    }
    return turnCount == turnMap.size();
  }
}

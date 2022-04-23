package oogasalad.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.application.Platform;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.winconditions.WinCondition;
import oogasalad.model.utilities.winconditions.WinState;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
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

  private List<Player> playerList;
  private Map<Integer, Player> idMap;
  private List<WinCondition> winConditions;
  private GameView view;
  private GameViewManager manager;

  private static final String PLAYER_MODIFIER = "PlayerModifier";
  private static final Logger LOG = LogManager.getLogger(ConditionHandler.class);

  /**
   * @param playerList    list of players
   * @param idMap         map relating player id to Player object
   * @param winConditions list of win conditions
   * @param view          GameView object displaying the game
   */
  public ConditionHandler(List<Player> playerList, Map<Integer, Player> idMap,
      List<WinCondition> winConditions, GameView view, GameViewManager manager) {
    this.playerList = playerList;
    this.idMap = idMap;
    this.winConditions = winConditions;
    this.view = view;
    this.manager = manager;
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
    if (playerList.size() == 1) {
      moveToWinGame(playerList.get(0));
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
    if (state.equals(WinState.LOSE)) {
      removePlayer(player, id);
      manager.sendUpdatesToView(player);
      Platform.runLater(() -> view.displayLosingDialog(player.getName()));
    } else if (state.equals(WinState.WIN)) {
      moveToWinGame(player);
    }
  }

  private void moveToWinGame(Player player) {
    int id = player.getID();
    LOG.info(String.format("Player %d wins!", id+1));
    view.displayWinningScreen(idMap.get(id).getName());
  }

  private void removePlayer(Player player, int id) {
    LOG.info("Player " + (id+1) + " lost");
    playerList.remove(player);
    idMap.remove(id);
    for (Player p : playerList) {
      p.getEnemyMap().remove(id);
    }
  }
}

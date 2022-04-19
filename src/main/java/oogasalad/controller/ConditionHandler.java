package oogasalad.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.WinConditions.WinCondition;
import oogasalad.model.utilities.WinConditions.WinState;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.view.GameView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConditionHandler {

  private List<Player> playerList;
  private Map<Integer, Player> idMap;
  private List<WinCondition> winConditions;
  private GameView view;

  private static final Logger LOG = LogManager.getLogger(ConditionHandler.class);


  public ConditionHandler(List<Player> playerList, Map<Integer, Player> idMap,
      List<WinCondition> winConditions, GameView view) {
    this.playerList = playerList;
    this.idMap = idMap;
    this.winConditions = winConditions;
    this.view = view;
  }

  void applyModifiers(Player currPlayer, Player enemyPlayer){
    List<Modifiers> mods = enemyPlayer.getBoard().update();
    for(Modifiers mod :mods){
      if(mod.getClass().getSimpleName().equals("PlayerModifier")){
        Player[] players = {currPlayer, enemyPlayer};
        try{
          mod.modifierFunction().accept(players);
        }catch(Exception e){}
      }
    }
  }

  void applyWinConditions() {
    for (WinCondition condition: winConditions) {
      checkCondition(condition);
    }
    if(playerList.size()==1) {
      moveToWinGame(playerList.get(0));
    }
  }

  private void checkCondition(WinCondition condition) {
    Set<Integer> playerIds = new HashSet<>(idMap.keySet());
    for(int id: playerIds) {
      Player currPlayer = idMap.get(id);
      WinState currPlayerWinState = condition.updateWinner(currPlayer);
      LOG.info(String.format("Player %d's WinState %s", id, currPlayerWinState));
      checkWinState(currPlayer, currPlayerWinState, id);
    }
  }

  private void checkWinState(Player player, WinState state, int id) {
    if (state.equals(WinState.LOSE)) {
      removePlayer(player, id);
      view.displayLosingMessage(id);
    } else if (state.equals(WinState.WIN)) {
      LOG.info(String.format("Player %d wins!", id));
      moveToWinGame(player);
    }
  }

  private void moveToWinGame(Player player) {
    int id = player.getID();
    view.displayWinningMessage(id);
  }

  private void removePlayer(Player player, int id) {
    LOG.info("Player " + id + " lost");
    playerList.remove(player);
    idMap.remove(id);
    for (Player p : playerList) {
      p.getEnemyMap().remove(id);
    }
  }
}

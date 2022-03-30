package oogasalad.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;

public class GameManager {

  private List<Player> playerList;
  private Map<Integer, Player> idMap;

  public GameManager(List<Player> playerList) {
    this.playerList = playerList;
    initialize();
  }

  public void playGame() {
    while (canStillPlay())
    for (Player player : playerList) {
      player.playTurn();
    }
  }

  private void initialize() {
    idMap = new HashMap<>();
    int id = 0;
    for (Player p : playerList) {
      idMap.put(id++, p);
    }
  }

  public void executeMove(int id, Coordinate c) {
    Player enemy = idMap.get(id);
    enemy.strike(c);
    if (enemy.getHealth() == 0) {
      playerList.remove(enemy);
    }
  }

  private boolean canStillPlay() {
    return playerList.size() == 1;
  }
}

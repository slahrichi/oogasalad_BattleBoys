package oogasalad.controller;

import java.util.List;
import java.util.Map;
import oogasalad.GameData;
import oogasalad.model.players.Player;
import oogasalad.view.GameView;

public class ConditionHandler {

  private List<Player> playerList;
  private Map<Integer, Player> idMap;
  private GameView view;

  public ConditionHandler(List<Player> playerList, Map<Integer, Player> idMap, GameView view) {
    this.playerList = playerList;
    this.idMap = idMap;
    this.view = view;
  }

}

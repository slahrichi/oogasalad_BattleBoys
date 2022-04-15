package oogasalad.controller;

import java.util.List;
import java.util.Map;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.Player;

public record PlayerFactoryRecord(List<Player> playerList, Map<Player, DecisionEngine> engineMap){}

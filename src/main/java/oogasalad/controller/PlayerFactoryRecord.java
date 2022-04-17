package oogasalad.controller;

import java.util.List;
import java.util.Map;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.Player;

/**
 * Record for holding the List<Player> and the Map<Player, DecisionEngine> created by the
 * PlayerFactory
 *
 * @author Matthew Giglio
 */

public record PlayerFactoryRecord(List<Player> playerList, Map<Player, DecisionEngine> engineMap){}

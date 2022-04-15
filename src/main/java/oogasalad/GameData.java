package oogasalad;

import java.util.List;
import java.util.Map;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.WinConditions.WinCondition;
import oogasalad.model.utilities.tiles.enums.CellState;

public record GameData(List<Player> players, CellState[][] board, List<Piece> pieces,
                       List<WinCondition> winConditions, Map<Integer, DecisionEngine> engineMap) {}

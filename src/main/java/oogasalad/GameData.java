package oogasalad;

import java.util.List;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;

public record GameData(List<Player> players, CellState[][] board, List<Piece> pieces) {}

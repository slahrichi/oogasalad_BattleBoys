package oogasalad;

import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;

public record PlayerData(List<String> players, List<Piece> pieces, CellState[][] board) {}

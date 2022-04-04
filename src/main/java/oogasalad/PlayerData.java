package oogasalad;

import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Piece;

public record PlayerData(List<String> players, List<Piece> pieces, int[][] board) {}

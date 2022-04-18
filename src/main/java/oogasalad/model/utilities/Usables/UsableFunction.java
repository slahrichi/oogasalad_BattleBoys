package oogasalad.model.utilities.Usables;

import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

import java.util.function.BiFunction;

public interface UsableFunction extends BiFunction<Coordinate, Board, Map<Coordinate, CellState>> {

}

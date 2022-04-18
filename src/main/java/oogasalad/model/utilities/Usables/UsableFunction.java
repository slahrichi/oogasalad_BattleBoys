package oogasalad.model.utilities.Usables.Weapons;

import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;

import java.util.function.BiFunction;

public interface WeaponFunction extends BiFunction<Coordinate, Board, Map<Coordinate, CellState>> {

}

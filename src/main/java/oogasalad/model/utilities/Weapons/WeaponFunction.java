package oogasalad.model.utilities.Weapons;

import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface WeaponFunction extends BiFunction<Coordinate, Board, CellState[]> {

}

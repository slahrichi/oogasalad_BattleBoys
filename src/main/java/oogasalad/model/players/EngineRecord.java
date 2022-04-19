package oogasalad.model.players;

import oogasalad.model.utilities.Coordinate;

/**
 * Record to hold the information pertinent to an AIPlayer's move (location and enemy of concern)
 *
 * @author Matthew Giglio
 */
public record EngineRecord(Coordinate shot, int enemyID) {}

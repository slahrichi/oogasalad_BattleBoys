package oogasalad.model.utilities.tiles;

import oogasalad.model.utilities.Coordinate;

public interface Tile {

   int hit();

   void update();

   boolean canCarryObject();

   void updateCoordinates(int x, int y);

   Coordinate getCoordinates();

}

package oogasalad.model.utilities.tiles;

import oogasalad.model.utilities.Coordinate;

public interface Cell {

   int hit();

   void update();

   boolean canCarryObject();

   void updateCoordinates(int row, int col);

   Coordinate getCoordinates();

}

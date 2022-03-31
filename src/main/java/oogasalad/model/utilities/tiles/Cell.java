package oogasalad.model.utilities.tiles;

import oogasalad.model.utilities.Coordinate;

public interface Cell{

   void hit();

   void update();

   boolean canCarryObject();

   void updateCoordinates();

   Coordinate getCoordinates();

}

package oogasalad.model.utilities.tiles;

import oogasalad.model.utilities.Coordinate;

public interface Cell {

   int hit();

   void update();

   boolean canCarryObject();

   void updateCoordinates(int x, int y);

   Coordinate getCoordinates();

   /**
    * Getter method that returns integer representation of cell's current state
    * @return integer representation of cell state (ordinal matches up with cellStates enum)
    */
   int getCellState();
}

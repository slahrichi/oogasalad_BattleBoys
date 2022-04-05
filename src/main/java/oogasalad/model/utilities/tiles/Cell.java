package oogasalad.model.utilities.tiles;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import oogasalad.model.utilities.Coordinate;

public interface Cell {

   int hit();

   List<Function> boardUpdate();

   List<Function> playerUpdate();


   boolean canCarryObject();

   void updateCoordinates(int row, int col);

   Coordinate getCoordinates();

   /**
    * Getter method that returns integer representation of cell's current state
    * @return integer representation of cell state (ordinal matches up with cellStates enum)
    */
   int getCellState();
}

package oogasalad.model.utilities.tiles;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

public interface CellInterface {

   CellState hit();

   List<Consumer> update();



   boolean canCarryObject();

   void updateCoordinates(int row, int col);

   Coordinate getCoordinates();

   /**
    * Getter method that returns integer representation of cell's current state
    * @return integer representation of cell state (ordinal matches up with cellStates enum)
    */
   CellState getCellState();

   public int getHealth();
}

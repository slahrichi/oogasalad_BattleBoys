package oogasalad.model.utilities.tiles;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.enums.CellState;

public interface CellInterface {


   CellState hit();

   List<Modifiers> update();



   boolean canCarryObject();

   //do we need this? Its a setter so you know how it be.
   void updateCoordinates(int row, int col);

   void moveCoordinate(Coordinate amtToMove);

   Coordinate getCoordinates();

   void addModifier(Modifiers myMod);

   /**
    * Getter method that returns integer representation of cell's current state
    * @return integer representation of cell state (ordinal matches up with cellStates enum)
    */
   CellState getCellState();

   public int getHealth();
}

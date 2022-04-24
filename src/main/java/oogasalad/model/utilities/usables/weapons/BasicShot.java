package oogasalad.model.utilities.usables.weapons;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;

public class BasicShot extends Weapon{

  public BasicShot(){
    super("Basic Shot", 0);
    addRelativePosition(new Coordinate(0,0),1);
    makeWeaponFunction();
  }


  @Override
  protected void makeWeaponFunction() {
    setMyFunction((absolute, board) -> {
        Map<Coordinate, CellState> results = new HashMap<>();
        if(board.checkBoundedCoordinate(absolute)){
          results.put(absolute, board.hit(absolute, 1));
        }else{
          throw new NullPointerException("Coordinate Provided Out of Bounds");
        }
        return results;
      }
    );
  }
}

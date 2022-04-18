package oogasalad.model.utilities.Usables.Weapons;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Usables.UsableFunction;
import oogasalad.model.utilities.tiles.enums.CellState;

public class BasicShot extends Weapon{

  public BasicShot(){
    super("Basic Shot", 0);
    addRelativePosition(new Coordinate(0,0),1);
    makeWeaponFunction();
  }


  @Override
  protected void makeWeaponFunction() {
    setMyFunction( new UsableFunction() {
      @Override
      public Map<Coordinate, CellState> apply(Coordinate coord, Board board) {
        Map<Coordinate, CellState> results = new HashMap<>();
        if(board.checkBoundedCoordinate(coord)){
          results.put(coord, board.hit(coord, 1));
        }else{
          throw new NullPointerException("Coordinate Provided Out of Bounds");
        }
        return results;
      }
    });
  }
}

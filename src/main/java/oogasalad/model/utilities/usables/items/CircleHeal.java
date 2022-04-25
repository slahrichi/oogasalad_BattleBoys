package oogasalad.model.utilities.usables.items;

import java.util.HashMap;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.UsableFunction;

public class CircleHeal extends Item{

  int myRadius;
  int healingPower;
  public CircleHeal(String id, int gold, int radius, int healingPower){
    super(id, gold);
    this.myRadius = radius;
    this.healingPower = healingPower;
  }


  private void generateRelativeCoords(){
    for(int row = -1*myRadius ; row<myRadius; row++){
      for(int col = -1*myRadius; col<myRadius; col++){
        addRelativePosition(new Coordinate(row,col), 0);
      }
    }
  }

  @Override
  protected UsableFunction makeItemFunction() {
    UsableFunction ret = (abs,board)->{
      Map<Coordinate, CellState> returnStates = new HashMap<>();
      for(Coordinate relative : getRelativeCoordShots().keySet()){
        Coordinate mappedCoord = new Coordinate(relative.getRow() + abs.getRow(),relative.getColumn() + abs.getColumn());
        if(board.checkBoundedCoordinate( mappedCoord)){
          returnStates.put(mappedCoord, board.hit(mappedCoord, healingPower));
        }
      }
      return returnStates;
    };
    return ret;
  }
}

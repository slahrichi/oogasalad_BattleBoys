package oogasalad.model.utilities;

import java.util.List;

public class StaticPiece extends Piece {
  public StaticPiece(List<Cell> cellList){
    super(cellList);
  }

  @Override
  public void registerDamage(Coordinate hitLocation) {

    for (Cell cell : getCellList()) {
      if (cell.getPosition().equals(hitLocation)) {
        if (getStatus().equals("Alive")) {
          updateStatus("Damaged");
          getCellList().remove(cell);
        }
        if (checkDeath()) {
          updateStatus("Dead");
        }
      }
    }
  }


}

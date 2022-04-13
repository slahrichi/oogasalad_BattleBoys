package oogasalad.model.utilities;

import java.util.List;
import java.util.Map;
import javafx.scene.control.Cell;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.enums.CellState;

public class GenericWeapon {
  List<Shot> myWeaponEffect;

  public GenericWeapon(List<Shot> weaponEffect) {
    myWeaponEffect = weaponEffect;
  }

  public void applyWeapon(Coordinate absoluteCoord, Map<Coordinate, CellInterface> cellMap, MarkerBoard markerBoard) {
    for(Shot s: myWeaponEffect) {
      Coordinate shotCoord = s.getShotAbsoluteCoord(absoluteCoord);
      CellInterface affectedCell = cellMap.get(shotCoord);
      CellState shotEffect = s.applyShot(affectedCell);
      markerBoard.placeMarker(shotCoord, shotEffect);
    }
  }
}

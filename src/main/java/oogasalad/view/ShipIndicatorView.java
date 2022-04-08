package oogasalad.view;

import oogasalad.view.board.BoardView;
import oogasalad.view.board.BoardShapeType;
import oogasalad.view.board.ShapeType;
import oogasalad.view.board.ShipShapeType;

public class ShipIndicatorView extends BoardView {
  public ShipIndicatorView(ShapeType type, int[][] shipLayout, int id) {
    super(type, shipLayout, id);
    //placeShipInView(shipLayout);
  }

  @Override
  public void initializeCellViews(int[][] arrayLayout, ShapeType shape) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        CellView cell = new CellView(shape, arrayLayout[row][col] == 0 ? mapCellToColor.get(INVALID) : mapCellToColor.get(HEALTHY_SHIP),
            row, col, arrayLayout.length, arrayLayout[0].length);
        myLayout[row][col] = cell;
      }
    }
  }

//  private void placeShipInView(int[][] layout) {
//    for (int i = 0; i < layout.length; i++) {
//      for (int j = 0; j < layout[0].length; j++) {
//        setColorAt(i, j, Color.BLACK);
//      }
//    }
//  }
}

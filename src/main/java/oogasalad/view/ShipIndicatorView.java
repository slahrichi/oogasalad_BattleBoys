package oogasalad.view;

import javafx.scene.paint.Color;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.board.BoardView;

public class ShipIndicatorView extends BoardView {
  public ShipIndicatorView(double size, CellState[][] shipLayout, int id) {
    super(size, shipLayout, id);
//    for (int i = 0 ; i < shipLayout.length; i++) {
//      for (int j = 0; j < shipLayout[0].length; j++) {
//        System.out.println(shipLayout[i][j].name());
//      }
//    }
    //placeShipInView(shipLayout);
  }

  public void initializeCellViews(CellState[][] arrayLayout) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        System.out.println(arrayLayout[row][col].name());
        CellView cell = new CellView(myBoardMaker, arrayLayout[row][col] == CellState.NOT_DEFINED ? Color.valueOf(
            myCellStateResources.getString(FILL_PREFIX+CellState.NOT_DEFINED.name())) : Color.valueOf(
            myCellStateResources.getString(FILL_PREFIX+CellState.SHIP_HEALTHY.name())),
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

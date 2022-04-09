package oogasalad.view;

import javafx.scene.paint.Color;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.board.BoardView;

public class ShipIndicatorView extends BoardView {

  public ShipIndicatorView(double size, CellState[][] shipLayout, int id) {
    super(size, shipLayout, id);
  }

  @Override
  public void initializeCellViews(CellState[][] arrayLayout) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        CellView cell = new CellView(myBoardMaker, Color.valueOf(
            FILL_PREFIX + myCellStateResources.getString(arrayLayout[row][col].name())),
            row, col, arrayLayout.length, arrayLayout[0].length);
        myLayout[row][col] = cell;
      }
    }
  }
}

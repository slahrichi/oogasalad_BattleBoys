package oogasalad.view.board;

import javafx.scene.paint.Color;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;

public class SetupBoardView extends BoardView {

  public SetupBoardView(double size, CellState[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  public void initializeCellViews(CellState[][] arrayLayout) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        if (arrayLayout[row][col] == CellState.NOT_DEFINED) {
          CellView cell = new CellView(myBoardMaker, Color.valueOf(
              myCellStateResources.getString(FILL_PREFIX+CellState.SHIP_HEALTHY.name())), row, col, arrayLayout.length, arrayLayout[0].length);
          cell.addObserver(this);
          myLayout[row][col] = cell;
        }
        if (arrayLayout[row][col] == CellState.WATER) {
          CellView cell = new CellView(myBoardMaker, Color.valueOf(
              myCellStateResources.getString(FILL_PREFIX+CellState.WATER.name())), row, col, arrayLayout.length,
              arrayLayout[0].length);
          cell.addObserver(this);
          myLayout[row][col] = cell;
        }
      }
    }
  }
}

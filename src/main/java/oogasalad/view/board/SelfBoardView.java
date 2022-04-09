package oogasalad.view.board;

import java.util.List;
import javafx.scene.paint.Color;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;

public class SelfBoardView extends GameBoardView {

  public SelfBoardView(double size, CellState[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  public void initializeCellViews(CellState[][] arrayLayout) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        List<Double> points = myBoardMaker.calculatePoints(row, col);
        CellView cell = new CellView(points, arrayLayout[row][col] == CellState.SHIP_HEALTHY ? Color.valueOf(
            myCellStateResources.getString(FILL_PREFIX+CellState.SHIP_HEALTHY.name())) : arrayLayout[row][col] == CellState.ISLAND_HEALTHY ? Color.valueOf(
            myCellStateResources.getString(FILL_PREFIX+CellState.ISLAND_HEALTHY.name())) : Color.valueOf(
            myCellStateResources.getString(FILL_PREFIX+CellState.WATER.name())), row, col);
        myLayout[row][col] = cell;
      }
    }
  }
}

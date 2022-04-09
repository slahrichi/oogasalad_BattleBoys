package oogasalad.view.board;

import java.util.List;
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
        List<Double> points = myBoardMaker.calculatePoints(row, col);
        CellView cell = new CellView(points, Color.valueOf(
            myCellStateResources.getString(FILL_PREFIX + arrayLayout[row][col].name())), row, col);
        cell.addObserver(this);
        myLayout[row][col] = cell;
      }
    }
  }
}

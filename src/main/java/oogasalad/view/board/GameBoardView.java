package oogasalad.view.board;

import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;

/**
 * This class represents BoardViews that are meant to be displayed during the main game phase.
 *
 * @author Minjun Kwak, Eric Xie
 */
public abstract class GameBoardView extends BoardView {

  public GameBoardView(double size, CellState[][] arrayLayout, Map<CellState, Color> colorMap, int id) {
    super(size, arrayLayout, colorMap, id);
  }

  /**
   * Initializes the cells of the BoardView by creating new CellView instances and adding them to
   * the CellView array associated with this BoardView
   *
   * @param arrayLayout the layout of the cells of this BoardView
   * @param size        the size of each cell
   */
  public void initializeCellViews(CellState[][] arrayLayout, double size) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        List<Double> points = BoardMaker.calculatePoints(row, col, size);
        CellView cell = new CellView(points, myColorMap.get(arrayLayout[row][col]), row, col);
        cell.addObserver(this);
        myLayout[row][col] = cell;
      }
    }
  }
}

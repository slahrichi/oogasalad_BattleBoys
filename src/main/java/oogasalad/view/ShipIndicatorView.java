package oogasalad.view;

import static oogasalad.view.GameView.CELL_STATE_RESOURCES;

import java.beans.PropertyChangeEvent;
import java.util.List;
import javafx.scene.paint.Color;
import oogasalad.model.utilities.Coordinate;
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
        List<Double> points = myBoardMaker.calculatePoints(row, col);
        CellView cell = new CellView(points, Color.valueOf(
          CELL_STATE_RESOURCES.getString(FILL_PREFIX + arrayLayout[row][col].name())),
            row, col);
        myLayout[row][col] = cell;
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName(), new Info(((Coordinate) evt.getNewValue()).getRow(), ((Coordinate) evt.getNewValue()).getColumn(), myID));
  }
}

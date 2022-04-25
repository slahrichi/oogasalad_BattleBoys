package oogasalad.view.board;

import static oogasalad.view.GameView.CELL_STATE_RESOURCES;

import java.beans.PropertyChangeEvent;
import java.util.List;
import javafx.scene.paint.Color;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;
import oogasalad.view.Info;

public class ShipIndicatorBoard extends BoardView {

  public ShipIndicatorBoard(double size, CellState[][] shipLayout, int id) {
    super(size, shipLayout, id);
  }

  @Override
  public void initializeCellViews(CellState[][] arrayLayout, double size) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        List<Double> points = BoardMaker.calculatePoints(row, col, size);
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
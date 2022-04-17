package oogasalad.view.board;

import static oogasalad.view.GameView.MARKER_RESOURCES;

import java.beans.PropertyChangeEvent;
import java.util.List;
import javafx.scene.paint.Color;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;
import oogasalad.view.Info;

public class EnemyBoardView extends GameBoardView {

  private static final String HANDLE_SHOT = "handleShot";

  public EnemyBoardView(double size, CellState[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  public void initializeCellViews(CellState[][] arrayLayout) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        List<Double> points = myBoardMaker.calculatePoints(row, col);
        CellView cell = new CellView(points, Color.valueOf(MARKER_RESOURCES.getString(FILL_PREFIX+arrayLayout[row][col].name())), row, col);
        cell.addObserver(this);
        myLayout[row][col] = cell;
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(HANDLE_SHOT, new Info(((Coordinate) evt.getNewValue()).getRow(), ((Coordinate) evt.getNewValue()).getColumn(), myID));
  }
}


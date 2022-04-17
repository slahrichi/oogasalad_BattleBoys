package oogasalad.view.board;

import static oogasalad.view.GameView.CELL_STATE_RESOURCES;

import java.beans.PropertyChangeEvent;
import java.util.List;
import javafx.scene.paint.Color;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;
import oogasalad.view.Info;

/**
 * This class is a BoardView that represents the current player's own board. It has no onClickHandlers
 * as a user clicking on their own board should not do anything. This BoardView is displayed as one
 * of the BoardViews during the main game.
 *
 * @author Minjun Kwak, Eric Xie, Edison Ooi
 */
public class SelfBoardView extends GameBoardView {

  private static final String HANDLE_SELF = "selfBoardClicked";

  public SelfBoardView(double size, CellState[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  public void initializeCellViews(CellState[][] arrayLayout) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        List<Double> points = myBoardMaker.calculatePoints(row, col);
        CellView cell = new CellView(points, Color.valueOf(CELL_STATE_RESOURCES.getString(FILL_PREFIX+arrayLayout[row][col].name())), row, col);
        cell.addObserver(this);
        myLayout[row][col] = cell;
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(HANDLE_SELF, new Info(((Coordinate) evt.getNewValue()).getRow(), ((Coordinate) evt.getNewValue()).getColumn(), myID));
  }
}

package oogasalad.view.board;

import static oogasalad.view.GameView.CELL_STATE_RESOURCES;

import java.beans.PropertyChangeEvent;
import java.util.List;
import javafx.scene.paint.Color;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;

/**
 * This class represents a BoardView that appears during the setup phase of the game. It allows
 * users to place their Pieces on their board. Because the frontend is not in charge of preserving
 * board state for each player, only one instance of SetupBoardView is created for the entire setup
 * phase, and it is cleared after each player finishes placing their Pieces.
 *
 * @author Eric Xie, Minjun Kwak, Edison Ooi
 */
public class SetupBoardView extends BoardView {

  private static final String PLACE_PIECE = "placePiece";

  public SetupBoardView(double size, CellState[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  public void initializeCellViews(CellState[][] arrayLayout) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        List<Double> points = myBoardMaker.calculatePoints(row, col);
        CellView cell = new CellView(points, Color.valueOf(
            CELL_STATE_RESOURCES.getString(FILL_PREFIX + arrayLayout[row][col].name())), row,
            col);
        if (arrayLayout[row][col] == CellState.WATER) {
          cell.addObserver(this);
        }
        myLayout[row][col] = cell;
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName(), evt.getNewValue());
  }
}

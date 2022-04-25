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

  private static final String SELF = "Self";

  public SelfBoardView(double size, CellState[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName()+SELF, ((Coordinate) evt.getNewValue()).getRow() + " " + ((Coordinate) evt.getNewValue()).getColumn() + " " + myID);
  }
}

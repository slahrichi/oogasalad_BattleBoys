package oogasalad.view.board;

import static oogasalad.view.GameView.CELL_STATE_RESOURCES;

import java.beans.PropertyChangeEvent;
import java.util.List;
import javafx.scene.paint.Color;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.CellView;
import oogasalad.view.Info;

public class EnemyBoardView extends GameBoardView {

  private static final String ENEMY = "Enemy";

  public EnemyBoardView(double size, CellState[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName()+ENEMY, ((Coordinate) evt.getNewValue()).getRow() + " " + ((Coordinate) evt.getNewValue()).getColumn() + " " + myID);
  }
}


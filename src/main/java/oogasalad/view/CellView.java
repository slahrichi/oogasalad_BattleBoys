package oogasalad.view;

import java.util.List;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.Coordinate;
import oogasalad.view.board.BoardMaker;

public class CellView extends PropertyObservable {

  private Coordinate myCoords;
  private Polygon myShape;

  // pass in list of points to create instead of passing board width and height (number of rows and cols)
  public CellView(List<Double> points, Paint color, int row, int col) {
    myCoords = new Coordinate(row, col);
    myShape = new Polygon();
    myShape.setFill(color);
    myShape.getPoints().addAll(points);
    myShape.setOnMouseClicked(e -> cellClicked());
    myShape.setOnMouseEntered(e -> cellHovered());
    myShape.setOnMouseExited(e -> cellExited());
  }

  private void cellClicked() {
    notifyObserver(new Object(){}.getClass().getEnclosingMethod().getName(), myCoords);
  }

  private void cellHovered() {
    notifyObserver(new Object(){}.getClass().getEnclosingMethod().getName(), myCoords);
  }

  private void cellExited() {
    notifyObserver(new Object(){}.getClass().getEnclosingMethod().getName(), myCoords);
  }

  public Coordinate getCoords() {
    return myCoords;
  }

  public Polygon getCell() {
    return myShape;
  }

}

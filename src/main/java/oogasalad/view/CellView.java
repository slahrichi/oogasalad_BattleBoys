package oogasalad.view;

import javafx.scene.shape.Polygon;
import oogasalad.Coordinate;
import oogasalad.PropertyObservable;

public class CellView extends PropertyObservable {

  private boolean isActive;
  private Coordinate myCoords;
  private Polygon myShape;

  // no need for status anymore because listeners are only attached to active cells
  public CellView(ShapeType shape, int x, int y, int width, int height) {
//    isActive = status;
    myCoords = new Coordinate(x, y);
    myShape = new Polygon();
    myShape.getPoints().addAll(shape.getPoints(x, y, width, height));
    myShape.setOnMouseClicked(e -> cellClicked());
  }

  public void cellClicked() {
    notifyObserver("cellClicked", myCoords);
  }

  public Coordinate getCoords() {
    return myCoords;
  }

  public Polygon getCell() {
    return myShape;
  }

}

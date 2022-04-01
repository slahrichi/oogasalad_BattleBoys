package oogasalad.view;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.Coordinate;
import oogasalad.view.board.ShapeType;

public class CellView extends PropertyObservable {

  private boolean isActive;
  private Coordinate myCoords;
  private Polygon myShape;

  // no need for status anymore because listeners are only attached to active cells
  public CellView(ShapeType shape, Paint color, int x, int y, int width, int height) {
//    isActive = status;
    myCoords = new Coordinate(x, y);
    myShape = new Polygon();
    myShape.setFill(color);
    myShape.getPoints().addAll(shape.getPoints(x, y, width, height));
    myShape.setOnMouseClicked(e -> cellClicked());
  }

  public void cellClicked() {
    // gets the name of the current method being executed - in this case it is "cellClicked"
    // may not need if cells are the only thing on the board that can be clicked on
//    System.out.print(myCoords.getRow() + ",");
//    System.out.println(myCoords.getColumn());
    notifyObserver(new Object(){}.getClass().getEnclosingMethod().getName(), myCoords);
  }

  public Coordinate getCoords() {
    return myCoords;
  }

  public Polygon getCell() {
    return myShape;
  }

}

package oogasalad.view;

import java.util.List;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.Coordinate;

/**
 * This class creates a viewable cell that makes up a board. It is represented by a Polygon so that
 * the shape can be modified easily through the calculatePoints() method of the BoardMaker.
 *
 * @author Minjun Kwak
 */
public class CellView extends PropertyObservable {

  private Coordinate myCoords;
  private Polygon myShape;

  /**
   * Constructor for a cell. It initializes and sets all the properties for this cell's Polygon
   * @param points the points that make up this cell's Polygon
   * @param color the color that this cell's Polygon should be filled with
   * @param row the row number of this cell
   * @param col the column number of this cell
   */
  public CellView(List<Double> points, Paint color, int row, int col) {
    myCoords = new Coordinate(row, col);
    myShape = new Polygon();
    myShape.setFill(color);
    myShape.getPoints().addAll(points);
    myShape.setOnMouseClicked(e -> cellClicked());
    myShape.setOnMouseEntered(e -> cellHovered());
    myShape.setOnMouseExited(e -> cellExited());
  }

  // handler for when a cell is clicked
  private void cellClicked() {
    notifyObserver(new Object() {
    }.getClass().getEnclosingMethod().getName(), myCoords);
  }

  // handler for when a cell is hovered over
  private void cellHovered() {
    notifyObserver(new Object() {
    }.getClass().getEnclosingMethod().getName(), myCoords);
  }

  // handler for when a cell is exited
  private void cellExited() {
    notifyObserver(new Object() {
    }.getClass().getEnclosingMethod().getName(), myCoords);
  }

  /**
   * Getter for the coordinates of this cell
   * @return myCoords
   */
  public Coordinate getCoords() {
    return myCoords;
  }

  /**
   * Getter for the Polygon of this cell
   * @return myShape
   */
  public Polygon getCell() {
    return myShape;
  }

}

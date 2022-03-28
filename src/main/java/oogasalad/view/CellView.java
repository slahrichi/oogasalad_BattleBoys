package oogasalad.view;

import java.awt.Button;
import java.awt.HeadlessException;
import java.awt.Point;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import javax.swing.JButton;
import oogasalad.Coordinate;

// cell class

public class CellView extends JButton {

  private boolean isActive;
  private Coordinate myCoords;
  private Polygon node;

  // some kind of constructor
  public CellView(boolean status, int x, int y) {
    isActive = status;
    myCoords = new Coordinate(x, y);

  }

  public Coordinate getCoords() {
    return myCoords;
  }

  public Node getNode() {

  }

}

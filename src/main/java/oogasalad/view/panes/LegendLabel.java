package oogasalad.view.panes;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LegendLabel extends HBox {

  private Color myColor;
  private String myName;

  public LegendLabel(String labelName, Color color) {

    myColor = color;
    myName = labelName;

    createColorIndicator();
    setUpLabel();

  }

  private void createColorIndicator() {

    Rectangle legendColor = new Rectangle();
    legendColor.setWidth(30);
    legendColor.setHeight(30);
    legendColor.setFill(myColor);

    this.getChildren().add(legendColor);


  }

  private void setUpLabel() {

    Label name = new Label("   " + myName);
    this.getChildren().add(name);


  }


}

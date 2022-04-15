package oogasalad.view.panes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class LegendPane extends TitledPane {

  private ScrollPane myScroller;
  private GridPane myGrid;
  private LinkedHashMap<String, Color> colorMap;

  private static final int PANE_PADDING = 10;
  private static final int PANE_GAP = PANE_PADDING / 2;


  public LegendPane(LinkedHashMap<String, Color> colors){
    colorMap = colors;

    setUpGrid();
    setUpPane();
  }

  private void setUpGrid(){
    myGrid = new GridPane();
    myGrid.setPadding(new Insets(PANE_PADDING));
    myGrid.setHgap(PANE_GAP);
    myGrid.setVgap(PANE_GAP);

    myScroller = new ScrollPane();
    myScroller.setContent(myGrid);

    String[] orderedKeys = colorMap.keySet().toArray(new String[0]);

    for (int i = 0; i < orderedKeys.length; i++) {
      myGrid.add(new LegendElement(orderedKeys[i], colorMap.get(orderedKeys[i])), 0, i);
    }
  }

  private void setUpPane(){

    this.setId("legendPane");
    this.setText("Piece Legend");
    this.setContent(myScroller);
    this.setExpanded(false);
    this.setMaxHeight(150);

  }

  // This class represents one cell in the legend, consisting of a rectangle with a color and then a label that
  // says what that color represents
  private class LegendElement extends HBox {

    private Color myColor;
    private String myName;

    // Class constructor
    private LegendElement(String labelName, Color color) {
      myColor = color;
      myName = labelName;

      createColorIndicator();
      setUpLabel();
    }

    // Creates rectangle of the specified color
    private void createColorIndicator() {
      Rectangle legendColor = new Rectangle();
      legendColor.setWidth(30);
      legendColor.setHeight(30);
      legendColor.setFill(myColor);

      this.getChildren().add(legendColor);
    }

    // Creates label for colored rectangle and places it to the right of the rectangle
    private void setUpLabel() {
      Label name = new Label("   " + myName);
      this.getChildren().add(name);
    }
  }
}

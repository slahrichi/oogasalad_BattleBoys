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

/**
 * This class represents a legend that helps the user know what colors on the board represent in terms of CellStates.
 *
 * @author Edison Ooi, Eric Xie
 */
public class LegendPane extends TitledPane {

  private ScrollPane myScroller;
  private GridPane myGrid;

  private LinkedHashMap<String, Color> colorMap;
  private ResourceBundle myResources;

  private static final String LEGEND_PANE_ID = "legendPane";
  private static final String LEGEND_TEXT = "Legend Key";

  private static final int PANE_PADDING = 10;
  private static final int PANE_GAP = PANE_PADDING / 2;
  private static final double LEGEND_COLOR_WIDTH = 30;
  private static final double LEGEND_COLOR_HEIGHT = 30;
  private static final String LABEL_SPACING = "   ";
  private static final double PANE_MAX_HEIGHT = 250;

  /**
   * Class constructor.
   * @param colors List of key-value pairs of color and what the color represents
   */
  public LegendPane(LinkedHashMap<String, Color> colors){
    colorMap = colors;
    setUpGrid();
    setUpPane();
  }

  // Sets up list of LegendElements to appear in this Pane
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

  // Sets up main Pane
  private void setUpPane(){

    this.setId(LEGEND_PANE_ID);
    this.setText(LEGEND_TEXT);
    this.setContent(myScroller);
    this.setExpanded(false);
    this.setMaxHeight(PANE_MAX_HEIGHT);

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
      legendColor.setWidth(LEGEND_COLOR_WIDTH);
      legendColor.setHeight(LEGEND_COLOR_HEIGHT);
      legendColor.setFill(myColor);
      legendColor.setStroke(Color.BLACK);

      this.getChildren().add(legendColor);
    }

    // Creates label for colored rectangle and places it to the right of the rectangle
    private void setUpLabel() {
      Label name = new Label(LABEL_SPACING + myName);
      this.getChildren().add(name);
    }
  }
}

package oogasalad.view.panes;

import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;



public class LegendPane extends TitledPane{

  private GridPane myGrid;
  private ResourceBundle myCellStateResources;
  private HashMap<String, Color> keyPairs;


  private static final int PANE_PADDING = 10;
  private static final int PANE_GAP = PANE_PADDING / 2;


  public LegendPane(){

    myCellStateResources = ResourceBundle.getBundle("/CellState");

    setUpHashMap();
    setUpGrid();
    setUpPane();

  }

  private void setUpGrid(){

    myGrid = new GridPane();
    myGrid.setPadding(new Insets(PANE_PADDING));
    myGrid.setHgap(PANE_GAP);
    myGrid.setVgap(PANE_GAP);


    int index = 0;
    for(String key : keyPairs.keySet()){

      myGrid.add(new LegendLabel(key, keyPairs.get(key)), 0, index);
      index++;

    }

//
//    myGrid.add(new LegendLabel("Ship", Color.BLACK), 0, 0);
//    myGrid.add(new Label("     "), 1, 0);
//    myGrid.add(new LegendLabel("Water", Color.BLUE), 2, 0);
//    myGrid.add(new LegendLabel("Sunk Ship", Color.RED), 0, 1);
//    myGrid.add(new Label("     "), 1, 1);
//    myGrid.add(new LegendLabel("Hit Ship", Color.ORANGE), 2, 1);

  }

  private void setUpPane(){

    this.setId("legendPane");
    this.setText("Legend Key");
    this.setContent(myGrid);
    this.setExpanded(false);
    this.setMinHeight(250);

  }

  // thinking ahead of when we're passed the list of user chosen colors

  private void setUpHashMap(){

    keyPairs = new HashMap<>();


    keyPairs.put("Ship", Color.BLACK);
    keyPairs.put("Water", Color.BLUE);
    keyPairs.put("Ship Sunk", Color.RED);
    keyPairs.put("Ship Hit", Color.ORANGE);
    keyPairs.put("Missed Shot", Color.YELLOW);
    keyPairs.put("Island Damaged", Color.GREEN);
    keyPairs.put("Island Sunk", Color.PURPLE);


  }






}

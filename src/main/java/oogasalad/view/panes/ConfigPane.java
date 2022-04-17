package oogasalad.view.panes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

import oogasalad.view.maker.ButtonMaker;

public class ConfigPane extends TitledPane {

  private static final String CONFIG_TEXT = "Configuration";
  private static final String CONFIG_ID = "configPane";
  private static final String GRID_ID = "configGrid";
  private static final String NIGHT_MODE_ID = "night-mode";
  private static final String NIGHT_MODE_TEXT = "Night Mode";

  private Button nightBtn;
  private GridPane myGrid;

  public ConfigPane(){
    setText(CONFIG_TEXT);
    setId(CONFIG_ID);
    setExpanded(false);

    myGrid = new GridPane();
    setContent(myGrid);
    myGrid.setId(GRID_ID);
  }

  public void setOnAction(EventHandler<ActionEvent> e){
    nightBtn = ButtonMaker.makeTextButton(NIGHT_MODE_ID, e, NIGHT_MODE_TEXT);
    myGrid.add(nightBtn, 0, 0);
  }
}

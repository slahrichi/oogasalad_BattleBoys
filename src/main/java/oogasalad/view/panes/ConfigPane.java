package oogasalad.view.panes;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

import oogasalad.view.maker.ButtonMaker;

public class ConfigPane extends TitledPane {


  private Button nightBtn;
  private GridPane myGrid;

  public ConfigPane(){
    setText("Configuration");
    setId("configPane");
    setExpanded(false);

    myGrid = new GridPane();
    setContent(myGrid);
    myGrid.setId("configGrid");

  }

  public void setOnAction(EventHandler<ActionEvent> e){

    nightBtn = ButtonMaker.makeTextButton("night-mode", e, "Night Mode");
    myGrid.add(nightBtn, 0, 0);

  }


}

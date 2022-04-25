package oogasalad.view.panes;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;

import oogasalad.view.maker.ButtonMaker;

/**
 * This class represents a configuration panel on screen that allows users to configure certain
 * visual aspects of the program, such as night mode.
 *
 * @author Eric Xie
 */
public class ConfigPane extends TitledPane {

  private static ResourceBundle myResources;

  private static final String CONFIG_ID = "configPane";
  private static final String GRID_ID = "configGrid";
  private static final String NIGHT_MODE_ID = "night-mode";
  private static final String NIGHT_MODE_RESOURCE = "NightMode";

  private Button nightBtn;
  private GridPane myGrid;

  /**
   * Constructor for the ConfigPane object
   *
   * @param resourceBundle, the ResourceBundle that has the language
   */

  public ConfigPane(ResourceBundle resourceBundle) {

    myResources = resourceBundle;
    setId(CONFIG_ID);
    setExpanded(false);

    myGrid = new GridPane();
    setContent(myGrid);
    myGrid.setId(GRID_ID);
  }

  /**
   * Sets an action on the button based on the event passed to it
   *
   * @param e, the event handler action event that's passed to it
   */
  public void setOnAction(EventHandler<ActionEvent> e) {
    nightBtn = ButtonMaker.makeTextButton(NIGHT_MODE_ID, e,
        myResources.getString(NIGHT_MODE_RESOURCE));
    myGrid.add(nightBtn, 0, 0);
  }
}

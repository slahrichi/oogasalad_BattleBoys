package oogasalad.view.screens;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import oogasalad.view.maker.LabelMaker;

/**
 * This class represents a temporary screen that is shown for a certain amount of time right when
 * the game starts, indicating to the players that it is time to place their Pieces on their
 * boards.
 *
 * @author Edison Ooi
 */
public class SetUpShipsScreen extends AbstractScreen {

  private static final String SETUP_SHIPS_RESOURCE = "SetupShipsMessage";
  private static final double SPACING = 20;
  private static final String SETUP_SHIPS_SCREEN_ID = "setup-ships-screen";
  private static final String SETUP_SHIPS_LABEL_ID = "setup-ships-screen-label";

  /**
   * Class constructor.
   */
  public SetUpShipsScreen(ResourceBundle resourceBundle) {
    super(SPACING, Pos.CENTER, null, resourceBundle);

    // Remove the button reference from children nodes
    getChildren().remove(getChildren().size() - 1);

    setId(SETUP_SHIPS_SCREEN_ID);
  }

  /**
   * Creates a configured Label object to represent the main label on the screen
   *
   * @return new Label object
   */
  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(myResources.getString(SETUP_SHIPS_RESOURCE), SETUP_SHIPS_LABEL_ID);
  }

  /**
   * Creates a configured Button object to represent the main button on the screen. For this screen
   * it is just a dummy button.
   *
   * @param handler buttonHandler event handler for main button
   * @return new Button object
   */
  @Override
  protected Button createMainButton(EventHandler<ActionEvent> handler) {
    return new Button();
  }
}

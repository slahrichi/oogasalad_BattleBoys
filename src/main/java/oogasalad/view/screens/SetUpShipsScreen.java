package oogasalad.view.screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import oogasalad.view.maker.LabelMaker;

public class SetUpShipsScreen extends AbstractScreen {

  private static final String SETUP_SHIPS_MESSAGE = "Get ready to set up your pieces!";

  /**
   * Class constructor.
   */
  public SetUpShipsScreen() {
    super(20, Pos.CENTER, null);

    // Remove the button reference from children nodes
    getChildren().remove(getChildren().size() - 1);

    setId("setup-ships-screen");
  }

  /**
   * Creates a configured Label object to represent the main label on the screen
   *
   * @return new Label object
   */
  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(SETUP_SHIPS_MESSAGE, "setup-ships-screen-label");
  }

  /**
   * Creates a configured Button object to represent the main button on the screen. For this screen it is just a dummy
   * button.
   *
   * @param handler buttonHandler event handler for main button
   * @return new Button object
   */
  @Override
  protected Button createMainButton(EventHandler<ActionEvent> handler) {
    return new Button();
  }
}

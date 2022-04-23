package oogasalad.view.screens;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import oogasalad.view.maker.ButtonMaker;
import oogasalad.view.maker.LabelMaker;

/**
 * This class represents a temporary screen that is shown when a player loses the game. Upon clicking the main button,
 * the game exits.
 *
 * @author Edison Ooi and Eric Xie
 */

public class LoserScreen extends AbstractScreen {

  private static final String LOSE_MESSAGE = " lost the game!";

  public LoserScreen(ResourceBundle resourceBundle, String loserName) {
    super(20, Pos.CENTER, null, resourceBundle);

    // Remove the button reference from children nodes
    getChildren().remove(getChildren().size() - 1);
    setId("loser-screen");
    mainLabel.setText(loserName + LOSE_MESSAGE);
  }

  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(LOSE_MESSAGE, "loser-screen-label");
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

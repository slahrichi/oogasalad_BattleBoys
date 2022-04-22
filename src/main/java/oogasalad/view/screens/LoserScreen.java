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

public class LoserScreen extends AbstractScreen{

  private static final String LOSE_MESSAGE = " lost the game!";
  private static final String BUTTON_MESSAGE = "Continue";

  public LoserScreen(EventHandler<ActionEvent> buttonHandler, ResourceBundle resourceBundle, String loserName) {
    super(20, Pos.CENTER, buttonHandler, resourceBundle);

    setId("loser-screen");
    mainLabel.setText(loserName + LOSE_MESSAGE);

  }
  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(LOSE_MESSAGE, "loser-screen-label");
  }

  @Override
  protected Button createMainButton(EventHandler<ActionEvent> handler) {
    return ButtonMaker.makeTextButton("loser-screen-button", handler, BUTTON_MESSAGE);
  }
}

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
 * This class represents a view that tells the user to pass the computer to the next player. Its
 * purpose is to hide any board information from other players when multiple players are playing
 * on the same computer.
 */
public class PassComputerScreen extends AbstractScreen {

  private static final double SPACING = 20;

  private static final String PASS_MESSAGE_RESOURCE = "PassMessage";
  private static final String BUTTON_TEXT = "OK";

  private static final String PASS_COMPUTER_SCREEN_ID = "pass-computer-message-view";
  private static final String PASS_COMPUTER_LABEL_ID = "pass-computer-message-label";
  private static final String PASS_COMPUTER_BTN_ID = "pass-computer-message-button";

  /**
   * Class constructor.
   *
   * @param buttonHandler event handler for main button
   */
  public PassComputerScreen(EventHandler<ActionEvent> buttonHandler, ResourceBundle resourceBundle) {
    super(SPACING, Pos.CENTER, buttonHandler, resourceBundle);
    setId(PASS_COMPUTER_SCREEN_ID);
  }

  /**
   * Creates a configured Label object to represent the main label on the screen
   *
   * @return new Label object
   */
  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(myResources.getString(PASS_MESSAGE_RESOURCE), PASS_COMPUTER_LABEL_ID);
  }

  /**
   * Creates a configured Button object to represent the main button on the screen
   *
   * @param handler buttonHandler event handler for main button
   * @return new Button object
   */
  @Override
  protected Button createMainButton(EventHandler<ActionEvent> handler) {
    return ButtonMaker.makeTextButton(PASS_COMPUTER_BTN_ID, handler, BUTTON_TEXT);
  }

  /**
   * Changes the player name that appears on the pass message label.
   *
   * @param text player name
   */
  @Override
  public void setLabelText(String text) {
    mainLabel.setText(myResources.getString(PASS_MESSAGE_RESOURCE) + text);
  }

}

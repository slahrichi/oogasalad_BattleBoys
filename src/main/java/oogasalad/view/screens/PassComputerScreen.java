package oogasalad.view.screens;

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

  //TODO: Put these strings into resourcebundles
  private static final String PASS_MESSAGE = "Please pass the computer to ";
  private static final String BUTTON_TEXT = "OK";

  /**
   * Class constructor.
   *
   * @param buttonHandler event handler for main button
   */
  public PassComputerScreen(EventHandler<ActionEvent> buttonHandler) {
    super(20, Pos.CENTER, buttonHandler);
    setId("pass-computer-message-view");
  }

  /**
   * Creates a configured Label object to represent the main label on the screen
   *
   * @return new Label object
   */
  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(PASS_MESSAGE, "pass-computer-message-label");
  }

  /**
   * Creates a configured Button object to represent the main button on the screen
   *
   * @param handler buttonHandler event handler for main button
   * @return new Button object
   */
  @Override
  protected Button createMainButton(EventHandler<ActionEvent> handler) {
    return ButtonMaker.makeTextButton("pass-computer-message-button", handler, BUTTON_TEXT);
  }

  /**
   * Changes the player name that appears on the pass message label.
   *
   * @param text player name
   */
  @Override
  public void setLabelText(String text) {
    mainLabel.setText(PASS_MESSAGE + text);
  }

}

package oogasalad.view.screens;

import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import oogasalad.view.maker.ButtonMaker;
import oogasalad.view.maker.LabelMaker;

/**
 * This class represents a temporary screen that is shown when a player wins the game. Upon clicking
 * the main button, the game exits.
 *
 * @author Edison Ooi
 */
public class WinnerScreen extends AbstractScreen {

  private static final String WIN_MESSAGE_RESOURCE = "WinText";
  private static final String BUTTON_MESSAGE = "Main Menu";
  private static final String WINNER_SCREEN_ID = "winner-screen";
  private static final String WINNER_LABEL_ID = "winner-screen-label";
  private static final String WINNER_BTN_ID = "winner-screen-button";

  private static final double SPACING = 20;

  /**
   * Class constructor.
   *
   * @param winnerName name of winning player
   */
  public WinnerScreen(ResourceBundle resourceBundle, String winnerName, EventHandler<ActionEvent> handler) {
    super(SPACING, Pos.CENTER, handler, resourceBundle);

    setId(WINNER_SCREEN_ID);

    mainLabel.setText(winnerName + " " + myResources.getString(WIN_MESSAGE_RESOURCE));
  }

  /**
   * Creates a configured Label object to represent the main label on the screen
   *
   * @return new Label object
   */
  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(myResources.getString(WIN_MESSAGE_RESOURCE), WINNER_LABEL_ID);
  }

  /**
   * Creates a configured Button object to represent the main button on the screen
   *
   * @param handler buttonHandler event handler for main button
   * @return new Button object
   */
  @Override
  protected Button createMainButton(EventHandler<ActionEvent> handler) {
    return ButtonMaker.makeTextButton(WINNER_BTN_ID, handler, BUTTON_MESSAGE);
  }
}

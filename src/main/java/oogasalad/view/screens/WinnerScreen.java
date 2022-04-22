package oogasalad.view.screens;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import oogasalad.view.maker.ButtonMaker;
import oogasalad.view.maker.LabelMaker;

/**
 * This class represents a temporary screen that is shown when a player wins the game. Upon clicking the main button,
 * the game exits.
 *
 * @author Edison Ooi
 */
public class WinnerScreen extends AbstractScreen {

  private static final String WIN_MESSAGE = " won the game!";
  private static final String BUTTON_MESSAGE = "Exit";

  /**
   * Class constructor.
   *
   * @param winnerName name of winning player
   */
  public WinnerScreen(String winnerName) {
    super(20, Pos.CENTER, e -> {
      Platform.exit();
      System.exit(0);
    });

    setId("winner-screen");

    mainLabel.setText(winnerName + WIN_MESSAGE);
  }

  /**
   * Creates a configured Label object to represent the main label on the screen
   *
   * @return new Label object
   */
  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(WIN_MESSAGE, "winner-screen-label");
  }

  /**
   * Creates a configured Button object to represent the main button on the screen
   *
   * @param handler buttonHandler event handler for main button
   * @return new Button object
   */
  @Override
  protected Button createMainButton(EventHandler<ActionEvent> handler) {
    return ButtonMaker.makeTextButton("winner-screen-button", handler, BUTTON_MESSAGE);
  }
}

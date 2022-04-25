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

  private static final String LOSE_MESSAGE_RESOURCE = "LostText";
  private static final double SPACING = 20;

  private static final String LOSER_SCREEN_ID = "loser-screen";
  private static final String LOSER_LABEL_ID = "loser-screen-label";

  /**
   * The constructor for the LoserScreen object, displayed whenever a user loses
   *
   * @param resourceBundle, the resourceBundle passed to it containing the languages
   * @param loserName, the name of the user who's a big fat loser
   */

  public LoserScreen(ResourceBundle resourceBundle, String loserName) {
    super(SPACING, Pos.CENTER, null, resourceBundle);

    // Remove the button reference from children nodes
    getChildren().remove(getChildren().size() - 1);
    setId(LOSER_SCREEN_ID);
    mainLabel.setText(loserName + LOSE_MESSAGE_RESOURCE);
  }

  /**
   * Create main label for the screen
   * @return Label of the lose message
   */

  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(LOSE_MESSAGE_RESOURCE, LOSER_LABEL_ID);
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

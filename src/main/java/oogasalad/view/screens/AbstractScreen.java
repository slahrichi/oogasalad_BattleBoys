package oogasalad.view.screens;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * This class represents temporary screens that are display some sort of information to the player
 * until they dismiss it, similar to an alert. Examples include a screen telling the player to pass
 * the computer, a winner/loser screen, and a screen showing what moves an AI player has made.
 * <p>
 * The default implementation includes one label on top of one button, however Nodes can be added as
 * needed because AbstractScreens are VBoxes.
 *
 * @author Edison Ooi and Eric Xie
 */
public abstract class AbstractScreen extends VBox {

  // File path constants
  protected static final String DEFAULT_RESOURCE_PACKAGE = "/";
  protected static final String STYLESHEET = "stylesheets/screenStylesheet.css";

  protected static ResourceBundle myResources;

  // Default components
  protected Label mainLabel;
  protected Button mainButton;

  /**
   * Class constructor. Lays out the screen with one label and one button.
   *
   * @param spacing   Spacing between nodes on screen
   * @param alignment Alignment of nodes on screen
   * @param handler   On click handler for main button
   */
  public AbstractScreen(double spacing, Pos alignment, EventHandler<ActionEvent> handler,
      ResourceBundle resourceBundle) {

    myResources = resourceBundle;

    setSpacing(spacing);
    setAlignment(alignment);
    getStylesheets().add(DEFAULT_RESOURCE_PACKAGE + STYLESHEET);

    mainLabel = createMainLabel();
    getChildren().add(mainLabel);

    mainButton = createMainButton(handler);
    getChildren().add(mainButton);
  }

  /**
   * Creates a configured Label object to represent the main label on the screen
   *
   * @return new Label object
   */
  protected abstract Label createMainLabel();

  /**
   * Creates a configured Button object to represent the main button on the screen
   *
   * @param handler buttonHandler event handler for main button
   * @return new Button object
   */
  protected abstract Button createMainButton(EventHandler<ActionEvent> handler);

  /**
   * Sets the text of the main label on the screen in a customized manner. Allows subclasses to take
   * advantage of static and dynamic parts of the main label's text.
   *
   * @param text new text to appear on Label
   */
  public void setLabelText(String text) {
    mainLabel.setText(text);
  }

}

package oogasalad.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public abstract class AbstractScreen extends VBox {
  protected Label mainLabel;
  protected Button mainButton;

  /**
   * Class constructor. Lays out the screen with one label and one button.
   *
   * @param spacing Spacing between nodes on screen
   * @param alignment Alignment of nodes on screen
   * @param handler On click handler for main button
   */
  public AbstractScreen(double spacing, Pos alignment, EventHandler<ActionEvent> handler) {
    setSpacing(spacing);
    setAlignment(alignment);
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
   * Sets the text of the main label on the screen in a customized manner. Allows subclasses to take advantage of
   * static and dynamic parts of the main label's text.
   *
   * @param text new text to appear on Label
   */
  public abstract void setLabelText(String text);

}

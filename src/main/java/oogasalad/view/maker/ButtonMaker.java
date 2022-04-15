package oogasalad.view.maker;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class contains static methods to create new Button objects from various arguments. These methods help reduce the
 * amount of repetitive customization code needed in View classes.
 *
 * @author Edison Ooi
 */
public class ButtonMaker {

  /**
   * Creates a new Button with text.
   *
   * @param id id of this Button
   * @param handler EventHandler for setOnMouseClicked property of this Button
   * @param text Text to appear on this Button
   * @return new Button
   */
  public static Button makeTextButton(String id, EventHandler<ActionEvent> handler, String text){
    Button button = makeGenericButton(id, handler);
    button.setText(text);
    return button;
  }

  /**
   * Creates a new Button with an image.
   *
   * @param id id of this Button
   * @param handler EventHandler for setOnMouseClicked property of this Button
   * @param image file name of image to appear on this Button
   * @return new Button
   */
  public static Button makeImageButton(String id, EventHandler<ActionEvent> handler, String image, double width, double height) {
    Button button = makeGenericButton(id, handler);
    ImageView imageView = new ImageView(new Image(image));
    imageView.setFitWidth(width);
    imageView.setFitHeight(height);
    button.setGraphic(imageView);
    return button;
  }

  // This method initializes a new generic Button object with an ID and event handler
  private static Button makeGenericButton(String id, EventHandler<ActionEvent> handler) {
    Button button = new Button();
    button.setOnAction(handler);
    button.setId(id);
    return button;
  }

}

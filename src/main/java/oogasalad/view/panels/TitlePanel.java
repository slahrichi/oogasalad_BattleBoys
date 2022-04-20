package oogasalad.view.panels;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

/**
 * This class represents an HBox that serves as the title view of any given screen. It contains
 * a customizable ImageView and a Label.
 *
 * @author Eric Xie
 */
public class TitlePanel extends HBox {

  // Constants
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String TITLE_IMAGE = "images/battleshipLogo.png";
  private static final int IMAGE_DIMENSIONS = 100;
  private static final String TITLE_TEXT_ID = "title-text";

  // Main JavaFX components
  private ImageView titleImage;
  private Label titleText;

  /**
   * Class constructor.
   *
   * @param inputString initial title text to be displayed
   */
  public TitlePanel(String inputString){

    titleImage = new ImageView();
    titleText = new Label(inputString);
    titleText.setId(TITLE_TEXT_ID);

    setUpImage();

    this.getChildren().addAll(titleImage, titleText);
  }


  // sets up the image of the title
  private void setUpImage() {

    titleImage.setImage(
        new Image(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + TITLE_IMAGE).toString(),
            true));
    titleImage.setFitHeight(IMAGE_DIMENSIONS);
    titleImage.setFitWidth(IMAGE_DIMENSIONS);
  }

  /**
   * Changes the text inside the title Label of this TitlePanel.
   *
   * @param inputString updated text to display as the title
   */
  public void changeTitle(String inputString) {
    titleText.setText(inputString);
  }

  /**
   * @return text contained in title Label of this TitlePanel
   */
  public String getTitle() {
    return titleText.getText();
  }


}

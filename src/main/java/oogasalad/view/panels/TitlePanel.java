package oogasalad.view.panels;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class TitlePanel extends HBox {

  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String TITLE_IMAGE = "images/battleshipLogo.png";
  private static final int IMAGE_DIMENSIONS = 100;

  private ImageView titleImage;
  private Label titleText;

  public TitlePanel(String inputString){

    titleImage = new ImageView();
    titleText = new Label(inputString);
    titleText.setId("titleText");


    setUpImage();

    this.setId("titleBox");
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

  public void changeTitle(String inputString) {
    titleText.setText(inputString);
  }

  public String getTitle() {
    return titleText.getText();
  }


}

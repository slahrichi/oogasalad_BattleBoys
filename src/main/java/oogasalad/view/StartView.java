package oogasalad.view;

import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.PropertyObservable;
import oogasalad.view.maker.BoxMaker;
import oogasalad.view.maker.ButtonMaker;

public class StartView extends PropertyObservable {

  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "stylesheets/startStylesheet.css";
  private static final String TITLE_IMAGE = "images/battleshipTitle.png";

  // Values
  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final double TITLE_WIDTH = 600;
  private static final double TITLE_HEIGHT = 200;
  private static final double VBOX_SPACING = 50;

  // ID strings
  private static final String PANE_ID = "startPane";
  private static final String TITLE_BOX_ID = "titleBox";
  private static final String START_BTN_ID = "start-button";
  private static final String CREATE_BTN_ID = "create-button";
  private static final String BUTTON_BOX_ID = "create-button";

  // Operation Strings
  private static final String LOAD_FILE_OPERATION = "loadFile";
  private static final String CREATE_FILE_OPERATION = "createGame";

  // Resource Strings
  private static final String START_BTN_RESOURCE = "StartButton";
  private static final String CREATE_BTN_RESOURCE = "CreateButton";

  private Scene myScene;
  private BorderPane myPane;
  private ImageView myTitle;
  private ResourceBundle myResources;

  public StartView(ResourceBundle resourceBundle) {

    myPane = new BorderPane();
    myPane.setId(PANE_ID);
    myResources = resourceBundle;

    setUpTitle();
    setUpButtons();
  }

  public Scene createScene() {
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
    return myScene;
  }


  private void setUpTitle() {
    Image myImage = new Image(
        getClass().getResource(DEFAULT_RESOURCE_PACKAGE + TITLE_IMAGE).toString(), true);
    myTitle = new ImageView(myImage);
    myTitle.setPreserveRatio(true);
    myTitle.setFitHeight(TITLE_HEIGHT);
    myTitle.setFitWidth(TITLE_WIDTH);

    HBox titleBox = new HBox();
    titleBox.getChildren().add(myTitle);
    titleBox.setId(TITLE_BOX_ID);

    myPane.setTop(titleBox);
  }

  private void setUpButtons() {
    Button startBtn = ButtonMaker.makeTextButton(START_BTN_ID,
        e -> handleClicked(LOAD_FILE_OPERATION), myResources.getString(START_BTN_RESOURCE));
    Button createBtn = ButtonMaker.makeTextButton(CREATE_BTN_ID,
        e -> handleClicked(CREATE_FILE_OPERATION), myResources.getString(CREATE_BTN_RESOURCE));
    VBox buttonBox = BoxMaker.makeVBox(BUTTON_BOX_ID, VBOX_SPACING, Pos.CENTER, startBtn, createBtn);
    myPane.setCenter(buttonBox);
  }

  private void handleClicked(String operation) {
    notifyObserver(operation, null);
  }
}

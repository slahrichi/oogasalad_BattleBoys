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

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "stylesheets/startStylesheet.css";
  private static final String TITLE_IMAGE = "images/battleshipTitle.png";

  private Scene myScene;
  private BorderPane myPane;
  private ImageView myTitle;
  private ResourceBundle myResources;

  public StartView(ResourceBundle resourceBundle) {

    myPane = new BorderPane();
    myPane.setId("startPane");

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
    Image myImage = new Image(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + TITLE_IMAGE).toString(), true);
    myTitle = new ImageView(myImage);
    myTitle.setPreserveRatio(true);
    myTitle.setFitHeight(200);
    myTitle.setFitWidth(600);

    HBox titleBox = new HBox();
    titleBox.getChildren().add(myTitle);
    titleBox.setId("titleBox");

    myPane.setTop(titleBox);
  }

  private void setUpButtons() {
    Button startBtn = ButtonMaker.makeTextButton("start-button", e -> handleClicked("start"), myResources.getString("StartButton"));
    Button loadBtn = ButtonMaker.makeTextButton("load-button", e -> handleClicked("load"), myResources.getString("LoadButton"));
    Button createBtn = ButtonMaker.makeTextButton("create-button", e -> handleClicked("create"), myResources.getString("CreateButton"));
    VBox buttonBox = BoxMaker.makeVBox("buttonBox", 50, Pos.CENTER, startBtn, loadBtn, createBtn);
    myPane.setCenter(buttonBox);
  }
  private void handleClicked(String operation) {
    notifyObserver(operation, null);
  }
}

package oogasalad.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import oogasalad.view.maker.ButtonMaker;

public class StartView {

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "stylesheets/startStylesheet.css";
  private static final String TITLE_IMAGE = "images/battleshipTitle.png";

  private Scene myScene;
  private BorderPane myPane;
  private ImageView myTitle;

  public StartView(){
    myPane = new BorderPane();
    myPane.setId("startPane");

    setUpTitle();
    setUpButtons();
  }

  public Scene createScene(){
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
    return myScene;
  }


  private void setUpTitle(){
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

  private void setUpButtons(){
    //TODO: Add event handlers to these buttons
    Button startBtn = ButtonMaker.makeTextButton("start-button", null, "Start");
    Button loadBtn = ButtonMaker.makeTextButton("load-button", null, "Load");
    Button createBtn = ButtonMaker.makeTextButton("create-button", null, "Create");

    VBox buttonBox = new VBox();
    buttonBox.setSpacing(50);
    buttonBox.setId("buttonBox");
    buttonBox.getChildren().addAll(startBtn, loadBtn, createBtn);

    myPane.setCenter(buttonBox);
  }

  private void notifyObserver(){
    System.out.println("Minjun's job.");
  }




}

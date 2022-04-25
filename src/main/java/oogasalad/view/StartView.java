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

/**
 * Purpose: The class that encapsulates the functionality and GUI of the Start screen, containing the
 * buttons to get to open up a File Chooser to play a game as well as a button to create games
 *
 * Assumptions: We assume that this screen shows up after a language has been selected after running the program
 * for the first time. We also assume that this screen will appear again if the user wants to be directed back to the main menu.
 *
 * Dependencies: This GUI has dependencies on JavaFX elements and on makers such as the Button and BoxMaker.
 *
 * An example of this is used in the Game class, where it creates and instantiates the StartView object and creates a Scene
 * off of it to display on a stage.
 *
 * @author Eric Xie
 */

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

  /**
   * The constructor for the StartView object, the main menu of the program.
   *
   * Assumptions: We assume that the ResourceBundle that it's passed is correctly initialized to be the correct
   * one with the correct languages. We also assume that the StartView constructor is correctly used by the Game.
   *
   * Parameters: The ResourceBundle with the language that was selected at the start of the program
   *
   * An exception might be thrown inside StartView if the ResourceBundle is passed incorrectly or it's passed something besides that
   *
   * @param resourceBundle, the ResourceBundle selected at the start of the program
   */

  public StartView(ResourceBundle resourceBundle) {

    myPane = new BorderPane();
    myPane.setId(PANE_ID);
    myResources = resourceBundle;

    setUpTitle();
    setUpButtons();
  }

  /**
   * Purpose: Creates a scene of the StartView and returns it for a stage to be set to it and displayed
   *
   * Assumed to be used by the Game to create a scene to display
   *
   * @return Scene of the StartView
   */

  public Scene createScene() {
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
    return myScene;
  }

  // sets up the title of the Main Menu

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

  // sets up the buttons of the main menu

  private void setUpButtons() {
    Button startBtn = ButtonMaker.makeTextButton(START_BTN_ID,
        e -> handleClicked(LOAD_FILE_OPERATION), myResources.getString(START_BTN_RESOURCE));
    Button createBtn = ButtonMaker.makeTextButton(CREATE_BTN_ID,
        e -> handleClicked(CREATE_FILE_OPERATION), myResources.getString(CREATE_BTN_RESOURCE));
    VBox buttonBox = BoxMaker.makeVBox(BUTTON_BOX_ID, VBOX_SPACING, Pos.CENTER, startBtn, createBtn);
    myPane.setCenter(buttonBox);
  }

  // observer that handles the functions of buttons clicked using reflection in the Game class

  private void handleClicked(String operation) {
    notifyObserver(operation, null);
  }
}

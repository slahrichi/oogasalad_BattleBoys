package oogasalad.view;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import oogasalad.PropertyObservable;
import oogasalad.view.maker.ButtonMaker;

/**
 * Creates a window for the user to select their preferred language with the options being English,
 * Spanish, and French
 *
 * Assumed to be run at the start of the program
 *
 * Depends on the Game class to be created and shown with the getScene() method, also has dependencies
 * on multiple JavaFX elements as well as ButtonMaker
 *
 * You can use this by creating an object of LanguageView and using the getScene() method to obtain a Scene to display
 * on a stage just as the Game class does.
 *
 * @author Eric Xie and Minjun Kwak
 */

public class LanguageView extends PropertyObservable {


  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "stylesheets/languageStylesheet.css";
  private static final String DEFAULT_LANGUAGE = "English";
  private static final String PROMPT_TEXT = "PromptText";
  private static final String CONTINUE_TEXT = "ContinueText";
  private static final String LANGUAGE_RESOURCE_PATH = "/languages/";
  private static final List<String> LANGUAGE_CHOICES = Arrays.asList("English", "Spanish", "French");

  private static final int SCREEN_WIDTH = 1200;
  private static final int SCREEN_HEIGHT = 800;
  private static final double VBOX_SPACING = 40;
  private static final double FONT_SIZE = 28;
  private static final double SELECTOR_WIDTH = 150;
  private static final double SELECTOR_HEIGHT = 50;

  private static final String CONTINUE_BTN_ID = "language-continue-button";
  private static final String LANGUAGE_OPERATION = "languageSelected";

  private Scene myScene;
  private VBox sideBox;
  private Text myText;
  private ComboBox mySelector;
  private Button myContinueButton;
  private ResourceBundle myResources;

  /**
   * The constructor for the LanguageView object
   *
   * Assumed to be used in Game to create and instantiate a LanguageView as well as create a scene out of it
   * When the LanguageView is created, sets up all the GUI elements and its functionality connected to Game
   *
   */

  public LanguageView(){
    myResources = ResourceBundle.getBundle(LANGUAGE_RESOURCE_PATH + DEFAULT_LANGUAGE);
    setUpText();
    setUpSelector();
    setUpButton();
    setUpBox();

  }

  /**
   * Creates the Scene for the LanguageView GUI to be shown on the stage in the Game class
   *
   * @return LanguageView scene
   */


  public Scene getScene(){
    myScene = new Scene(sideBox, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
    return myScene;
  }

  // sets up the Box to hold the text, selector, and continue button

  private void setUpBox(){
    sideBox = new VBox();
    sideBox.getChildren().addAll(myText, mySelector, myContinueButton);
    sideBox.setSpacing(VBOX_SPACING);
    sideBox.setAlignment(Pos.CENTER);
  }

  // sets up the text prompting the user what to do

  private void setUpText(){
    myText = new Text(myResources.getString(PROMPT_TEXT));
    myText.setFont(new Font(FONT_SIZE));
  }

  // sets up the selector for the user to choose a language

  private void setUpSelector() {
    mySelector = new ComboBox();
    mySelector.setPrefSize(SELECTOR_WIDTH, SELECTOR_HEIGHT);
    mySelector.getItems().addAll(LANGUAGE_CHOICES);
    mySelector.getSelectionModel().selectFirst();
    mySelector.setOnAction(e -> switchLanguage());
  }

  // sets up the continue button for the program

  private void setUpButton(){
    myContinueButton = ButtonMaker.makeTextButton(CONTINUE_BTN_ID, e -> handleClicked(LANGUAGE_OPERATION), myResources.getString(CONTINUE_TEXT));
  }

  // handles what happens when a language is selected from the Combo box

  private void switchLanguage(){
    myResources = ResourceBundle.getBundle(LANGUAGE_RESOURCE_PATH + mySelector.getValue());
    myText.setText(myResources.getString(PROMPT_TEXT));
    myContinueButton.setText(myResources.getString(CONTINUE_TEXT));;
  }

  // used with the button observer for reflection in the Game

  private void handleClicked(String operation) {
    notifyObserver(operation, myResources);
  }


}

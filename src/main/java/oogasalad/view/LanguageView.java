package oogasalad.view;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import oogasalad.PropertyObservable;
import oogasalad.view.maker.ButtonMaker;

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


  public LanguageView(){
    myResources = ResourceBundle.getBundle(LANGUAGE_RESOURCE_PATH + DEFAULT_LANGUAGE);
    setUpText();
    setUpSelector();
    setUpButton();
    setUpBox();

  }


  public Scene getScene(){
    myScene = new Scene(sideBox, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
    return myScene;
  }


  private void setUpBox(){
    sideBox = new VBox();
    sideBox.getChildren().addAll(myText, mySelector, myContinueButton);
    sideBox.setSpacing(VBOX_SPACING);
    sideBox.setAlignment(Pos.CENTER);
  }

  private void setUpText(){
    myText = new Text(myResources.getString(PROMPT_TEXT));
    myText.setFont(new Font(FONT_SIZE));
  }

  private void setUpSelector() {
    mySelector = new ComboBox();
    mySelector.setPrefSize(SELECTOR_WIDTH, SELECTOR_HEIGHT);
    mySelector.getItems().addAll(LANGUAGE_CHOICES);
    mySelector.getSelectionModel().selectFirst();
    mySelector.setOnAction(e -> switchLanguage());
  }

  private void setUpButton(){
    myContinueButton = ButtonMaker.makeTextButton(CONTINUE_BTN_ID, e -> handleClicked(LANGUAGE_OPERATION), myResources.getString(CONTINUE_TEXT));
  }


  private void switchLanguage(){
    myResources = ResourceBundle.getBundle(LANGUAGE_RESOURCE_PATH + mySelector.getValue());
    myText.setText(myResources.getString(PROMPT_TEXT));
    myContinueButton.setText(myResources.getString(CONTINUE_TEXT));;
  }

  private void handleClicked(String operation) {
    notifyObserver(operation, myResources);
  }


}

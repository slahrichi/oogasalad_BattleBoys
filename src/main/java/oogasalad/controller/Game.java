package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import oogasalad.FilePicker;
import oogasalad.model.parsing.ParserData;
import oogasalad.model.parsing.Parser;
import oogasalad.PropertyObservable;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.Player;
import oogasalad.view.GameView;
import oogasalad.view.LanguageView;
import oogasalad.view.StartView;
import oogasalad.view.gamebuilder.GameSetupView;
import oogasalad.view.maker.DialogMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class creates the game from start to finish. It initializes the creation of a language
 * selecting display to be displayed at the beginning of the game, and provides the logic for moving
 * on to new aspects of the game with each button press/user interaction.
 *
 * @author Minjun Kwak
 */
public class Game extends PropertyObservable implements PropertyChangeListener {

  private static final Logger LOG = LogManager.getLogger(GameView.class);
  private static final String INVALID_METHOD = "Invalid method name given";
  private static final String GAME_ALERT_ID = "game-alert";

  private StartView myStart;
  private LanguageView myLanguage;
  private GameSetup setup;
  private Stage myStage;
  private FilePicker fileChooser;
  private Parser parser;
  private ResourceBundle myResources;
  private ParserData parserData;
  private GameData gameData;

  /**
   * Constructor for a Game object. It initializes a FilePicker for loading a file, the language
   * view to appear at the start of the program, and a parser for parsing the loaded file.
   *
   * @param stage the stage that is passed by the Main class to display the application
   */
  public Game(Stage stage) {
    myStage = stage;
    fileChooser = new FilePicker();
    myLanguage = new LanguageView();
    myLanguage.addObserver(this);
    parser = new Parser();
  }

  /**
   * Opens a file chooser so the user can select a file to load
   *
   * @return
   */
  public File chooseDataFile() {
    return fileChooser.getFile();
  }

  /**
   * Opens a view that allows users to select their language
   */
  public void selectLanguage() {
    myStage.setScene(myLanguage.getScene());
    myStage.show();
  }

  // Creates a start screen and displays it
  private void showStart() {
    myStart = new StartView(myResources);
    myStart.addObserver(this);
    myStage.setScene(myStart.createScene());
  }

  /**
   * The listener method of this BoardView that is called when the class it is observing notifies
   * this class
   *
   * @param evt the evt associated with the notification
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    try {
      Method m = this.getClass().getDeclaredMethod(evt.getPropertyName(), ResourceBundle.class);
      m.invoke(this, evt.getNewValue());
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
        NullPointerException e) {
      e.printStackTrace();
      throw new NullPointerException(INVALID_METHOD);
    }
  }

  // This method is called by reflection
  // Creates the view for the start of the game after placing ships, and makes the first AI player's
  // move if necessary
  private void startGame(ResourceBundle resourceBundle) {
    GameManager manager = new GameManager(gameData, myResources);
    myStage.setScene(manager.createScene());
    manager.makeFirstAIPlayersMove();
  }

  // This method is called by reflection
  // Parses the file chosen by the user, and launches the placing ships view if succeeded
  private void loadFile(ResourceBundle resourceBundle) {
    try {
      String path = chooseDataFile().getAbsolutePath();
      parserData = parser.parse(path);
      createGameData(parserData);
      setup = new GameSetup(gameData, myResources);
      setup.addObserver(this);
      myStage.setScene(setup.createScene());
    } catch (NullPointerException e) {
      e.printStackTrace();
      return;
    } catch (ParserException e) {
      showError(e.getMessage());
      return;
    }
  }

  // This method is called by reflection
  // Sets the properties file associated with the selected language, and then shows the Start screen
  private void languageSelected(ResourceBundle resourceBundle) {
    myResources = resourceBundle;
    showStart();
  }

  // Creates the data relevant to the game given the data that is parsed by the parser
  // It does this by creating a PlayerFactory to take the parser data and create Player objects
  private void createGameData(ParserData data) {
    PlayerFactoryRecord pr = PlayerFactory.initializePlayers(data.board(), data.players(),
        data.playerInventory(), data.gold(), data.decisionEngines(), data.winConditions());
    Map<Player, DecisionEngine> engineMap = pr.engineMap();
    gameData = new GameData(pr.playerList(), data.pieces(), data.board(), engineMap,
        data.winConditions(), data.cellStateColorMap(), data.weapons(), data.specialIslands(),
        data.powerUps(), data.playerInventory(), data.allUsables(), data.shotsPerTurn(),
        data.shipMovementRate(), data.gold());
  }

  // This method is called by reflection
  // Creates the game builder and shows it on the screen
  private void createGame(ResourceBundle resources) throws Exception {
    GameSetupView builder = new GameSetupView();
    builder.start(myStage);
  }

  // Shows an error to the user
  private void showError(String message) {
    Alert alert = DialogMaker.makeAlert(message, GAME_ALERT_ID);
    alert.showAndWait();
  }
}

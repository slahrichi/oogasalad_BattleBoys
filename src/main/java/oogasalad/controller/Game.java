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
import oogasalad.view.maker.DialogMaker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game extends PropertyObservable implements PropertyChangeListener {

  private static final Logger LOG = LogManager.getLogger(GameView.class);
  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String INVALID_METHOD = "Invalid method name given";


  private StartView myStart;
  private LanguageView myLanguage;
  private GameSetup setup;
  private Stage myStage;
  private FilePicker fileChooser;
  private Parser parser;
  private ResourceBundle myResources;
  private ParserData parserData;
  private GameData gameData;


  public Game(Stage stage) {
    myStage = stage;
    fileChooser = new FilePicker();
    myLanguage = new LanguageView();
    myLanguage.addObserver(this);
    parser = new Parser();
  }

  public File chooseDataFile() {
    return fileChooser.getFile();
  }

  public void selectLanguage() {
    myStage.setScene(myLanguage.getScene());
    myStage.show();
  }

  private void showStart() {
    myStart = new StartView(myResources);
    myStart.addObserver(this);
    myStage.setScene(myStart.createScene());
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    try {
      Method m = this.getClass().getDeclaredMethod(evt.getPropertyName(), ResourceBundle.class);
      m.invoke(this, evt.getNewValue());
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
        NullPointerException e) {
      throw new NullPointerException(INVALID_METHOD);
    }
  }

  private void startGame(ResourceBundle resourceBundle) {
    GameManager manager = new GameManager(gameData, myResources);
    myStage.setScene(manager.createScene());
    manager.makeFirstAIPlayersMove();
  }

  private void loadFile(ResourceBundle resourceBundle) {
    LOG.info("loadFile");
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

  private void languageSelected(ResourceBundle resourceBundle) {
    myResources = resourceBundle;
    showStart();
  }

  private void createGameData(ParserData data) {
    PlayerFactoryRecord pr = PlayerFactory.initializePlayers(data.board(), data.players(),
        data.playerInventory(), data.gold(), data.decisionEngines());
    Map<Player, DecisionEngine> engineMap = pr.engineMap();
    gameData = new GameData(pr.playerList(), data.pieces(), data.board(), engineMap,
        data.winConditions(), data.cellStateColorMap(), data.weapons(), data.specialIslands(),
        data.powerUps(), data.playerInventory(), data.allUsables(), data.shotsPerTurn(),
        data.shipMovementRate(), data.gold());
  }

  private void createGame() {
    LOG.info("Create");
  }

  private void showError(String message) {
    Alert alert = DialogMaker.makeAlert(message, "game-alert");
    alert.showAndWait();
  }
}

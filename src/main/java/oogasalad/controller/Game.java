package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import oogasalad.FilePicker;
import oogasalad.GameData;
import oogasalad.model.parsing.Parser;
import oogasalad.ParserData;
import oogasalad.PropertyObservable;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.WinConditions.LoseXShipsLossCondition;
import oogasalad.model.utilities.WinConditions.WinCondition;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.GameView;
import oogasalad.view.StartView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Game extends PropertyObservable implements PropertyChangeListener {

  private static final Logger LOG = LogManager.getLogger(GameView.class);
  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String INVALID_METHOD = "Invalid method name given";
  private static final String DEFAULT_LANGUAGE_PACKAGE = "languages/";
  private static final String LANGUAGE = "English";

  private StartView myStart;
  private GameSetup setup;
  private GameManager manager;
  private Stage myStage;
  private FilePicker fileChooser;
  private Parser parser;
  private GameData data;
  private ResourceBundle myResources;


  public Game(Stage stage) {
    myStage = stage;
    fileChooser = new FilePicker();
    myResources = ResourceBundle.getBundle(DEFAULT_LANGUAGE_PACKAGE + LANGUAGE);
    parser = new Parser();
    ParserData parserData;
    try {
      parserData = parser.parse("src/main/resources/ExampleDataFile.properties");
    } catch (ParserException e) {
      LOG.error(e);
      parserData = null;
    }

    PlayerFactoryRecord pr = PlayerFactory.initializePlayers(parserData.board(), parserData.players(),
        parserData.decisionEngines());
    Map<Player, DecisionEngine> engineMap = pr.engineMap();
    //testing win condition code
    List<WinCondition> dummyWinConditions = new ArrayList<WinCondition>();
    dummyWinConditions.add(new LoseXShipsLossCondition(2));

    myStart = new StartView(myResources);
    myStart.addObserver(this);
    data = new GameData(pr.playerList(), parserData.board(), parserData.pieces(), dummyWinConditions, engineMap);
    // GameManager should take in list of players and GameData
  }

  public File chooseDataFile() {
    return fileChooser.getFile();
  }

  public void showStart() {
    myStage.setScene(myStart.createScene());
    myStage.show();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    try {
      Method m = this.getClass().getDeclaredMethod(evt.getPropertyName());
      m.invoke(this);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
        NullPointerException e) {
      throw new NullPointerException(INVALID_METHOD);
    }
  }

  private void startGame() {
    GameManager manager = new GameManager(data, myResources);
    myStage.setScene(manager.createScene());
  }

  private void start() {
    LOG.info("Start");
    setup = new GameSetup(data, myResources);
    setup.addObserver(this);
    myStage.setScene(setup.createScene());
  }

  private void load() {
    File loadedFile = chooseDataFile();

    LOG.info("Load");
  }

  private void create() {
    LOG.info("Create");
  }
}

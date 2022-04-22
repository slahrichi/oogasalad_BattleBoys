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
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import oogasalad.FilePicker;
import oogasalad.GameData;
import oogasalad.model.parsing.Parser;
import oogasalad.PlayerData;
import oogasalad.PropertyObservable;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.winconditions.LoseXShipsLossCondition;
import oogasalad.model.utilities.winconditions.WinCondition;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.GameView;
import oogasalad.view.StartView;
import oogasalad.view.maker.DialogMaker;
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
  private List<String> stringPlayers;
  private ResourceBundle myResources;
  private GameData gameData;

  //TODO: Remove this variable, it's for testing only
  private List<Piece> pieceList;

  public Game(Stage stage) {
    myStage = stage;
    parser = new Parser();
    fileChooser = new FilePicker();
    myResources = ResourceBundle.getBundle(DEFAULT_LANGUAGE_PACKAGE + LANGUAGE);
    myStart = new StartView(myResources);
    myStart.addObserver(this);
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
    GameManager manager = new GameManager(gameData, myResources);
    myStage.setScene(manager.createScene());
  }

  private void loadFile() {
    LOG.info("loadFile");
    PlayerData playerData;
    try {
      playerData = parser.parse(chooseDataFile().getAbsolutePath());
      createGameData(playerData);
      setup = new GameSetup(gameData, myResources);
      setup.addObserver(this);
      myStage.setScene(setup.createScene());
    } catch (NullPointerException e) {
      return;
    }
    catch (ParserException e) {
      showError(e.getMessage());
      return;
    }
  }

  private void createGameData(PlayerData data) {
    stringPlayers = data.players();
    pieceList = data.pieces();
    CellState[][] notSoDummyBoard = data.board();

    PlayerFactoryRecord pr = PlayerFactory.initializePlayers(notSoDummyBoard, stringPlayers,
        data.decisionEngines());
    List<Player> players = pr.playerList();
    Map<Player, DecisionEngine> engineMap = pr.engineMap();
    //testing win condition code
    List<WinCondition> dummyWinConditions = new ArrayList<WinCondition>();
    dummyWinConditions.add(new LoseXShipsLossCondition(1));
    gameData = new GameData(players, notSoDummyBoard, pieceList, dummyWinConditions, engineMap);
  }

  private void createGame() {
    LOG.info("Create");
  }

  private void showError(String message) {
    Alert alert = DialogMaker.makeAlert(message, "game-alert");
    alert.showAndWait();
  }
}

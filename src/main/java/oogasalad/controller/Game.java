package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.stage.Stage;
import oogasalad.FilePicker;
import oogasalad.GameData;
import oogasalad.model.parsing.Parser;
import oogasalad.PlayerData;
import oogasalad.PropertyObservable;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.WinConditions.LoseXShipsLossCondition;
import oogasalad.model.utilities.WinConditions.WinCondition;
import oogasalad.model.utilities.tiles.enums.CellState;

public class Game extends PropertyObservable implements PropertyChangeListener {

  private static final String FILEPATH = "oogasalad.model.players.";

  private GameSetup setup;
  private GameManager manager;
  private Stage myStage;
  private FilePicker fileChooser;
  private Parser parser;
  private List<String> stringPlayers;
  private GameData data;

  //TODO: Remove this variable, it's for testing only
  private List<Piece> pieceList = new ArrayList<>();

  public Game(Stage stage) {
    myStage = stage;
    parser = new Parser();
    fileChooser = new FilePicker();
    PlayerData playerData;
    try {
      playerData = parser.parse("src/main/resources/ExampleDataFile.properties");
    } catch (ParserException e) {
      playerData = null;
    }

    stringPlayers = playerData.players();
    pieceList = playerData.pieces();
    CellState[][] notSoDummyBoard = playerData.board();

    PlayerFactory pf = new PlayerFactory(notSoDummyBoard);
    List<Player> players = pf.createPlayerList(stringPlayers);

    //testing win condition code
    List<WinCondition> dummyWinConditions = new ArrayList<WinCondition>();
    dummyWinConditions.add(new LoseXShipsLossCondition(1));


    data = new GameData(players, notSoDummyBoard, pieceList, dummyWinConditions);
    setup = new GameSetup(data);
    setup.addObserver(this);
    // GameManager should take in list of players and GameData
  }

  public File chooseDataFile() {
    return fileChooser.getFile();
  }

  public void showSetup() {
    myStage.setScene(setup.createScene());
    myStage.show();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    System.out.println("Start game");
    //TODO: Change this to an instance of GameManager
    GameManager manager = new GameManager(data);
    myStage.setScene(manager.createScene());
//    manager.updateShipsLeft(pieceList);
  }
}

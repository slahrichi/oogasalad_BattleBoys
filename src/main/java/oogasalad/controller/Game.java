package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.stage.Stage;
import oogasalad.FilePicker;
import oogasalad.GameData;
import oogasalad.Parser;
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.GameView;
import oogasalad.view.SetupView;

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
    PlayerData playerData = parser.parse("src/main/resources/ExampleDataFile.properties");

    stringPlayers = playerData.players();
    pieceList = playerData.pieces();
    CellState[][] notSoDummyBoard = playerData.board();

    List<Player> players = new ArrayList<>();
    for (int i = 0; i < stringPlayers.size(); i++) {
      players.add(createPlayer(stringPlayers.get(i), notSoDummyBoard, i));
    }
    data = new GameData(players, notSoDummyBoard, pieceList);
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

  private Player createPlayer(String playerType, CellState[][] board, int id) {
    Board b = new Board(board);
    Map<Integer, Board> enemyMap = createEnemyMap(b, id);
    Player p = null;
    try {
      p = (Player) Class.forName(FILEPATH + playerType).getConstructor(Board.class, int.class,
              Map.class)
          .newInstance(b, id, enemyMap);
    } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
        IllegalAccessException | NoSuchMethodException e) {
    }
    return p;
  }

  private Map<Integer, Board> createEnemyMap(Board b, int id) {
    Map<Integer, Board> boardMap = new TreeMap<>();
    for (int i = 0; i < stringPlayers.size(); i++) {
      if (i == id) continue;
      boardMap.put(i, b.copyOf());
    }
    return boardMap;
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

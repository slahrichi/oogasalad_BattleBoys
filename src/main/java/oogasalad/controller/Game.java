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

    CellState[][] dummyBoard = new CellState[][]{{CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER}};


    List<Coordinate> coordinateList = new ArrayList<>(Arrays.asList(new Coordinate(0, 1),
        new Coordinate(1, 0), new Coordinate(1, 1)));
    List<ShipCell> dummyShipCellList = new ArrayList<>();
    dummyShipCellList.add(new ShipCell(1, new Coordinate(0,1), 0, "0"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    StaticPiece dummyShip = new StaticPiece(dummyShipCellList, coordinateList, "0");

    List<Coordinate> coordinateList2 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
        new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 2)));
    List<ShipCell> dummyShipCellList2 = new ArrayList<>();
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0,0), 0, "0"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0,1), 0, "3"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0,2), 0, "4"));
    StaticPiece dummyShip2 = new StaticPiece(dummyShipCellList2, coordinateList2, "0");
//
//    List<Coordinate> coordinateList3 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
//        new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 0), new Coordinate(3, 0)));
//    List<ShipCell> dummyShipCellList3 = new ArrayList<>();
//    dummyShipCellList3.add(new ShipCell(1, new Coordinate(0,0), 0, "0"));
//    dummyShipCellList3.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
//    dummyShipCellList3.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
//    dummyShipCellList3.add(new ShipCell(1, new Coordinate(2,0), 0, "3"));
//    dummyShipCellList3.add(new ShipCell(1, new Coordinate(3,0), 0, "4"));
//    StaticPiece dummyShip3 = new StaticPiece(dummyShipCellList3, coordinateList3, "0");
//
//    List<Coordinate> coordinateList4 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
//        new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3), new Coordinate(0, 4)));
//    List<ShipCell> dummyShipCellList4 = new ArrayList<>();
//    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,0), 0, "0"));
//    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,1), 0, "1"));
//    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,2), 0, "2"));
//    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,3), 0, "3"));
//    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,4), 0, "4"));
//    StaticPiece dummyShip4 = new StaticPiece(dummyShipCellList4, coordinateList4, "0");



    pieceList.add(dummyShip);
    pieceList.add(dummyShip2);
//    pieceList.add(dummyShip3);
//    pieceList.add(dummyShip4);
    //PlayerData data = parser.parse(chooseDataFile());
    stringPlayers = List.of("HumanPlayer", "HumanPlayer");
    List<Player> players = new ArrayList<>();
    for (int i = 0; i < stringPlayers.size(); i++) {
      players.add(createPlayer(stringPlayers.get(i), dummyBoard, i));
    }
    data = new GameData(players, dummyBoard, pieceList);
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
    manager.updateShipsLeft(pieceList);
  }
}

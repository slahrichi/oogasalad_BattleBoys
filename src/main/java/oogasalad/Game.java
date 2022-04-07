package oogasalad;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.beans.property.Property;
import javafx.stage.Stage;
import oogasalad.model.GameManager;
import oogasalad.model.GameSetup;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.view.GameView;

public class Game extends PropertyObservable implements PropertyChangeListener {

  private GameSetup setup;
  private GameManager manager;
  private Stage myStage;
  private FilePicker fileChooser;
  private Parser parser;

  public Game(Stage stage) {
    myStage = stage;
    parser = new Parser();
    fileChooser = new FilePicker();

    int[][] dummyBoard = new int[][]{{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1},{1,1,1,1,1,1,1,1}};

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

    List<Coordinate> coordinateList3 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
        new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(2, 0), new Coordinate(3, 0)));
    List<ShipCell> dummyShipCellList3 = new ArrayList<>();
    dummyShipCellList3.add(new ShipCell(1, new Coordinate(0,0), 0, "0"));
    dummyShipCellList3.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList3.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    dummyShipCellList3.add(new ShipCell(1, new Coordinate(2,0), 0, "3"));
    dummyShipCellList3.add(new ShipCell(1, new Coordinate(3,0), 0, "4"));
    StaticPiece dummyShip3 = new StaticPiece(dummyShipCellList3, coordinateList3, "0");

    List<Coordinate> coordinateList4 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
        new Coordinate(0, 1), new Coordinate(0, 2), new Coordinate(0, 3), new Coordinate(0, 4)));
    List<ShipCell> dummyShipCellList4 = new ArrayList<>();
    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,0), 0, "0"));
    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,1), 0, "1"));
    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,2), 0, "2"));
    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,3), 0, "3"));
    dummyShipCellList4.add(new ShipCell(1, new Coordinate(0,4), 0, "4"));
    StaticPiece dummyShip4 = new StaticPiece(dummyShipCellList4, coordinateList4, "0");


    List<Piece> pieceList = new ArrayList<>();
    pieceList.add(dummyShip);
    pieceList.add(dummyShip2);
    pieceList.add(dummyShip3);
    pieceList.add(dummyShip4);
    //PlayerData data = parser.parse(chooseDataFile());
    PlayerData data = new PlayerData(List.of("HumanPlayer", "HumanPlayer", "HumanPlayer"), pieceList, dummyBoard);
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
    myStage.setScene((new GameView()).createScene());
  }
}

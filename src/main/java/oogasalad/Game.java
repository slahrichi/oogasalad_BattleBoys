package oogasalad;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Stage;
import oogasalad.model.GameSetup;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;

public class Game {

  private GameSetup setup;
  private Stage myStage;
  private FilePicker fileChooser;
  private Parser parser;

  public Game(Stage stage) {
    myStage = stage;
    parser = new Parser();
    fileChooser = new FilePicker();

    int[][] dummyBoard = new int[][]{{1,1,1,1},{1,1,1,1},{1,1,1,1},{1,1,1,1}};

    //Player dummyPlayer = new HumanPlayer(dummyBoard, 1);
    List<Player> playerList = new ArrayList<>();
    //playerList.add(dummyPlayer);

    List<ShipCell> dummyShipCellList = new ArrayList<>();
    dummyShipCellList.add(new ShipCell(new Coordinate(0,1), 0));
    dummyShipCellList.add(new ShipCell(new Coordinate(1,0), 0));
    dummyShipCellList.add(new ShipCell(new Coordinate(1,1), 0));
    StaticPiece dummyShip = new StaticPiece(dummyShipCellList);

    List<ShipCell> dummyShipCellList2 = new ArrayList<>();
    dummyShipCellList2.add(new ShipCell(new Coordinate(2,2), 0));
    dummyShipCellList2.add(new ShipCell(new Coordinate(2,3), 0));
    dummyShipCellList2.add(new ShipCell(new Coordinate(3,2), 0));
    StaticPiece dummyShip2 = new StaticPiece(dummyShipCellList);

    List<Piece> pieceList = new ArrayList<>();
    pieceList.add(dummyShip);
    pieceList.add(dummyShip2);
    //PlayerData data = parser.parse(chooseDataFile());
    PlayerData data = new PlayerData(List.of("HumanPlayer", "HumanPlayer"), pieceList, dummyBoard);
    setup = new GameSetup(data);
    // GameManager should take in list of players and GameData
  }

  public File chooseDataFile() {
    return fileChooser.getFile();
  }

  public void showSetup() {
    myStage.setScene(setup.createScene());
    myStage.show();
  }
}

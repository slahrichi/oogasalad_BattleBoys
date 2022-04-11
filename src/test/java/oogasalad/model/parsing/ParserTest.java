package oogasalad.model.parsing;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oogasalad.PlayerData;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParserTest {

  PlayerData examplePlayerData;
  Parser parser;
  String PROPERTIES_EXTENSION = "properties";

  @BeforeEach
  void setup() {
    parser = new Parser();
    List<String> players = Arrays.asList("HumanPlayer", "AIPlayer", "HumanPlayer");

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

    List<Piece> pieceList = Arrays.asList(dummyShip, dummyShip2);

    CellState[][] dummyBoard = new CellState[][]{{CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER}};

    examplePlayerData = new PlayerData(players, pieceList, dummyBoard);

  }


  @Test
  void loadCorrectFileExtension(){
    String validPath = "src/test/resources/Test.properties";
    String invalidPath = "src/test/resources/Test.matt";
    assertTrue(parser.checkExtension(validPath, PROPERTIES_EXTENSION));
    assertFalse(parser.checkExtension(invalidPath, PROPERTIES_EXTENSION));
  }

  @Test
  void loadAll() throws Exception {
    PlayerData generatedData = parser.parse("src/test/resources/Test.properties");
    assertEquals(examplePlayerData, generatedData);
  }

  @Test
  void loadBoard() throws Exception {
    PlayerData generatedData = parser.parse("src/test/resources/Test.properties");
    assertTrue(Arrays.deepEquals(examplePlayerData.board(), generatedData.board()));
  }

  @Test
  void loadPlayers() throws Exception {
    PlayerData generatedData = parser.parse("src/test/resources/Test.properties");
    assertEquals(examplePlayerData.players(), generatedData.players());
  }

  @Test
  void loadPieces() throws Exception {
    PlayerData generatedData = parser.parse("src/test/resources/Test.properties");
    assertEquals(examplePlayerData.pieces(), generatedData.pieces());
  }

  @Test
  void loadPathToPropertiesFile() {
    Exception exception = assertThrows(FileNotFoundException.class, () -> parser.parse("obviously/bad/path.properties"));
    System.out.println(exception.getMessage());
  }

  @Test
  void saveAll() {

  }

}

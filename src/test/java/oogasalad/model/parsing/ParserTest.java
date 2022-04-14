package oogasalad.model.parsing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import oogasalad.PlayerData;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParserTest {

  PlayerData examplePlayerData;
  Parser parser;
  String PROPERTIES_EXTENSION = "properties";
  private final String DOT = ".";
  //private final ResourceBundle exceptionMessages = ResourceBundle.getBundle("src/test/resources/ParserExceptions.properties");
  Properties exceptionMessageProperties;

  @BeforeEach
  void setup() {
    exceptionMessageProperties = new Properties();

    try {
      InputStream is = new FileInputStream("src/main/resources/ParserExceptions.properties");
      exceptionMessageProperties.load(is);
      is.close();
    } catch (IOException ignored) {
    }
    
    
    parser = new Parser();
    List<String> players = Arrays.asList("HumanPlayer", "AIPlayer", "HumanPlayer");
    List<String> decisionEngines = Arrays.asList("None", "Easy", "None");

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

    examplePlayerData = new PlayerData(players, pieceList, dummyBoard, decisionEngines);

  }

  @Test
  void loadCorrectFileExtension(){
    String validPath = "src/test/resources/Test.properties";
    String invalidPath = "src/test/resources/Test.matt";
    try {
      parser.checkExtension(validPath, PROPERTIES_EXTENSION);
    } catch(Exception e) {
      fail(String.format("Exception thrown: %s",e.getMessage()));
    }
    assertThrows(Exception.class, () -> parser.checkExtension(invalidPath, PROPERTIES_EXTENSION));
  }

  @Test
  void loadAll() {
    try {
      PlayerData generatedData = parser.parse("src/test/resources/Test.properties");
      assertEquals(examplePlayerData, generatedData);
    } catch(Exception e) {
      fail(String.format("Exception thrown: %s",e.getMessage()));
    }
  }

  @Test
  void loadBoard()  {
    try {
      PlayerData generatedData = parser.parse("src/test/resources/Test.properties");
      assertTrue(Arrays.deepEquals(examplePlayerData.board(), generatedData.board()));
    } catch(Exception e) {
      fail(String.format("Exception thrown: %s",e.getMessage()));
    }
  }

  @Test
  void loadPlayers()  {
    try {
      PlayerData generatedData = parser.parse("src/test/resources/Test.properties");
      assertEquals(examplePlayerData.players(), generatedData.players());
    } catch(Exception e) {
      fail(String.format("Exception thrown: %s",e.getMessage()));
    }
  }

  @Test
  void loadPieces()  {
    try {
      PlayerData generatedData = parser.parse("src/test/resources/Test.properties");
      assertEquals(examplePlayerData.pieces(), generatedData.pieces());
    } catch(Exception e) {
      fail(String.format("Exception thrown: %s",e.getMessage()));
    }
  }

  @Test
  void loadBadPathToPropertiesFile() {
    String path = "obviously/bad/path.properties";
    ParserException thrown = assertThrows(ParserException.class, () -> parser.parse(path));
    assertEquals(exceptionMessageProperties.getProperty("badPath").formatted("properties",path), thrown.getMessage());
  }

  @Test
  void loadBadExtension() {
    String path = "src/test/resources/Test.xml";
    assertThrows(ParserException.class, () -> parser.parse(path));
  }

  @Test
  void loadBadPropertiesFile() {
    String path = "src/test/resources/BadPlayers.properties";
    ParserException thrown = assertThrows(ParserException.class, () -> parser.parse(path));
    assertEquals(exceptionMessageProperties.getProperty("badPlayer").formatted("PlayerThatObviouslyDoesNotExist"), thrown.getMessage());
  }

  @Test
  void loadMismatchedPropertiesFile() {
    assertThrows(Exception.class, () -> parser.parse("src/test/resources/Mismatched.properties"));
  }

  @Test
  void loadInvalidBoard() {
    String path = "src/test/resources/InvalidBoard.properties";
    ParserException thrown = assertThrows(ParserException.class, () -> parser.parse(path));
    assertEquals(exceptionMessageProperties.getProperty("jsonError").formatted(path), thrown.getMessage());
  }

  @Test
  void loadInvalidPieces() {
    String path = "src/test/resources/InvalidPieces.properties";
    ParserException thrown = assertThrows(ParserException.class, () -> parser.parse(path));
    assertEquals(exceptionMessageProperties.getProperty("jsonError").formatted(path), thrown.getMessage());
  }

  @Test
  void loadBoardWithMissingData() {
    String path = "src/test/resources/BoardWithMissingData.properties";
    String jsonPath = "src/test/resources/BoardWithMissingData.json";
    ParserException thrown = assertThrows(ParserException.class, () -> parser.parse(path));
    assertEquals(exceptionMessageProperties.getProperty("missingData").formatted(jsonPath,"Board"), thrown.getMessage());
  }

  @Test
  void loadPiecesWithMissingData() {
    String path = "src/test/resources/PiecesWithMissingData.properties";
    ParserException thrown = assertThrows(ParserException.class, () -> parser.parse(path));
    assertEquals(exceptionMessageProperties.getProperty("missingData").formatted(path,"Pieces"), thrown.getMessage());
  }

  @Test
  void notEnoughKeys() {
    String path = "src/test/resources/MissingBoardFile.properties";
    ParserException thrown = assertThrows(ParserException.class, () -> parser.parse(path));
    assertEquals(exceptionMessageProperties.getProperty("missingArg").formatted("BoardFile"), thrown.getMessage());
  }

  @Test
  void basicSave() {
    String path = "src/test/resources/BasicSave.properties";
    try {
      parser.save(examplePlayerData, path);
      PlayerData check = parser.parse(path);
      assertEquals(examplePlayerData, check);
    } catch (ParserException e) {
      fail(e.getMessage());
    }
  }

}

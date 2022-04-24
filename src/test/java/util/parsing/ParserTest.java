package util.parsing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javafx.scene.paint.Color;
import oogasalad.model.parsing.Parser;
import oogasalad.model.parsing.ParserData;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Item;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.WaterCell;
import oogasalad.model.utilities.tiles.enums.CellState;

import static oogasalad.model.utilities.tiles.enums.CellState.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.weapons.BasicShot;
import oogasalad.model.utilities.usables.weapons.BurnShot;
import oogasalad.model.utilities.usables.weapons.Weapon;
import oogasalad.model.utilities.winconditions.HitXCellsCondition;
import oogasalad.model.utilities.winconditions.WinCondition;
import oogasalad.model.utilities.winconditions.WinState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ParserTest {

  ParserData exampleParserData;
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

    exampleParserData = new ParserData(
        makePlayers(),
        makePieceList(),
        makeDummyBoard(),
        makeDecisionEngines(),
        makeWinConditions(),
        makeColorMap(),
        makeWeapons(),
        makeIslandCells(),
        makePowerups(),
        makeInventory(),
        makeUsables(),
        4,
        3,
        2);
    try {
      parser.save(exampleParserData, "data/recentlyGeneratedData.properties");
    } catch(ParserException e) {
      System.out.println(e.getMessage());
    }

    try {
      ParserData generatedData = parser.parse("data/recentlyGeneratedData.properties");
      assertEquals(exampleParserData, generatedData);
    } catch (ParserException e) {
      e.printStackTrace();
    }


  }

  private List<Usable> makeUsables() {
    List<Usable> usables = new ArrayList<>();
    usables.add(new BasicShot());
    return usables;
  }

  private Map<String,Integer> makeInventory() {
    Map<String,Integer> map = new HashMap<>();
    map.put("hey",4);
    return map;
  }

  private List<Item> makePowerups() {
    List<Item> powerups = new ArrayList<>();
    powerups.add(new Item(4));
    powerups.add(new Item(3));
    return powerups;
  }

  private List<IslandCell> makeIslandCells() {
    List<IslandCell> arr = new ArrayList<>();
    arr.add(new IslandCell(new Coordinate(0,0), 5));
    arr.add(new IslandCell(new Coordinate(1,1), 7));
    return arr;
  }

  private List<Weapon> makeWeapons() {
    List<Weapon> arr = new ArrayList<>();
    arr.add(new BasicShot());
    return arr;
  }

  private Map<CellState, Color> makeColorMap() {
    Map<CellState, Color> map = new HashMap<>();
    map.put(NOT_DEFINED, Color.WHITE);
    map.put(WATER, Color.BLUE);
    map.put(WATER_HIT, Color.WHITE);
    map.put(SHIP_HEALTHY, Color.GRAY);
    map.put(SHIP_DAMAGED, Color.BLANCHEDALMOND);
    map.put(SHIP_SUNKEN, Color.AQUA);
    map.put(SHIP_HOVER, Color.ORANGE);
    map.put(ISLAND_HEALTHY, Color.BROWN);
    map.put(ISLAND_DAMAGED, Color.AQUAMARINE);
    map.put(ISLAND_SUNK, Color.CORAL);
    map.put(SCANNED, Color.DEEPPINK);
    return map;
  }

  private List<Piece> makePieceList() {
    List<Piece> pieceList = Arrays.asList(makeDummyPiece(), makeDummyPiece2());
    return pieceList;
  }

  private List<String> makeDecisionEngines() {
    List<String> decisionEngines = Arrays.asList("None", "Easy", "None");
    return decisionEngines;
  }

  private List<String> makePlayers() {
    List<String> players = Arrays.asList("HumanPlayer", "AIPlayer", "HumanPlayer");
    return players;
  }

  private CellState[][] makeDummyBoard() {
    CellState[][] dummyBoard = new CellState[][]{{CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER}};
    return dummyBoard;
  }

  private Piece makeDummyPiece2() {
    List<Coordinate> coordinateList2 = new ArrayList<>(Arrays.asList(new Coordinate(0, 0),
        new Coordinate(1, 0), new Coordinate(1, 1), new Coordinate(0, 1), new Coordinate(0, 2)));
    List<ShipCell> dummyShipCellList2 = new ArrayList<>();
    //dummyShipCellList2.add(new WaterCell(new Coordinate(1,0)));

    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0,0), 0, "0"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0,1), 0, "3"));
    dummyShipCellList2.add(new ShipCell(1, new Coordinate(0,2), 0, "4"));


    Piece dummyShip2 = new StaticPiece(dummyShipCellList2, coordinateList2, "0");
    return dummyShip2;
  }

  private StaticPiece makeDummyPiece() {
    List<Coordinate> coordinateList = new ArrayList<>(Arrays.asList(new Coordinate(0, 1),
        new Coordinate(1, 0), new Coordinate(1, 1)));
    List<ShipCell> dummyShipCellList = new ArrayList<>();
    dummyShipCellList.add(new ShipCell(1, new Coordinate(0,1), 0, "0"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,0), 0, "1"));
    dummyShipCellList.add(new ShipCell(1, new Coordinate(1,1), 0, "2"));
    StaticPiece dummyShip = new StaticPiece(dummyShipCellList, coordinateList, "0");
    return dummyShip;
  }

  private List<WinCondition> makeWinConditions() {
    Piece staticPiece = makeDummyPiece();
    CellState[][] testBoard = makeDummyBoard();
    Board b = new Board(testBoard);
    Player testPlayer = new HumanPlayer(b, 0, new HashMap<String, Integer>(), new HashMap<Integer, MarkerBoard>());
    testPlayer.placePiece(staticPiece, new Coordinate(0,0));
    WinCondition testCondition = new HitXCellsCondition(CellState.WATER_HIT, 2, WinState.WIN);
    return Arrays.asList(testCondition);
  }


  /*
  @Test
  void playerDataReflection() throws ParserException{
    List<Object> parsedElements = List.of(exampleParserData.players(),
        exampleParserData.pieces(), exampleParserData.board(), exampleParserData.decisionEngines());
    ParserData returnedPlayerData = parser.getPlayerData(parsedElements);
    assertEquals(exampleParserData, returnedPlayerData);
  }

   */


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
      ParserData generatedData = parser.parse("data/recentlyGeneratedData.properties");
      assertEquals(exampleParserData, generatedData);
    } catch(Exception e) {
      fail(String.format("Exception thrown: %s",e.getMessage()));
    }
  }

  @Test
  void loadBoard()  {
    try {
      ParserData generatedData = parser.parse("src/test/resources/Test.properties");
      assertTrue(Arrays.deepEquals(exampleParserData.board(), generatedData.board()));
    } catch(Exception e) {
      fail(String.format("Exception thrown: %s",e.getMessage()));
    }
  }

  @Test
  void loadPlayers()  {
    try {
      ParserData generatedData = parser.parse("src/test/resources/Test.properties");
      assertEquals(exampleParserData.players(), generatedData.players());
    } catch(Exception e) {
      fail(String.format("Exception thrown: %s",e.getMessage()));
    }
  }

  @Test
  void loadPieces()  {
    try {
      ParserData generatedData = parser.parse("src/test/resources/Test.properties");
      assertEquals(exampleParserData.pieces(), generatedData.pieces());
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
    String jsonPath = "src/test/resources/PiecesWithMissingData.json";
    ParserException thrown = assertThrows(ParserException.class, () -> parser.parse(path));
    assertEquals(exceptionMessageProperties.getProperty("missingData").formatted(jsonPath,"Pieces"), thrown.getMessage());
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
      parser.save(exampleParserData, path);
      ParserData check = parser.parse(path);
      assertEquals(exampleParserData, check);
    } catch (ParserException e) {
      fail(e.getMessage());
    }
  }

}

package oogasalad.model.parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import oogasalad.PlayerData;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.Usables.Weapons.Weapon;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;


public class Parser {


  public Parser() {
    exceptionMessageProperties = new Properties();
    try {
      InputStream is = new FileInputStream("src/main/resources/ParserExceptions.properties");
      exceptionMessageProperties.load(is);
      is.close();
    } catch (IOException ignored) {
    }
  }

  private final String PROPERTIES_PLAYER_LIST = "Players";
  private final String PROPERTIES_PIECES_FILE = "PiecesFile";
  private final String PROPERTIES_BOARD_FILE = "BoardFile";
  private final String PROPERTIES_WEAPONS_FILE = "WeaponsFile";
  private final String PROPERTIES_ISLANDS_FILE = "IslandsFile";
  private final String PROPERTIES_DECISION_ENGINES_LIST = "DecisionEngines";
  private final String HUMAN_PLAYER = "HumanPlayer";
  private final String AI_PLAYER = "AIPlayer";
  private final List<String> REQUIRED_ARGS = List.of(PROPERTIES_PLAYER_LIST, PROPERTIES_PIECES_FILE, PROPERTIES_BOARD_FILE, PROPERTIES_DECISION_ENGINES_LIST);
  private final String MISSING_ARG = "missingArg";
  private final String DOT = ".";
  private final String PROPERTIES_EXTENSION = "properties";
  private final String JSON_EXTENSION = "json";
  private final List<String> jsonPaths = List.of("PiecesFile", "BoardFile");
  private Properties exceptionMessageProperties;
  private final String HUMAN_DECISION_ENGINE = "None";

  public void save(PlayerData data, String pathToNewFile) throws ParserException  {

    File file = new File(pathToNewFile);
    Properties props = new Properties();
    savePlayers(props, data.players());
    saveDecisionEngines(props, data.decisionEngines());
    String nameOfNewFile = file.toString().replaceFirst("[.][^.]+$", "");
    String nameOfBoardFile = nameOfNewFile + "Board.json";
    String nameOfPiecesFile = nameOfNewFile + "Pieces.json";
    String nameOfWeaponsFile = nameOfNewFile + "Weapons.json";
    String nameOfIslandsFile = nameOfNewFile + "Islands.json";
    saveBoard(props, data.board(), nameOfBoardFile);
    savePieces(props, data.pieces(), nameOfPiecesFile);
    saveWeapons(props, data.weapons(), nameOfWeaponsFile);
    saveIslands(props, data.island(), nameOfIslandsFile);
    try {
      FileOutputStream outputStream = new FileOutputStream(file);
      props.store(outputStream, "generated via save");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  /**
   * @param pathToFile
   * @param expectedExtension
   * @throws Exception
   */
  public void checkExtension(String pathToFile, String expectedExtension) throws ParserException {
    String passedFileExtension = pathToFile.substring(pathToFile.lastIndexOf(DOT) + 1);
    if (!passedFileExtension.equals(expectedExtension)) {
        throw new ParserException(exceptionMessageProperties.getProperty(expectedExtension).formatted(passedFileExtension));
      }
  }

  private void checkProperties(Properties props) throws ParserException {
    for (String key: REQUIRED_ARGS){
      if(props.getProperty(key) == null) {
        throw new ParserException(exceptionMessageProperties.getProperty(MISSING_ARG).formatted(key));
      }
    }
  }

  
  // add method to check if saving works fine (e.g. the player's saved data is not null)
  /**
   *
   * @param pathToFile properties file
   * @return a parser Player Data
   * @throws FileNotFoundException
   */
  public PlayerData parse(String pathToFile) throws ParserException {
    File file = new File(pathToFile);
    Properties props = new Properties();
    try {
      InputStream is = new FileInputStream(file);
      props.load(is);
      is.close();
    } catch (IOException e) {
      if (e instanceof FileNotFoundException) {
        throw new ParserException(exceptionMessageProperties.getProperty("badPath").formatted("properties",file.toString()));
      }
      e.printStackTrace();
    }

    checkExtension(pathToFile, PROPERTIES_EXTENSION);
    checkProperties(props);
    for (String path:jsonPaths) {
      checkExtension(props.getProperty(path), JSON_EXTENSION);
    }


    List<String> players;
    CellState[][] cellBoard;
    List<Piece> pieces;
    List<String> decisionEngines;
    List<Weapon> weapons;
    List<IslandCell> islands;
    try {
      players = loadPlayers(props);
      cellBoard = loadBoard(props);
      pieces = loadPieces(props);
      decisionEngines = loadDecisionEngines(props, players);
      weapons = loadWeapons(props);
      islands = loadIslands(props);

    } catch (JsonSyntaxException e) {
      throw new ParserException(exceptionMessageProperties.getProperty("jsonError").formatted(pathToFile));
    }

    return new PlayerData(players, pieces, cellBoard, decisionEngines, weapons, islands);

  }

  private List<String> loadDecisionEngines(Properties props, List<String> players)
      throws ParserException {
    List<String> decisionEngines = List.of(
        props.getProperty(PROPERTIES_DECISION_ENGINES_LIST).split(" "));
    if(decisionEngines.size() != players.size()) {
      throw new ParserException(exceptionMessageProperties.getProperty("misalignedEngines").formatted(players.size(), decisionEngines.size()));
    }
    for(int i = 0; i < decisionEngines.size(); i++) {
      String player = players.get(i);
      String engine = decisionEngines.get(i);
      if (player.equals(HUMAN_PLAYER)) {
        if (!engine.equals(HUMAN_DECISION_ENGINE)) {
          throw new ParserException(exceptionMessageProperties.getProperty("HumanPlayerAIEngine"));
        }
      } else if (player.equals(AI_PLAYER)) {
        try {
          Class.forName("oogasalad.model.players." + engine + "DecisionEngine");
        } catch (ClassNotFoundException e) {
          throw new ParserException(
              exceptionMessageProperties.getProperty("AIPlayerBadEngine").formatted(engine));
        }
      }

    }
    return decisionEngines;
  }

  private List<String> loadPlayers(Properties props) throws ParserException {
    String[] playersData = props.getProperty(PROPERTIES_PLAYER_LIST).split(" ");
    for(String player: playersData) {
      try {
        Class.forName("oogasalad.model.players." + player);
      } catch (ClassNotFoundException e) {
        throw new ParserException(exceptionMessageProperties.getProperty("badPlayer").formatted(player));
      }
    }
    return new ArrayList<>(Arrays.asList(playersData));
  }

  private CellState[][] loadBoard(Properties props) throws ParserException {
    String boardFile = props.getProperty(PROPERTIES_BOARD_FILE);
    Gson gson = new GsonBuilder().create();
    CellState[][] boardData;
    try {
      boardData = gson.fromJson(new FileReader(boardFile), CellState[][].class);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
    checkAlignedBoard(boardData, boardFile);
    return boardData;
  }

  private void checkAlignedBoard(CellState[][] board, String path) throws ParserException {
    int assumedLength = board[0].length;
    for(int i = 1; i < board.length; i++) {
      if(board[i].length != assumedLength) {
        throw new ParserException(exceptionMessageProperties.getProperty("missingData").formatted(path,"Board"));
      }
    }
  }

  private List<Piece> loadPieces(Properties props) {
    String piecesFile = props.getProperty(PROPERTIES_PIECES_FILE);
    Gson gson = new GsonBuilder().registerTypeAdapter(Piece.class, new GSONHelper()).create();

    Type listOfMyClassObject = new TypeToken<ArrayList<Piece>>() {}.getType();
    List<Piece> ret = null;
    try {
      ret = gson.fromJson(new FileReader(piecesFile), listOfMyClassObject);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return ret;
  }


  private List<Weapon> loadWeapons(Properties props) {
    String weaponsFile = props.getProperty(PROPERTIES_WEAPONS_FILE);
    Gson gson = new GsonBuilder().registerTypeAdapter(Weapon.class, new GSONHelper()).create();
    Type listOfMyClassObject = new TypeToken<ArrayList<Weapon>>() {
    }.getType();
    List<Weapon> ret = null;
    try {
      ret = gson.fromJson(new FileReader(weaponsFile), listOfMyClassObject);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return ret;
  }

  private List<IslandCell> loadIslands(Properties props){
    String islandFile = props.getProperty(PROPERTIES_ISLANDS_FILE);
    Gson gson = new GsonBuilder().registerTypeAdapter(IslandCell.class, new GSONHelper()).create();
    Type listOfMyClassObject = new TypeToken<ArrayList<IslandCell>>() {
    }.getType();
    List<IslandCell> ret = null;
    try {
      ret = gson.fromJson(new FileReader(islandFile), listOfMyClassObject);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return ret;
  }


  private void savePlayers(Properties props, List<String> players) {
    props.put(PROPERTIES_PLAYER_LIST, String.join(" ", players));
  }

  private void saveDecisionEngines(Properties props, List<String> decisionEngines) {
    props.put(PROPERTIES_DECISION_ENGINES_LIST, String.join(" ", decisionEngines));
  }

// draft method to replace saveWeapons, saveIslands, and savePieces
  // same must be done to load()
//  private void saveSomething(Properties props, String type, String location) throws ClassNotFoundException{
//    Gson gson = new GsonBuilder().setPrettyPrinting().
//        registerTypeHierarchyAdapter(Class.forName(type), new GSONHelper()).
//        create();
//    String json = gson.toJson(type);
//    File myNewFile = new File(location);
//    try {
//      if (myNewFile.createNewFile()) { //new file created
//        FileWriter myWriter = new FileWriter(myNewFile);
//        myWriter.write(json);
//        myWriter.close();
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//    props.put(type, myNewFile.toString());
//  }

  private void saveWeapons(Properties props, List<Weapon> weapons, String location){
    Gson gson = new GsonBuilder().setPrettyPrinting().
        registerTypeHierarchyAdapter(Weapon.class, new GSONHelper()).
        create();
    String json = gson.toJson(weapons);
    File myNewFile = new File(location);
    try {
      if (myNewFile.createNewFile()) { //new file created
        FileWriter myWriter = new FileWriter(myNewFile);
        myWriter.write(json);
        myWriter.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    props.put(PROPERTIES_WEAPONS_FILE, myNewFile.toString());
  }

  private void saveIslands(Properties props, List<IslandCell> islands, String location) {
    Gson gson = new GsonBuilder().setPrettyPrinting().
        registerTypeHierarchyAdapter(IslandCell.class, new GSONHelper()).
        create();
    String json = gson.toJson(islands);
    File myNewFile = new File(location);
    try {
      if (myNewFile.createNewFile()) { //new file created
        FileWriter myWriter = new FileWriter(myNewFile);
        myWriter.write(json);
        myWriter.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    props.put(PROPERTIES_ISLANDS_FILE, myNewFile.toString());
  }

  private void saveBoard(Properties props, CellState[][] board, String location) {
    //make json for board
    //put link to json in props file
    Gson gson = new GsonBuilder().setPrettyPrinting().
        create();
    String json = gson.toJson(board);
    File myNewFile = new File(location);
    try {
      FileWriter myWriter = new FileWriter(myNewFile);
      myWriter.write(json);
      myWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    props.put(PROPERTIES_BOARD_FILE, myNewFile.toString());
  }

  private void savePieces(Properties props, List<Piece> pieces, String location) {
    Gson gson = new GsonBuilder().setPrettyPrinting().
        registerTypeHierarchyAdapter(Piece.class, new GSONHelper()).
        create();

    String json = gson.toJson(pieces);

    File myNewFile = new File(location);
    try {
      if (myNewFile.createNewFile()) { //new file created
        FileWriter myWriter = new FileWriter(myNewFile);
        myWriter.write(json);
        myWriter.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    props.put(PROPERTIES_PIECES_FILE, myNewFile.toString());
  }


}

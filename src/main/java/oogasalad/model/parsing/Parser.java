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
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import oogasalad.PlayerData;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;


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
  private final String PROPERTIES_DECISION_ENGINES_LIST = "DecisionEngines";
  private final String HUMAN_PLAYER = "HumanPlayer";
  private final String AI_PLAYER = "AIPlayer";
  private final String PLAYERDATA_PATH = "oogasalad.PlayerData.java";
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
    saveBoard(props, data.board(), nameOfBoardFile);
    savePieces(props, data.pieces(), nameOfPiecesFile);
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

    List<ParsedElement> parsers = new ArrayList<>();
    parsers.add(new ParseBoard()); //this is the only line that needs to be changed



    List<Object> parsedElements = new ArrayList<>();
    try {
      for(ParsedElement p: parsers) {
        parsedElements.add(p.parse(props));
      }
    } catch (JsonSyntaxException e) {
      throw new ParserException(exceptionMessageProperties.getProperty("jsonError").formatted(pathToFile));
    }
    return getPlayerData(parsedElements);
    // return new PlayerData(players, pieces, cellBoard, decisionEngines);


  }

  PlayerData getPlayerData(List<Object> parsedElements) throws ParserException {
    try {
      Class<?> clazz = Class.forName(PLAYERDATA_PATH);
      Constructor<?> cons = clazz.getConstructor();
      Object[] obj = parsedElements.toArray();
      Object newInstance = cons.newInstance(obj); //this Object could instead be a Command object
      return (PlayerData) newInstance;
    }
    catch (Exception e){
      // this catches any error while trying to use reflection
      throw new ParserException(exceptionMessageProperties.getProperty("reflectionError"));
    }
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





  private void savePlayers(Properties props, List<String> players) {
    props.put(PROPERTIES_PLAYER_LIST, String.join(" ", players));
  }

  private void saveDecisionEngines(Properties props, List<String> decisionEngines) {
    props.put(PROPERTIES_DECISION_ENGINES_LIST, String.join(" ", decisionEngines));
  }





}

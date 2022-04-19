package oogasalad.model.parsing;

import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import oogasalad.PlayerData;


public class Parser {

  private List<ParsedElement> parsers;

  public Parser() {
    makeParsers();
    exceptionMessageProperties = new Properties();
    try {
      InputStream is = new FileInputStream("src/main/resources/ParserExceptions.properties");
      exceptionMessageProperties.load(is);
      is.close();
    } catch (IOException ignored) {
    }
  }

  private void makeParsers() {
    parsers = PlayerData.makeParsers();
  }

  private final String PROPERTIES_PLAYER_LIST = "Players";
  private final String PROPERTIES_PIECES_FILE = "PiecesFile";
  private final String PROPERTIES_BOARD_FILE = "BoardFile";
  private final String PROPERTIES_DECISION_ENGINES_LIST = "DecisionEngines";
  private final List<String> REQUIRED_ARGS = List.of(PROPERTIES_PLAYER_LIST, PROPERTIES_PIECES_FILE, PROPERTIES_BOARD_FILE, PROPERTIES_DECISION_ENGINES_LIST);
  private final String MISSING_ARG = "missingArg";
  private final String DOT = ".";
  private final String PROPERTIES_EXTENSION = "properties";
  private final String JSON_EXTENSION = "json";
  private final String JSON_ERROR = "jsonError";
  private final String BAD_PATH = "badPath";
  private final String REGEX = "[.][^.]+$";

  private final String EMPTY = "";
  private final String SAVING_COMMENT = "generated via save";
  private final String SAVING_ERROR = "savingError";
  private final String PROPERTIES = "properties";
  private final List<String> jsonPaths = List.of("PiecesFile", "BoardFile");
  private Properties exceptionMessageProperties;

  public void save(PlayerData data, String pathToNewFile) throws ParserException  {

    File file = new File(pathToNewFile);
    Properties props = new Properties();

    String nameOfNewFile = file.toString().replaceFirst(REGEX, EMPTY);
    List<Object> elementsOfPlayerData = PlayerData.getItems(data);

    for(int i = 0; i < parsers.size(); i++) {
      ParsedElement parser = parsers.get(i);
      parser.save(props, nameOfNewFile, elementsOfPlayerData.get(i));
    }
    try {
      FileOutputStream outputStream = new FileOutputStream(file);
      props.store(outputStream, SAVING_COMMENT);
    } catch (IOException e) {
      throw new ParserException(exceptionMessageProperties.getProperty(SAVING_ERROR));
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
    Properties props = getProperties(pathToFile);
    List<Object> parsedElements = new ArrayList<>();
    try {
      for(ParsedElement p: parsers) {
        parsedElements.add(p.parse(props));
      }
    } catch (JsonSyntaxException e) {
      throw new ParserException(exceptionMessageProperties.getProperty(JSON_ERROR).formatted(pathToFile));
    }
    return PlayerData.make(parsedElements);
  }

  private Properties getProperties(String pathToFile) throws ParserException {
    File file = new File(pathToFile);
    Properties props = new Properties();
    try {
      InputStream is = new FileInputStream(file);
      props.load(is);
      is.close();
    } catch (IOException e) {
      if (e instanceof FileNotFoundException) {
        throw new ParserException(exceptionMessageProperties.getProperty(BAD_PATH).formatted(PROPERTIES,file.toString()));
      }
      e.printStackTrace();
    }
    sanityCheck(pathToFile, props);
    return props;
  }

  private void sanityCheck(String pathToFile, Properties props) throws ParserException {
    checkExtension(pathToFile, PROPERTIES_EXTENSION);
    checkProperties(props);
    for (String path:jsonPaths) {
      checkExtension(props.getProperty(path), JSON_EXTENSION);
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

}


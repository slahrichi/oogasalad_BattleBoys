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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Parser class defines an interface between the game design stage and the game player stage.
 * We use a file storage interface.
 * During the game design stage, files are created using the save method.
 * During the game player stage, files are loaded using the parse method.
 */
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
    parsers = ParserData.makeParsers();
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
  private static final Logger LOG = LogManager.getLogger(Parser.class);

  /**
   *
   * @param data: the ParserData to save
   * @param pathToNewFile: the location to save the file at
   * @throws ParserException: when there is an issue with ParserData
   */
  public void save(ParserData data, String pathToNewFile) throws ParserException  {

    LOG.info(String.format("Saving ParserData to %s", pathToNewFile));
    File file = new File(pathToNewFile);
    Properties props = new Properties();

    String nameOfNewFile = file.toString().replaceFirst(REGEX, EMPTY);
    List<Object> elementsOfPlayerData = ParserData.getItems(data);

    for(int i = 0; i < parsers.size(); i++) {
      ParsedElement parser = parsers.get(i);
      parser.save(props, nameOfNewFile, elementsOfPlayerData.get(i));
    }
    try {
      FileOutputStream outputStream = new FileOutputStream(file);
      props.store(outputStream, SAVING_COMMENT);
    } catch (IOException e) {
      String message = exceptionMessageProperties.getProperty(SAVING_ERROR);
      LOG.warn(message);
      throw new ParserException(message);
    }
  }

  /**
   *
   * @param pathToFile: the path to a save file to parse
   * @return: a ParserData corresponding to the save file
   * @throws ParserException: when there is an issue with the save file
   */
  public ParserData parse(String pathToFile) throws ParserException {
    LOG.info(String.format("Parsing from %s", pathToFile));
    Properties props = getProperties(pathToFile);
    List<Object> parsedElements = new ArrayList<>();
    try {
      for(ParsedElement p: parsers) {
        parsedElements.add(p.parse(props));
      }
    } catch (JsonSyntaxException e) {
      String message = exceptionMessageProperties.getProperty(JSON_ERROR).formatted(pathToFile);
      LOG.warn(message);
      throw new ParserException(message);
    }
    return ParserData.make(parsedElements);
  }

  //returns a Properties object given a path to a Properties file
  private Properties getProperties(String pathToFile) throws ParserException {
    File file = new File(pathToFile);
    Properties props = new Properties();
    try {
      InputStream is = new FileInputStream(file);
      props.load(is);
      is.close();
    } catch (IOException e) {
      if (e instanceof FileNotFoundException) {
        String message = exceptionMessageProperties.getProperty(BAD_PATH).formatted(PROPERTIES,file.toString());
        LOG.warn(message);
        throw new ParserException(message);
      }
      e.printStackTrace();
    }
    checkPropertyFile(pathToFile, props);
    return props;
  }

  //runs multiple checks on properties file
  private void checkPropertyFile(String pathToFile, Properties props) throws ParserException {
    checkExtension(pathToFile, PROPERTIES_EXTENSION);
    checkProperties(props);
    for (String path:jsonPaths) {
      checkExtension(props.getProperty(path), JSON_EXTENSION);
    }
  }

  //ensures a path has a certain extension, throws ParserException if not
  private void checkExtension(String pathToFile, String expectedExtension) throws ParserException {
    String passedFileExtension = pathToFile.substring(pathToFile.lastIndexOf(DOT) + 1);
    if (!passedFileExtension.equals(expectedExtension)) {
      String message = exceptionMessageProperties.getProperty(expectedExtension).formatted(passedFileExtension);
      LOG.warn(message);
      throw new ParserException(message);
    }
  }

  //checks that a properties file has all required components, throws ParserException if not
  private void checkProperties(Properties props) throws ParserException {
    for (String key: REQUIRED_ARGS){
      if(props.getProperty(key) == null) {
        String message = exceptionMessageProperties.getProperty(MISSING_ARG).formatted(key);
        LOG.warn(message);
        throw new ParserException(message);
      }
    }
  }

}


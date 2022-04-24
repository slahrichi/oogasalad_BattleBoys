package oogasalad.model.parsing.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import oogasalad.model.parsing.GSONHelper;
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.utilities.winconditions.WinCondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseWinConditions extends ParsedElement {
  private final String PROPERTIES_WINCONDITIONS_FILE = "WinConditionsFile";
  private final String WINCONDITIONS = "WinConditions";
  private final String WINCONDITIONS_JSON = "WinConditions.json";
  private static final Logger LOG = LogManager.getLogger(ParseWinConditions.class);


  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += WINCONDITIONS_JSON;
    LOG.info("saving Win Conditions at {}",location);
    List<WinCondition> winConditionsList = (List<WinCondition>) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().
        registerTypeHierarchyAdapter(WinCondition.class, new GSONHelper()).
        create();
    putJsonInProp(props, location, winConditionsList, gson, PROPERTIES_WINCONDITIONS_FILE);
  }

  @Override
  public List<WinCondition> parse(Properties props) throws ParserException {
    String winConditionsFile = props.getProperty(PROPERTIES_WINCONDITIONS_FILE);
    LOG.info("parsing Win Conditions at {}",winConditionsFile);
    Gson gson = new GsonBuilder().registerTypeAdapter(WinCondition.class, new GSONHelper()).create();
    Type listOfMyClassObject = new TypeToken<ArrayList<WinCondition>>() {}.getType();
    return (List<WinCondition>) getParsedObject(winConditionsFile, gson, listOfMyClassObject, WINCONDITIONS);
  }

  @Override
  public Class getParsedClass() {
    return List.class;
  }
}

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
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.winconditions.WinCondition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseSpecialIslands extends ParsedElement {

  private static final Logger LOG = LogManager.getLogger(ParseSpecialIslands.class);
  private final String PROPERTIES_SPECIAL_ISLANDS_FILE = "SpecialIslandsFile";
  private final String SPECIAL_ISLANDS = "SpecialIslands";
  private final String SPECIAL_ISLANDS_JSON = "SpecialIslands.json";

  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += SPECIAL_ISLANDS_JSON;
    LOG.info("saving Special Islands at {}", location);
    List<Cell> specialIslands = (List<Cell>) o;
    Gson gson = new GsonBuilder()
        .registerTypeHierarchyAdapter(Cell.class, new GSONHelper())
        .setPrettyPrinting().create();
    String json = gson.toJson(specialIslands);
    putJsonInProp(props, location, json, PROPERTIES_SPECIAL_ISLANDS_FILE);
  }

  @Override
  public List<Cell> parse(Properties props) throws ParserException {
    String specialIslandsFile = props.getProperty(PROPERTIES_SPECIAL_ISLANDS_FILE);
    LOG.info("parsing Special Islands at {}", specialIslandsFile);
    Gson gson = new GsonBuilder().registerTypeAdapter(Cell.class, new GSONHelper()).create();
    Type listOfMyClassObject = new TypeToken<ArrayList<IslandCell>>() {}.getType();
    return (List<Cell>) getParsedObject(specialIslandsFile, gson, listOfMyClassObject, SPECIAL_ISLANDS);

  }

  @Override
  public Class getParsedClass() {
    return List.class;
  }
}

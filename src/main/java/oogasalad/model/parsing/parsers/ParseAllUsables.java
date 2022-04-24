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
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.items.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseAllUsables extends ParsedElement {

  private static final Logger LOG = LogManager.getLogger(ParseAllUsables.class);
  private final String PROPERTIES_ALL_USABLES_FILE = "AllUsablesFile";
  private final String ALL_USABLES = "AllUsables";
  private final String ALL_USABLES_JSON = "AllUsables.json";


  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += ALL_USABLES_JSON;
    LOG.info("saving all usables at {}", location);
    List<Usable> allUsables = (List<Usable>) o;
    Gson gson = new GsonBuilder().
        registerTypeHierarchyAdapter(Usable.class, new GSONHelper())
        .setPrettyPrinting().create();
    String json = gson.toJson(allUsables);
    putJsonInProp(props, location, json, PROPERTIES_ALL_USABLES_FILE);
  }

  @Override
  public List<Usable> parse(Properties props) throws ParserException {
    String allUsablesFile = props.getProperty(PROPERTIES_ALL_USABLES_FILE);
    LOG.info("parsing all usables at {}", allUsablesFile);
    Gson gson = new GsonBuilder().registerTypeAdapter(Usable.class, new GSONHelper()).create();
    Type listOfMyClassObject = new TypeToken<ArrayList<Usable>>() {}.getType();
    return (List<Usable>) getParsedObject(allUsablesFile, gson, listOfMyClassObject, ALL_USABLES);
  }

  @Override
  public Class getParsedClass() {
    return List.class;
  }
}

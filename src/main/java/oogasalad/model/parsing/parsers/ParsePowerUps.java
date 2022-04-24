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
import oogasalad.model.utilities.usables.items.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParsePowerUps extends ParsedElement {

  private static final Logger LOG = LogManager.getLogger(ParsePowerUps.class);
  private final String PROPERTIES_POWERUPS_FILE = "PowerUpsFile";
  private final String POWERUPS = "PowerUps";
  private final String POWERUPS_JSON = "PowerUps.json";

  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += POWERUPS_JSON;
    LOG.info("saving powerups at {}", location);
    List<Item> powerUps = (List<Item>) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    putJsonInProp(props, location, powerUps, gson, PROPERTIES_POWERUPS_FILE);
  }


  @Override
  public List<Item> parse(Properties props) throws ParserException {
    String powerUpsFile = props.getProperty(PROPERTIES_POWERUPS_FILE);
    LOG.info("parsing powerups at {}", powerUpsFile);
    Gson gson = new GsonBuilder().registerTypeAdapter(Item.class, new GSONHelper()).create();
    Type listOfMyClassObject = new TypeToken<ArrayList<Item>>() {}.getType();
    return (List<Item>) getParsedObject(powerUpsFile, gson, listOfMyClassObject, POWERUPS);
  }

  @Override
  public Class getParsedClass() {
    return List.class;
  }
}

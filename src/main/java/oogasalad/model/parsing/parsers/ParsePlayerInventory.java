package oogasalad.model.parsing.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;
import java.util.Properties;
import javafx.scene.paint.Color;
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParsePlayerInventory extends ParsedElement {

  private final String PLAYER_INVENTORY_JSON = "PlayerInventory.json";
  private final String PROPERTIES_PLAYER_INVENTORY_FILE = "PlayerInventory";
  private static final Logger LOG = LogManager.getLogger(ParsePlayerInventory.class);


  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += PLAYER_INVENTORY_JSON;
    LOG.info("saving player inventory at {}", location);
    Map playerInventory = (Map<String, Integer>) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(playerInventory);
    putJsonInProp(props, location, json, PROPERTIES_PLAYER_INVENTORY_FILE);
  }


  @Override
  public Map<String, Integer> parse(Properties props) throws ParserException {
    String playerInventoryFile = props.getProperty(PROPERTIES_PLAYER_INVENTORY_FILE);
    LOG.info("parsing player inventory at {}", playerInventoryFile);
    Gson gson = new GsonBuilder().create();
    Map playerInventory;
    playerInventory = (Map<String, Integer>) getElementFromJson(playerInventoryFile, gson, getParsedClass());
    return playerInventory;
  }

  @Override
  public Class getParsedClass() {
    return Map.class;
  }
}

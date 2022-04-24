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
import oogasalad.model.utilities.usables.weapons.Weapon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseWeapons extends ParsedElement {

  private static final Logger LOG = LogManager.getLogger(ParseWeapons.class);

  private final String WEAPONS_JSON = "Weapons.json";
  private final String PROPERTIES_WEAPONS_FILE = "WeaponsFile";
  private final String WEAPONS = "Weapons";

  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += WEAPONS_JSON;
    LOG.info("saving Weapons at {}",location);
    List<Weapon> weapons = (List<Weapon>) o;
    Gson gson =  new GsonBuilder().setPrettyPrinting().registerTypeHierarchyAdapter(
        Weapon.class, new GSONHelper()).create();
    String json = gson.toJson(weapons);
    putJsonInProp(props, location, json, PROPERTIES_WEAPONS_FILE);
  }

  @Override
  public List<Weapon> parse(Properties props) throws ParserException {
    String weaponsFile = props.getProperty(PROPERTIES_WEAPONS_FILE);
    LOG.info("parsing Weapons at {}",weaponsFile);
    Gson gson = new GsonBuilder().registerTypeAdapter(Weapon.class, new GSONHelper()).create();
    Type listOfMyClassObject = new TypeToken<ArrayList<Weapon>>() {}.getType();
    return (List<Weapon>) getParsedObject(weaponsFile, gson, listOfMyClassObject, WEAPONS);
  }

  @Override
  public Class getParsedClass() {
    return List.class;
  }
}

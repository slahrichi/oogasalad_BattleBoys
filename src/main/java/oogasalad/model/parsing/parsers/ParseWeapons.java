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

public class ParseWeapons extends ParsedElement {

  private final String WEAPONS_JSON = "Weapons.json";
  private final String PROPERTIES_WEAPONS_FILE = "WeaponsFile";
  private final String WEAPONS = "Weapons";

  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += WEAPONS_JSON;
    List<Weapon> weapons = (List<Weapon>) o;
    Gson gson =  new GsonBuilder().setPrettyPrinting().registerTypeHierarchyAdapter(
        Weapon.class, new GSONHelper()).create();
    putJsonInProp(props, location, weapons, gson, PROPERTIES_WEAPONS_FILE);
  }

  @Override
  public Object parse(Properties props) throws ParserException {
    String weaponsFile = props.getProperty(PROPERTIES_WEAPONS_FILE);
    Gson gson = new GsonBuilder().registerTypeAdapter(Weapon.class, new GSONHelper()).create();
    Type listOfMyClassObject = new TypeToken<ArrayList<Weapon>>() {}.getType();
    return (List<Weapon>) getParsedObject(weaponsFile, gson, listOfMyClassObject, WEAPONS);
  }

  @Override
  public Class getParsedClass() {
    return List.class;
  }
}

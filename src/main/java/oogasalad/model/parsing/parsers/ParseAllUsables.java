package oogasalad.model.parsing.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import oogasalad.model.parsing.GSONHelper;
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.items.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseAllUsables extends ParsedElement {

  private static final Logger LOG = LogManager.getLogger(ParseAllUsables.class);
  private final String PROPERTIES_ALL_USABLES_FILE = "AllUsablesFile";
  private final String PROPERTIES_ALL_USABLES_FILE_MAPS = "AllUsablesMapsFile";
  private final String ALL_USABLES = "AllUsables";
  private final String ALL_USABLES_JSON = "AllUsables.json";
  private final String ALL_USABLES_JSON_MAPS = "AllUsablesMaps.json";


  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    LOG.info("saving all usables at {}", location + ALL_USABLES_JSON);
    List<Usable> allUsables = (List<Usable>) o;
    List<SerializedUsable> serializedUsables = serializeAllMaps(allUsables);
    for(Usable u: allUsables) {
      u.setRelativeCoordShots(new HashMap<>());
    }

    //original stuff
    Gson gson = new GsonBuilder().
        registerTypeHierarchyAdapter(Usable.class, new GSONHelper()).enableComplexMapKeySerialization()
        .setPrettyPrinting().create();
    String json = gson.toJson(allUsables);
    putJsonInProp(props, location + ALL_USABLES_JSON, json, PROPERTIES_ALL_USABLES_FILE);

    //maps
    String json2 = gson.toJson(serializedUsables);
    putJsonInProp(props, location + ALL_USABLES_JSON_MAPS, json2, PROPERTIES_ALL_USABLES_FILE_MAPS);
  }

  @Override
  public List<Usable> parse(Properties props) throws ParserException {
    String allUsablesFile = props.getProperty(PROPERTIES_ALL_USABLES_FILE);
    String allUsablesMapsFile = props.getProperty(PROPERTIES_ALL_USABLES_FILE_MAPS);
    LOG.info("parsing all usables at {}", allUsablesFile);
    Gson gson = new GsonBuilder().enableComplexMapKeySerialization().
        registerTypeAdapter(Usable.class, new GSONHelper()).create();
    Type listOfMyClassObject = new TypeToken<ArrayList<Usable>>() {}.getType();
    Type listOfMyClassObjectMaps = new TypeToken<ArrayList<SerializedUsable>>() {}.getType();

    List<SerializedUsable> mapUsables = (List<SerializedUsable>) getParsedObject(allUsablesMapsFile, gson, listOfMyClassObjectMaps, ALL_USABLES);
    List<Usable> almostCompleteUsables = (List<Usable>) getParsedObject(allUsablesFile, gson, listOfMyClassObject, ALL_USABLES);

    return reconcile(almostCompleteUsables, mapUsables);
  }

  @Override
  public Class getParsedClass() {
    return List.class;
  }

  public List<Usable> reconcile(List<Usable> usables, List<SerializedUsable> serializedUsables) {
    for(int i = 0; i < usables.size(); i++) {
      usables.get(i).setRelativeCoordShots(deserializeSingleMap(serializedUsables.get(i).relativeCoordShots));
    }
    return usables;
  }

  public List<SerializedUsable> serializeAllMaps(List<Usable> allUsables) {
    List<SerializedUsable> ret = new ArrayList<>();
    for(int i = 0; i < allUsables.size(); i++) {
      Map<Coordinate, Integer> oldMap = allUsables.get(i).getRelativeCoordShots();
      Map<String, String> serializedMap = serializeSingleMap(oldMap);
      SerializedUsable usableToAdd = new SerializedUsable(allUsables.get(i), serializedMap);
      ret.add(usableToAdd);
    }
    return ret;
  }



  public Map<String, String> serializeSingleMap(Map<Coordinate, Integer> oldMap) {
    Map<String, String> ret = new HashMap<>();
    for(Coordinate c: oldMap.keySet()) {
      String coord = c.getRow() + " " + c.getColumn();
      ret.put(coord, oldMap.get(c).toString());
    }
    return ret;
  }

  public Map<Coordinate, Integer> deserializeSingleMap(Map<String,String> oldMap) {
    Map<Coordinate, Integer> ret = new HashMap<>();
    for(String s: oldMap.keySet()) {
      String[] values = s.split(" ");
      Coordinate c = new Coordinate(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
      ret.put(c, Integer.parseInt(oldMap.get(s)));
    }
    return ret;
  }

  public class SerializedUsable {

    String myID;
    int goldCost;
    Map<String,String> relativeCoordShots;
    public SerializedUsable(Usable old, Map<String,String> newMap) {
      myID = old.getMyID();
      goldCost = old.getPrice();
      relativeCoordShots = newMap;
    }

    public String getMyID() { return myID;}
    public int getPrice() { return goldCost;}
    public Map<String,String> getMap() { return relativeCoordShots;}

  }
}

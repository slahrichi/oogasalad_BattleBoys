package oogasalad.model.parsing.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javafx.scene.paint.Color;
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseCellColors extends ParsedElement {

  private final String CELL_COLORS_JSON = "CellColors.json";
  private final String PROPERTIES_CELL_COLORS_FILE = "CellColors";
  private static final Logger LOG = LogManager.getLogger(ParseCellColors.class);

  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += CELL_COLORS_JSON;
    LOG.info("saving Cell colors at {}", location);
    Map<CellState, Color> cellStateColorMap = (Map<CellState, Color>) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(serializeMap(cellStateColorMap));
    putJsonInProp(props, location, json, PROPERTIES_CELL_COLORS_FILE);
  }

  @Override
  public Map<CellState, Color> parse(Properties props) throws ParserException {
    String cellColorsFile = props.getProperty(PROPERTIES_CELL_COLORS_FILE);
    LOG.info("parsing Cell colors at {}", cellColorsFile);
    Gson gson = new GsonBuilder().create();
    Map<String, String> cellStateColorMap = (Map<String, String>) getElementFromJson(cellColorsFile, gson, getParsedClass());
    return deserializeMap(cellStateColorMap);
  }

  @Override
  public Class getParsedClass() {
    return Map.class;
  }

  private Map<String, String> serializeMap(Map<CellState, Color> map) {
    Map<String, String> ret = new HashMap<>();
    for(CellState s: map.keySet()) {
      Color c = map.get(s);
      ret.put(serializeCellState(s),serializeColor(c));
    }
    return ret;
  }

  private Map<CellState, Color> deserializeMap(Map<String, String> map) {
    Map<CellState, Color> ret = new HashMap<>();
    for(String s: map.keySet()) {
      String c = map.get(s);
      ret.put(deserializeCellState(s), deserializeColor(c));
    }
    return ret;
  }

  private String serializeCellState(CellState state) {
    return state.toString();
  }

  private CellState deserializeCellState(String i) {
    return CellState.valueOf(i);
  }

  private String serializeColor(Color c) {
    String ret = "";
    ret += (int)(c.getRed()*255) + " ";
    ret += (int)(c.getGreen()*255) + " ";
    ret += (int)(c.getBlue()*255);
    return ret;
  }

  private Color deserializeColor(String s) {
    String[] colors = s.split(" ");
    int[] colorVals = new int[colors.length];
    for(int i = 0; i < 3; i++) {
      colorVals[i] = Integer.parseInt(colors[i]);
    }
    return Color.rgb(colorVals[0], colorVals[1], colorVals[2]);
  }
}

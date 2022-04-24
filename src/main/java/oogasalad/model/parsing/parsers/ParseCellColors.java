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

public class ParseCellColors extends ParsedElement {

  private final String CELL_COLORS_JSON = "CellColors.json";
  private final String PROPERTIES_CELL_COLORS_FILE = "CellColors";
  private static final Logger LOG = LogManager.getLogger(ParseCellColors.class);

  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += CELL_COLORS_JSON;
    LOG.info("saving Cell colors at {}", location);
    Map cellStateColorMap = (Map<CellState, Color>) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    putJsonInProp(props, location, cellStateColorMap, gson, PROPERTIES_CELL_COLORS_FILE);
  }

  @Override
  public Map<CellState, Color> parse(Properties props) throws ParserException {
    String cellColorsFile = props.getProperty(PROPERTIES_CELL_COLORS_FILE);
    LOG.info("parsing Cell colors at {}", cellColorsFile);
    Gson gson = new GsonBuilder().create();
    Map cellStateColorMap;
    cellStateColorMap = (Map<CellState, Color>) getElementFromJson(cellColorsFile, gson, getParsedClass());
    return cellStateColorMap;
  }

  @Override
  public Class getParsedClass() {
    return Map.class;
  }
}

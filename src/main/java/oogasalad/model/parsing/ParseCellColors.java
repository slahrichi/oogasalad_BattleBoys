package oogasalad.model.parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;
import java.util.Properties;
import javafx.scene.paint.Color;
import oogasalad.model.utilities.tiles.enums.CellState;

public class ParseCellColors extends ParsedElement{

  private final String CELL_COLORS_JSON = "CellColors.json";
  private final String PROPERTIES_CELL_COLORS_FILE = "CellColors";
  private final String MISSING_FILE = "missingFile";

  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += CELL_COLORS_JSON;
    Map cellStateColorMap = (Map<CellState, Color>) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    putJsonInProp(props, location, cellStateColorMap, gson, PROPERTIES_CELL_COLORS_FILE);
  }

  @Override
  public Object parse(Properties props) throws ParserException {
    String cellColorsFile = props.getProperty(PROPERTIES_CELL_COLORS_FILE);
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

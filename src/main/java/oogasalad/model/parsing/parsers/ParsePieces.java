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
import oogasalad.model.utilities.Piece; //
import oogasalad.model.utilities.tiles.Cell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParsePieces extends ParsedElement {

  private final String PROPERTIES_PIECES_FILE = "PiecesFile";
  private final String PIECES_JSON = "Pieces.json";
  private final String PIECES = "Pieces";
  private static final Logger LOG = LogManager.getLogger(ParsePieces.class);


  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += PIECES_JSON;
    LOG.info("saving Pieces at {}",location);
    List<Piece> pieces = (List<Piece>) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().
        registerTypeHierarchyAdapter(Piece.class, new GSONHelper()).
        create();
    String json = gson.toJson(pieces);
    putJsonInProp(props, location, json, PROPERTIES_PIECES_FILE);
  }

  @Override
  public List<Piece> parse(Properties props) throws ParserException {
    String piecesFile = props.getProperty(PROPERTIES_PIECES_FILE);
    LOG.info("parsing Pieces at {}",piecesFile);
    Gson gson = new GsonBuilder().registerTypeAdapter(Piece.class, new GSONHelper()).create();
    Type listOfMyClassObject = new TypeToken<ArrayList<Piece>>() {}.getType();
    return (List<Piece>) getParsedObject(piecesFile, gson, listOfMyClassObject, PIECES);
  }

  @Override
  public Class getParsedClass() {
    return List.class;
  }
}

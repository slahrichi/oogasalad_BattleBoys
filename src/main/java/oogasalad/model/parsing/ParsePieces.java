package oogasalad.model.parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import oogasalad.model.utilities.Piece; //

public class ParsePieces extends ParsedElement {

  private final String PROPERTIES_PIECES_FILE = "PiecesFile";
  private final String PIECES_JSON = "Pieces.json";
  private final String PIECES = "Pieces";
  private final String MISSING_DATA =  "missingData";



  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    location += PIECES_JSON;
    List<Piece> pieces = (List<Piece>) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().
        registerTypeHierarchyAdapter(Piece.class, new GSONHelper()).
        create();
    putJsonInProp(props, location, pieces, gson, PROPERTIES_PIECES_FILE);
  }

  @Override
  public Object parse(Properties props) throws ParserException {
    String piecesFile = props.getProperty(PROPERTIES_PIECES_FILE);
    Gson gson = new GsonBuilder().registerTypeAdapter(Piece.class, new GSONHelper()).create();
    Type listOfMyClassObject = new TypeToken<ArrayList<Piece>>() {}.getType();
    return (List<Piece>) getParsedObject(piecesFile, gson, listOfMyClassObject, PIECES);
  }

  @Override
  public Class getParsedClass() {
    return List.class;
  }
}

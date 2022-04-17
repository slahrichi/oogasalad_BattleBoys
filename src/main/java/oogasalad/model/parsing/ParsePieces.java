package oogasalad.model.parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import oogasalad.model.utilities.Piece;

public class ParsePieces extends ParsedElement {

  private final String PROPERTIES_PIECES_FILE = "PiecesFile";


  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    List<Piece> pieces = (List<Piece>) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().
        registerTypeHierarchyAdapter(Piece.class, new GSONHelper()).
        create();

    String json = gson.toJson(pieces);

    File myNewFile = new File(location);
    try {
      if (myNewFile.createNewFile()) { //new file created
        FileWriter myWriter = new FileWriter(myNewFile);
        myWriter.write(json);
        myWriter.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    props.put(PROPERTIES_PIECES_FILE, myNewFile.toString());
  }

  @Override
  public Object parse(Properties props) throws ParserException {
    String piecesFile = props.getProperty(PROPERTIES_PIECES_FILE);
    Gson gson = new GsonBuilder().registerTypeAdapter(Piece.class, new GSONHelper()).create();

    Type listOfMyClassObject = new TypeToken<ArrayList<Piece>>() {}.getType();
    List<Piece> ret = null;
    try {
      ret = gson.fromJson(new FileReader(piecesFile), listOfMyClassObject);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    if (ret == null) {
      throw new ParserException(exceptionMessageProperties.getProperty("missingData").formatted(piecesFile,"Pieces"));
    }
    return ret;
  }

  @Override
  public Class getParsedClass() {
    return null;
  }
}

package oogasalad.model.parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import oogasalad.model.utilities.tiles.enums.CellState;

public class ParseBoard extends ParsedElement {

  private final String PROPERTIES_BOARD_FILE = "BoardFile";

  public ParseBoard() {
  }


  @Override
  public void save(Properties props, String location, Object o) {
    CellState[][] board = (CellState[][]) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().
        create();
    String json = gson.toJson(board);
    File myNewFile = new File(location);
    try {
      FileWriter myWriter = new FileWriter(myNewFile);
      myWriter.write(json);
      myWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    props.put(PROPERTIES_BOARD_FILE, myNewFile.toString());
  }

  @Override
  public CellState[][] parse(Properties props) throws ParserException {
    String boardFile = props.getProperty(PROPERTIES_BOARD_FILE);
    Gson gson = new GsonBuilder().create();
    CellState[][] boardData;
    try {
      boardData = gson.fromJson(new FileReader(boardFile), CellState[][].class);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return null;
    }
    checkAlignedBoard(boardData, boardFile);
    return boardData;
  }

  @Override
  public Class getParsedClass() {
    return CellState[][].class;
  }

  private void checkAlignedBoard(CellState[][] board, String path) throws ParserException {
    int assumedLength = board[0].length;
    for(int i = 1; i < board.length; i++) {
      if(board[i].length != assumedLength) {
        throw new ParserException(exceptionMessageProperties.getProperty("missingData").formatted(path,"Board"));
      }
    }
  }
}

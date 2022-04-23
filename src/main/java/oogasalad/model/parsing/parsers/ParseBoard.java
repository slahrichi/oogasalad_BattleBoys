package oogasalad.model.parsing.parsers;

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
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.utilities.tiles.enums.CellState;

public class ParseBoard extends ParsedElement {

  private final String PROPERTIES_BOARD_FILE = "BoardFile";
  private final String MISSING_DATA = "missingData";
  private final String BOARD = "Board";
  private final String BOARD_JSON = "Board.json";

  @Override
  public void save(Properties props, String location, Object o) {
    location += BOARD_JSON;
    CellState[][] board = (CellState[][]) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().
        create();
    putJsonInProp(props, location, board, gson, PROPERTIES_BOARD_FILE);
  }

  @Override
  public CellState[][] parse(Properties props) throws ParserException {
    String boardFile = props.getProperty(PROPERTIES_BOARD_FILE);
    Gson gson = new GsonBuilder().create();
    CellState[][] boardData;
    boardData = (CellState[][]) getElementFromJson(boardFile, gson, getParsedClass());
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
        throw new ParserException(exceptionMessageProperties.getProperty(MISSING_DATA).formatted(path,BOARD));
      }
    }
  }
}

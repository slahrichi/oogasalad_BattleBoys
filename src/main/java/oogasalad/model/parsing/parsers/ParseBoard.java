package oogasalad.model.parsing.parsers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Properties;
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseBoard extends ParsedElement {

  private final String PROPERTIES_BOARD_FILE = "BoardFile";
  private final String MISSING_DATA = "missingData";
  private final String BOARD = "Board";
  private final String BOARD_JSON = "Board.json";
  private static final Logger LOG = LogManager.getLogger(ParseBoard.class);

  @Override
  public void save(Properties props, String location, Object o) {
    location += BOARD_JSON;
    LOG.info("saving Board at {}",location);
    CellState[][] board = (CellState[][]) o;
    Gson gson = new GsonBuilder().setPrettyPrinting().
        create();
    putJsonInProp(props, location, board, gson, PROPERTIES_BOARD_FILE);
  }

  @Override
  public CellState[][] parse(Properties props) throws ParserException {
    String boardFile = props.getProperty(PROPERTIES_BOARD_FILE);
    LOG.info("parsing Board at {}",boardFile);
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

package oogasalad;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;

public class Parser {

  private List<String> stringPlayers;
  private static final String FILEPATH = "oogasalad.model.players.";

  public Parser() {
    stringPlayers = new ArrayList<>();
    PlayerData s = parse(new File("src/main/resources/ExampleDataFile.properties"));
  }

  public PlayerData parse(File file) {
    List<String> players = new ArrayList<>();
    List<Piece> pieces = new ArrayList<>();
    Properties props = new Properties();

    try {
      InputStream is = new FileInputStream(file);
      props.load(is);
      is.close();
    }
    catch(IOException e) {
      System.out.println("Bad file");
      //TODO: throw some type of error
    }

    makePlayers(props, players);
    CellState[][] cellBoard = makeboard(props);

    String piecesFileLocation = "PiecesFileLocation";
    String piecesFile = props.getProperty(piecesFileLocation);

    return new PlayerData(players, pieces, cellBoard);

  }

  private void makePlayers(Properties props, List<String> players) {
    int numPlayers = Integer.parseInt(props.getProperty("NumPlayers"));
    System.out.printf("There are %d players\n", numPlayers);
    for(int i = 0; i < numPlayers; i++) {
      String playerString = "Player" + (i+1);
      String playerType = props.getProperty(playerString);
      System.out.printf("Type of player is %s\n", playerType);
      players.add(playerType);
    }
  }

  private CellState[][] makeboard(Properties props) {
    String boardFileLocation = "board";
    String[] boardData = props.getProperty(boardFileLocation).split(";");

    int numRows = boardData.length;
    int numCols = boardData[0].split(" ").length;
    int[][] intBoard = new int[numRows][numCols];

    for(int row = 0; row < numRows; row++) {
      String[] rowData = boardData[row].split(" ");
      for(int col = 0; col < numCols; col++) {
        intBoard[row][col] = Integer.parseInt(rowData[col]);
      }
    }

    CellState[][] cellBoard = new CellState[intBoard.length][intBoard[0].length];
    for (int i = 0; i < intBoard.length; i++) {
      for (int j = 0; j < intBoard[0].length; j++) {
        cellBoard[i][j] = CellState.values()[intBoard[i][j]];
        System.out.println(cellBoard[i][j]);
      }
    }

    return cellBoard;
  }


}

package oogasalad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;


public class Parser {


  public Parser() {
  }

  public void saveData(PlayerData data, String pathToNewFile) {
    //TODO: implement this
  }

  public void savePieces(List<Piece> data, String pathToNewFile) {
    Gson gson = new GsonBuilder().setPrettyPrinting().
        registerTypeHierarchyAdapter(Piece.class, new GSONHelper()).
        create();

    String json = gson.toJson(data);

    File myNewFile = new File(pathToNewFile);
    try {
      if (myNewFile.createNewFile()) { //new file created
        FileWriter myWriter = new FileWriter(myNewFile);
        myWriter.write(json);
        myWriter.close();
      } else { //file already exists, maybe prompt to make sure you want to delete this
        System.out.println("Encountered else block in saveData in Parser.saveData");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public PlayerData parse(String pathToFile) {
    File file = new File(pathToFile);
    Properties props = new Properties();
    try {
      InputStream is = new FileInputStream(file);
      props.load(is);
      is.close();
    }
    catch(IOException e) {
      System.out.println("Bad file");
      return null;
      //TODO: throw some type of error
    }

    List<String> players = makePlayers(props);
    CellState[][] cellBoard = makeBoard(props);
    List<Piece> pieces = makePieces(props);

    return new PlayerData(players, pieces, cellBoard);

  }

  private List<Piece> makePieces(Properties props) {
    String piecesFileLocation = "PiecesFileLocation";
    String piecesFile = props.getProperty(piecesFileLocation);
    Gson gson = new GsonBuilder().registerTypeAdapter(Piece.class, new GSONHelper()).create();

    Type listOfMyClassObject = new TypeToken<ArrayList<Piece>>() {}.getType();
    List<Piece> ret = null;
    try {
      ret = gson.fromJson(new FileReader(piecesFile), listOfMyClassObject);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return ret;
  }

  private List<String> makePlayers(Properties props) {
    List<String> players = new ArrayList<>();
    int numPlayers = Integer.parseInt(props.getProperty("NumPlayers"));
    for(int i = 0; i < numPlayers; i++) {
      String playerString = "Player" + (i+1);
      String playerType = props.getProperty(playerString);
      players.add(playerType);
    }
    return players;
  }

  private CellState[][] makeBoard(Properties props) {
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
      }
    }

    return cellBoard;
  }


}

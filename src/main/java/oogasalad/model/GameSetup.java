package oogasalad.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Scene;
import oogasalad.PlayerData;
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.view.SetupView;

public class GameSetup extends PropertyObservable implements PropertyChangeListener {

  private List<String> playerTypes;
  private int[][] boardSetup;
  private SetupView setupView;
  private List<Player> playerList;
  private int playerIndex;
  private List<Piece> pieceList;
  private int pieceIndex;

  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String CONFIG_ERROR = "Invalid player type given";
  private static final String COORD_ERROR = "Error placing piece at (%d, %d)";

  public GameSetup(PlayerData data){
    this.playerTypes = data.players();
    this.boardSetup = data.board();
    this.pieceList = data.pieces();
    this.pieceIndex = 0;
    this.playerIndex = 0;
    setupGame();
  }

  private void setupGame() {
    playerList = new ArrayList<>();
    int id = 0;
    for (String playerType : playerTypes) {
      playerList.add(createPlayer(playerType, id++));
    }
    initializeSetupView();
  }

  public List<Player> getPlayerList() {
    return playerList;
  }

  private void initializeSetupView() {
    setupView = new SetupView(boardSetup);
    setupView.addObserver(this);
    setupView.setCurrentPlayerNum(playerIndex + 1);
    setupView.setCurrentPiece(pieceList.get(0).getHPList());
  }

  private Player createPlayer(String playerType, int id) {
    Board b = new Board(boardSetup);
    Map<Integer, Board> enemyMap = createEnemyMap(b, id);
    Player p = null;
    try {
      p = (Player) Class.forName(FILEPATH + playerType).getConstructor(Board.class, int.class,
              Map.class)
          .newInstance(b, id, enemyMap);
    } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
        IllegalAccessException | NoSuchMethodException e) {
      setupView.showError(CONFIG_ERROR);
     }
    return p;
  }

  private Map<Integer, Board> createEnemyMap(Board b, int id) {
    Map<Integer, Board> boardMap = new HashMap<>();
    for (int i = 0; i < playerTypes.size(); i++) {
      if (i == id) continue;
      boardMap.put(i, b.copyOf());
    }
    return boardMap;
  }

  public Scene createScene() {
    System.out.println("GameSetup pre-alive");
    return setupView.createSetUp();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    //System.out.println("Im alive");
    Coordinate coords = (Coordinate) evt.getNewValue();
    //System.out.println("Row: " + coords.getRow() + " Col: " + coords.getColumn());
    placePiece(coords.getColumn(), coords.getRow());
  }

  private void placePiece(int row, int col) {
    //System.out.println("Success");
    Player player = playerList.get(playerIndex);
    Piece piece = pieceList.get(pieceIndex).copyOf();
    if (player.placePiece(piece, new Coordinate(row, col))) {
      update(piece);
    }
    else {
      setupView.showError(String.format(COORD_ERROR, row, col));
    }
  }

  private void moveToNextPlayer() {
    playerIndex++;
    if(playerIndex == playerList.size() - 1) {
      notifyObserver("moveToGame", null);
      return;
    }
    resetElements();
  }

  private void resetElements() {
    pieceIndex = 0;
    setupView.setCurrentPlayerNum(playerIndex + 1);
    setupView.clearBoard();
    setupView.setCurrentPiece(pieceList.get(pieceIndex).getHPList());
  }

  private void update(Piece piece) {
    setupView.placePiece(piece.getHPList(), "Piece type");
    pieceIndex++;
    if(pieceIndex >= pieceList.size()) {
      moveToNextPlayer();
      return;
    }
    setupView.setCurrentPiece(pieceList.get(pieceIndex).getHPList());
  }
}

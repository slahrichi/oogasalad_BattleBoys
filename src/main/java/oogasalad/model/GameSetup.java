package oogasalad.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import oogasalad.PlayerData;
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.view.ErrorDisplayer;
import oogasalad.view.Info;
import oogasalad.view.SetupView;

public class GameSetup extends PropertyObservable implements PropertyChangeListener {

  private List<String> playerTypes;
  private int[][] boardSetup;
  private SetupView setupView;
  private List<Player> playerList;
  private int currentPlayerIndex;
  private List<Piece> pieceList;
  private int currentPieceIndex;

  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String CONFIG_ERROR = "Invalid player type given";

  public GameSetup(PlayerData data){
    this.playerTypes = data.players();
    this.boardSetup = data.board();
    this.pieceList = data.pieces();
    this.currentPieceIndex = 0;
    this.currentPlayerIndex = 0;
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
    setupView.setCurrentPlayerNum(currentPlayerIndex + 1);
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
    } catch (ClassNotFoundException e) {
      setupView.showError(CONFIG_ERROR);
    } catch (InvocationTargetException e) {
      setupView.showError(CONFIG_ERROR);
    } catch (InstantiationException e) {
      setupView.showError(CONFIG_ERROR);
    } catch (IllegalAccessException e) {
      setupView.showError(CONFIG_ERROR);
    } catch (NoSuchMethodException e) {
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
    System.out.println("Im alive");
    Coordinate coords = (Coordinate) evt.getNewValue();
    System.out.println("Row: " + coords.getRow() + " Col: " + coords.getColumn());
    placePiece(coords.getColumn(), coords.getRow());
  }

  private void placePiece(int row, int col) {
    System.out.println("Success");
    Player player = playerList.get(currentPlayerIndex);
    Piece piece = pieceList.get(currentPieceIndex).copyOf();
    if (player.placePiece(piece, new Coordinate(row, col))) {
      update(piece);
    }
    else {
      setupView.showError("Error placing piece at (" + row + "," + col + ")");
    }
  }

  private void moveToNextPlayer() {
    currentPlayerIndex++;

    if(currentPlayerIndex == playerList.size() - 1) {
      notifyObserver("moveToGame", null);
      return;
    }

    currentPieceIndex = 0;
    setupView.setCurrentPlayerNum(currentPlayerIndex + 1);
    setupView.clearBoard();
    setupView.setCurrentPiece(pieceList.get(currentPieceIndex).getHPList());
  }

  private void update(Piece piece) {
    System.out.println(piece.getHPList());
    setupView.placePiece(piece.getHPList(), "Piece type");
    currentPieceIndex++;

    if(currentPieceIndex >= pieceList.size()) {
      moveToNextPlayer();
      return;
    }

    // What is HPList?
    setupView.setCurrentPiece(pieceList.get(currentPieceIndex).getHPList());
  }
}

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
//  private Map<Integer, Player> idMap;
//  private Map<Player, Integer> placeCounts;

  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String ERROR = "Invalid player type given";

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
    idMap = new HashMap<>();
    placeCounts = new HashMap<>();
    int id = 0;
    for (String playerType : playerTypes) {
      playerList.add(createPlayer(playerType, id++));
    }
    setupView = new SetupView(playerList);
    setupView.addObserver(this);
  }

  private Player createPlayer(String playerType, int id) {
    Board b = new Board(boardSetup);
    Player p = null;
    try {
      p = (Player) Class.forName(FILEPATH + playerType).getConstructor(Board.class, int.class)
          .newInstance(b, id);
      idMap.put(id, p);
      placeCounts.put(p, 0);
    } catch (ClassNotFoundException e) {
      showError(ERROR);
    } catch (InvocationTargetException e) {
      showError(ERROR);
    } catch (InstantiationException e) {
      showError(ERROR);
    } catch (IllegalAccessException e) {
      showError(ERROR);
    } catch (NoSuchMethodException e) {
      showError(ERROR);
    }
    return p;
  }

  public Scene createScene() {
    return setupView.createSetUp();
  }

  private void showError(String message) {
    Alert alert = new Alert(AlertType.ERROR, message);
    alert.showAndWait();
    endGame();
  }

  private void endGame() {
    Platform.exit();
    System.exit(0);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Coordinate coords = (Coordinate) evt.getNewValue();
    placePiece(coords.getColumn(), coords.getRow());
  }

  private void placePiece(int row, int col) {
    System.out.println("Success");
    Player player = playerList.get(currentPlayerIndex);
    Piece piece = pieceList.get(currentPieceIndex);
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

  private void update(Player player, Piece piece) {
    placeCounts.put(player, placeCounts.get(player) + 1);
    setupView.placePiece(piece);
  }
}

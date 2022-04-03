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
  private List<Piece> pieceList;
  private Map<Integer, Player> idMap;
  private Map<Player, Integer> placeCounts;

  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String ERROR = "Invalid player type given";

  public GameSetup(PlayerData data){
    this.playerTypes = data.players();
    this.boardSetup = data.board();
    this.pieceList = data.pieces();
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
    Info info = (Info)evt.getNewValue();
    placePiece(info.x(), info.y(), info.ID());
  }

  public void placePiece(int x, int y, int id) {
    System.out.println("Success");
    if (canStillPlace(id)) {
      Player player = idMap.get(id);
      Piece piece = pieceList.get(placeCounts.get(player));
      if (player.placePiece(piece, new Coordinate(x, y))) {
        update(player, piece);
      }
      else {
        setupView.showError();
      }
    }
    else if (!canStillPlace(id) && id == idMap.size() - 1) {
      notifyObserver("moveToGame", null);
    }
    else if (!canStillPlace(id)) {
      setupView.moveToNextPlayer();
    }
  }

  private void update(Player player, Piece piece) {
    placeCounts.put(player, placeCounts.get(player) + 1);
    setupView.placePiece(piece);
  }

  public boolean canStillPlace(int id) {
    int index = placeCounts.get(idMap.get(id));
    return index < pieceList.size();
  }
}

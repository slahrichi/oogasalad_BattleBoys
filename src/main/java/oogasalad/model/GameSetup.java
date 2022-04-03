package oogasalad.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import oogasalad.PlayerData;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Piece;
import oogasalad.view.SetupView;

public class GameSetup {

  private List<String> playerTypes;
  private int[][] boardSetup;
  private SetupView setupView;
  private List<Player> playerList;

  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String ERROR = "Invalid player type given";

  public GameSetup(PlayerData data){
    this.playerTypes = data.players();
    this.boardSetup = data.board();
    /*
    this.pieceMap = pieceMap;
    this.rows = rows;
    this.cols = cols;
     */
//    this.setupView = new SetupView();
    setupGame();
  }

  private void setupGame() {
    playerList = new ArrayList<>();
    int id = 0;
    for (String playerType : playerTypes) {
      playerList.add(createPlayer(playerType, id++));
    }
    setupView = new SetupView(playerList);
  }

  private Player createPlayer(String playerType, int id) {
    Board b = new Board(boardSetup);
    Player p = null;
    try {
      p = (Player) Class.forName(FILEPATH + playerType).getConstructor(Board.class, int.class)
          .newInstance(b, id);
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
}

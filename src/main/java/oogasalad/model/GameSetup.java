package oogasalad.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Piece;
import oogasalad.view.SetupView;

public class GameSetup {

  private List<String> playerTypes;
  private Map<Player, List<Piece>> pieceMap;
  private int rows;
  private int cols;
  private SetupView setupView;
  private List<Player> playerList;

  private static final String FILEPATH = "oogasalad.model.players.";

  public GameSetup(List<String> playerTypes, Map<Player, List<Piece>> pieceMap, int rows, int cols){
    this.playerTypes = playerTypes;
    this.pieceMap = pieceMap;
    this.rows = rows;
    this.cols = cols;
    this.setupView = new SetupView();
    setupGame();
  }

  private void setupGame() {
    playerList = new ArrayList<>();
    int id = 0;
    for (String playerType : playerTypes) {
      playerList.add(createPlayer(playerType, id++));
    }
  }

  private Player createPlayer(String playerType, int id) {
    Board b = new Board(rows, cols);
    Player p = null;
    try {
      p = (Player) Class.forName(FILEPATH + playerType).getConstructor(Board.class, int.class)
          .newInstance(b, id);
      placePieces(p, id);
    } catch (ClassNotFoundException e) {
      //setupView.showError()
    } catch (InvocationTargetException e) {
      //setupView.showError()
    } catch (InstantiationException e) {
      //setupView.showError()
    } catch (IllegalAccessException e) {
      //setupView.showError()
    } catch (NoSuchMethodException e) {
      //setupView.showError()
    }
    return p;
  }


  private void placePieces(Player p, int id) {
    List<Piece> pieceList = pieceMap.get(id);
    //setupView.getPiecesFromSetup();
    /*
    This method should take the pieces, render them, and then query the player to place them
    You can call some method from GameSetup to explicitly do so
     */

  }
}

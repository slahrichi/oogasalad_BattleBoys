package oogasalad.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.stage.Stage;
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Piece;
import oogasalad.view.SetupView;
import oogasalad.view.ShotInfo;

public class GameSetup extends PropertyObservable implements PropertyChangeListener {

  private SetupView setupView;
  private static final String FILEPATH = "oogasalad.model.players.";

  private List<Player> playerList;
  private int currentPlayerIndex;
  private List<String> playerTypes;
  private Map<Player, List<Piece>> pieceMap;
  private int rows;
  private int cols;

  public GameSetup(List<Player> playerList) {
    this.playerList = playerList;
//    this.pieceMap = pieceMap;
    setupView = new SetupView();
    setupView.addObserver(this);
//    initializeGame(rows, cols);
  }

//  private void initializeGame(int rows, int cols) {
//    setupBoards(rows, cols);
//    placePieces();
//  }
//
//  private void setupBoards(int rows, int cols) {
//    for (Player p : playerList) {
//      p.setupBoard(rows, cols);
//    }
//  }
//
//  private void placePieces() {
//    for (Player p : playerList) {
//      List<Piece> list = pieceMap.get(p);
//      for (Piece piece : list) {
//        p.placePiece(piece);
//      }
//      p.determineHealth();
//    }
//  }

  public void show(Stage stage) {
    stage.setScene(setupView.createSetUp());
  }

  //
  private void handlePiecePlacement(int x, int y) {
    // Check if current Piece can be placed in this position on the current Board
    // If yes, place the Piece there, tell the SetupView to place the piece visually, update the
    // current Piece and the visual representation of the current Piece being placed, and switch to
    // the next Player setup if that was the last Piece for them to place

  }

//
//  public GameSetup(List<String> playerTypes, Map<Player, List<Piece>> pieceMap, int rows, int cols){
//    this.playerTypes = playerTypes;
//    this.pieceMap = pieceMap;
//    this.rows = rows;
//    this.cols = cols;
//    this.setupView = new SetupView();
//    setupGame();
//  }

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

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    System.out.println("ID: " + ((ShotInfo)evt.getNewValue()).ID());
    int y = ((ShotInfo)evt.getNewValue()).y();
    int x = ((ShotInfo)evt.getNewValue()).x();
    handlePiecePlacement(x, y);
  }
}

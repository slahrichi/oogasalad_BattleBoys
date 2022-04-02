package oogasalad.model;

import java.util.List;
import java.util.Map;
import javafx.stage.Stage;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Piece;
import oogasalad.view.SetupView;

public class GameSetup {

  private SetupView setupView;

  private List<Player> playerList;
  private Map<Player, List<Piece>> pieceMap;

  public GameSetup(List<Player> playerList) {
    this.playerList = playerList;
//    this.pieceMap = pieceMap;
    setupView = new SetupView();
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
}

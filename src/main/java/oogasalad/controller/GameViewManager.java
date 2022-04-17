package oogasalad.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import oogasalad.GameData;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.GameView;

public class GameViewManager {

  private GameView view;

  public GameViewManager(GameData data) {
    setupGameView(data);
  }

  private void setupGameView(GameData data) {
    List<CellState[][]> boards = createFirstPlayerBoards(data);
    Collection<Collection<Coordinate>> coords = createInitialPieces(data.pieces());
    view = new GameView(boards, coords);
  }

  private List<CellState[][]> createFirstPlayerBoards(GameData data) {
    List<CellState[][]> boards = new ArrayList<>();
    Player firstPlayer = data.players().get(0);
    boards.add(firstPlayer.getBoard().getCurrentBoardState());
    for (int i = 0; i < firstPlayer.getEnemyMap().size(); i++) {
      boards.add(data.board());
    }
    return boards;
  }

  private Collection<Collection<Coordinate>> createInitialPieces(List<Piece> pieces) {
    Collection<Collection<Coordinate>> pieceCoords = new ArrayList<>();
    for (Piece piece : pieces) {
      pieceCoords.add(piece.getRelativeCoords());
    }
    return pieceCoords;
  }

  GameView getView() {
    return view;
  }
}

package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import oogasalad.GameData;
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.SetupView;

public class GameSetup extends PropertyObservable implements PropertyChangeListener {

  private CellState[][] board;
  private SetupView setupView;
  private List<Player> playerList;
  private int playerIndex;
  private List<Piece> pieceList;
  private int pieceIndex;

  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String CONFIG_ERROR = "Invalid player type given";
  private static final String COORD_ERROR = "Error placing piece at (%d, %d)";

  public GameSetup(GameData data){
    this.playerList = data.players();
    this.board = data.board();
    this.pieceList = data.pieces();
    this.pieceIndex = 0;
    this.playerIndex = 0;
    setupGame();
  }

  private void setupGame() {
    initializeSetupView();
  }

  public List<Player> getPlayerList() {
    return playerList;
  }

  private void initializeSetupView() {
    setupView = new SetupView(board);
    setupView.addObserver(this);
    setupView.setCurrentPiece(pieceList.get(0).getRelativeCoords());
  }

  public Scene createScene() {
    return setupView.getScene();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Coordinate c = (Coordinate) evt.getNewValue();
    try {
      Method m = this.getClass().getDeclaredMethod(evt.getPropertyName(), Coordinate.class);
      m.invoke(this, c);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private void placePiece(Coordinate c) {
    Player player = playerList.get(playerIndex);
    Piece piece = pieceList.get(pieceIndex).copyOf();
    if (player.placePiece(piece, new Coordinate(c.getRow(), c.getColumn()))) {
      update(piece);
    }
    else {
      setupView.showError(String.format(COORD_ERROR, c.getRow(), c.getColumn()));
    }
  }

  private void moveToNextPlayer(Coordinate c) {
    playerIndex++;
    if(playerIndex >= playerList.size()) {
      System.out.println("Moving to game");
      notifyObserver("moveToGame", null);
      return;
    }
    resetElements();
    System.out.println("Moving to next player");
  }

  private void resetElements() {
    pieceIndex = 0;
    setupView.setCurrentPiece(pieceList.get(pieceIndex).getRelativeCoords());
  }

  private void update(Piece piece) {
    List<Coordinate> coords = new ArrayList<>();
    for (ShipCell cell : piece.getCellList()) {
      coords.add(cell.getCoordinates());
    }
    setupView.placePiece(coords, CellState.SHIP_HEALTHY);
    pieceIndex++;
    if (pieceIndex != pieceList.size()) {
      setupView.setCurrentPiece(pieceList.get(pieceIndex).getRelativeCoords());
    }
    else {
      setupView.displayCompletion();
      setupView.activateConfirm();
    }
  }

  public SetupView getSetupView() {
    return setupView;
  }
}

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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Auxiliary class for initializing game elements, particularly allowing players to place their
 * pieces and then send the resulting placements both to the UI and the backend
 *
 * @author Matthew Giglio
 */
public class GameSetup extends PropertyObservable implements PropertyChangeListener {

  private CellState[][] board;
  private SetupView setupView;
  private List<Player> playerList;
  private int playerIndex;
  private List<Piece> pieceList;
  private int pieceIndex;

  private static final String COORD_ERROR = "Error placing piece at (%d, %d)";
  private static final String INVALID_METHOD = "Invalid method name given";
  private static final Logger LOG = LogManager.getLogger(GameSetup.class);

  /**
   *
   * @param data GameData object used to standarize elements such as a generic game board, the
   * players involved in the game, and the pieces they are allowed to place
   *
   */
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

  public SetupView getSetupView() {
    return setupView;
  }


  private void initializeSetupView() {
    setupView = new SetupView(board);
    setupView.addObserver(this);
    setupView.setCurrentPiece(pieceList.get(0).getRelativeCoords());
  }

  /**
   *
   * @return Scene object storing the SetupView
   */
  public Scene createScene() {
    return setupView.getScene();
  }

  /**
   * listener that takes in a Coordinate and then checks with the backend if the choice is valid
   * @param evt PropertyChangeEvent storing the Coordinate at which a Player selected in the view
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Coordinate c = (Coordinate) evt.getNewValue();
    try {
      Method m = this.getClass().getDeclaredMethod(evt.getPropertyName(), Coordinate.class);
      m.invoke(this, c);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
    NullPointerException e) {
      throw new NullPointerException(INVALID_METHOD);
    }
  }

  private void removePiece(Coordinate c) {
    if (pieceIndex > 0) {
      pieceIndex--;
      setupView.setCurrentPiece(pieceList.get(pieceIndex).getRelativeCoords());
      playerList.get(playerIndex).removePiece(pieceList.get(pieceIndex).getID());
      // if last placed should be empty
      if (pieceIndex == 0) {
        setupView.setLastPlaced(new ArrayList<>());
      } else {
        setupView.setLastPlaced(pieceList.get(pieceIndex-1).getRelativeCoords());
      }
    }
  }

  private void removeAllPieces(Coordinate c) {
    if (pieceIndex > 0) {
      pieceIndex = 0;
      setupView.setCurrentPiece(pieceList.get(pieceIndex).getRelativeCoords());
      playerList.get(playerIndex).removeAllPieces();
    }
  }

  private void placePiece(Coordinate c) {
    Player player = playerList.get(playerIndex);
    Piece piece = pieceList.get(pieceIndex).copyOf();
    if (player.placePiece(piece, new Coordinate(c.getRow(), c.getColumn()))) {
      updatePiece(piece);
    }
    else {
      setupView.showError(String.format(COORD_ERROR, c.getRow(), c.getColumn()));
    }
  }

  private void moveToNextPlayer(Coordinate c) {
    playerIndex++;
    if(playerIndex >= playerList.size()) {
      notifyObserver("startGame", null);
      return;
    }
    resetElements();
  }

  private void resetElements() {
    pieceIndex = 0;
    setupView.setCurrentPiece(pieceList.get(pieceIndex).getRelativeCoords());
  }

  private void updatePiece(Piece piece) {
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
}

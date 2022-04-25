package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Stack;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import oogasalad.PropertyObservable;
import oogasalad.model.players.DecisionEngine;
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
 * @author Matthew Giglio, Minjun Kwak
 */
public class GameSetup extends PropertyObservable implements PropertyChangeListener {

  private CellState[][] board;
  private SetupView setupView;
  private List<Player> playerList;
  private int playerIndex;
  private List<Piece> pieceList;
  private Map<Player, DecisionEngine> engineMap;
  private int pieceIndex;
  private Map<CellState, Color> colorMap;
  private Stack<Collection<Coordinate>> lastPlacedAbsoluteCoords;
  private ResourceBundle myResources;

  public static final int SCREEN_DURATION = 2000;
  private static final String COORD_ERROR = "Error placing piece at (%d, %d)";
  private static final String INVALID_METHOD = "Invalid method name given";
  private static final String PROMPT_PREFIX_RESOURCE = "PromptPrefix";
  private static final Logger LOG = LogManager.getLogger(GameSetup.class);

  /**
   *
   * @param data GameData object used to standardize elements such as a generic game board, the
   * players involved in the game, and the pieces they are allowed to place
   *
   */
  public GameSetup(GameData data, ResourceBundle resourceBundle) {
    this.playerList = data.players();
    this.board = data.board();
    this.pieceList = data.pieces();
    this.colorMap = data.cellStateColorMap();
    this.pieceIndex = 0;
    this.playerIndex = -1;
    this.lastPlacedAbsoluteCoords = new Stack<>();
    this.myResources = resourceBundle;
    this.engineMap = data.engineMap();
    setupGame();
  }

  private void setupGame() {
    initializeSetupView();
  }

  public SetupView getSetupView() {
    return setupView;
  }

  private void initializeSetupView() {
    setupView = new SetupView(board, colorMap, myResources);

    setupView.displayIntroScreen();

    Animation pt = new PauseTransition(new Duration(SCREEN_DURATION));
    pt.setOnFinished(e -> {
      setupView.addObserver(this);
      moveToNextPlayer("");
      setupView.setCurrentPiece(pieceList.get(0).getRelativeCoords());
    });

    pt.play();
  }

  /**
   * @return Scene object storing the SetupView
   */
  public Scene createScene() {
    return setupView.getScene();
  }

  /**
   * listener that takes in a Coordinate and then checks with the backend if the choice is valid
   *
   * @param evt PropertyChangeEvent storing the Coordinate at which a Player selected in the view
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String s = (String) evt.getNewValue();
    try {
      Method m = this.getClass().getDeclaredMethod(evt.getPropertyName(), String.class);
      m.invoke(this, s);
    } catch (NoSuchMethodException ex) {
      throw new NullPointerException("NoSuchMethod");
    } catch (InvocationTargetException ex) {
      throw new NullPointerException("InvocationTarget");
    } catch (IllegalAccessException ex) {
      throw new NullPointerException("IllegalAccess");
    }
  }

  private void removePiece(String s) {
    if (pieceIndex > 0) {
      pieceIndex--;
      setupView.setCurrentPiece(pieceList.get(pieceIndex).getRelativeCoords());
      playerList.get(playerIndex).removePiece(pieceList.get(pieceIndex).getID());
      lastPlacedAbsoluteCoords.pop();
      // if last placed should be empty
      if (pieceIndex == 0) {
        setupView.setLastPlaced(new ArrayList<>());
      } else {
        setupView.setLastPlaced(lastPlacedAbsoluteCoords.peek());
      }
    }
  }

  private void removeAllPieces(String s) {
    if (pieceIndex > 0) {
      lastPlacedAbsoluteCoords.clear();
      pieceIndex = 0;
      setupView.setCurrentPiece(pieceList.get(pieceIndex).getRelativeCoords());
      playerList.get(playerIndex).removeAllPieces();
    }
  }

  private void placePiece(String coordinate) {
    int row = Integer.parseInt(coordinate.substring(0, coordinate.indexOf(" ")));
    int col = Integer.parseInt(coordinate.substring(coordinate.indexOf(" ") + 1));
    Player player = playerList.get(playerIndex);
    Piece piece = pieceList.get(pieceIndex).copyOf();
    if (player.placePiece(piece, new Coordinate(row, col))) {
      updatePiece(piece);
    } else {
      setupView.showError(String.format(COORD_ERROR, row, col));
    }
  }

  private void moveToNextPlayer(String s) {
    playerIndex++;
    setupView.displayPassComputerMessage(myResources.getString(PROMPT_PREFIX_RESOURCE) + (playerIndex + 1));
    if (playerIndex >= playerList.size()) {
      notifyObserver("startGame", null);
      return;
    }
    resetElements();
    handleAI();
  }

  private void handleAI() {
    Player player = playerList.get(playerIndex);
    if (engineMap.containsKey(player)) {
      DecisionEngine engine = engineMap.get(player);
      for (int i = 0; i < pieceList.size(); i++) {
        Coordinate c = engine.placePiece(pieceList);
        String coord = c.getRow() + " " + c.getColumn();
        placePiece(coord);
      }
      //have setupView show that AI has placed pieces
      setupView.displayAIShipsPlaced();
      setupView.handleConfirm();
    }
  }

  // Assigns a new name to the current player being set up. This method is called through reflection from this class'
  // property change listener
  private void assignCurrentPlayerName(String name) {
    playerList.get(playerIndex).setName(name);
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
    lastPlacedAbsoluteCoords.push(coords);
    setupView.placePiece(coords, CellState.SHIP_HEALTHY);
    pieceIndex++;
    if (pieceIndex != pieceList.size()) {
      setupView.setCurrentPiece(pieceList.get(pieceIndex).getRelativeCoords());
    } else {
      setupView.displayCompletion();
      setupView.activateConfirm();
    }
  }
}

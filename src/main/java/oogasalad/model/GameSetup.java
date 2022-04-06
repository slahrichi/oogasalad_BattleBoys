package oogasalad.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.Scene;
import oogasalad.PlayerData;
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.view.SetupView;

public class GameSetup extends PropertyObservable implements PropertyChangeListener {

  private List<String> playerTypes;
  private int[][] boardSetup;
  private SetupView setupView;
  private List<Player> playerList;
  private int playerIndex;
  private List<Piece> pieceList;
  private int pieceIndex;

  private static final String FILEPATH = "oogasalad.model.players.";
  private static final String CONFIG_ERROR = "Invalid player type given";
  private static final String COORD_ERROR = "Error placing piece at (%d, %d)";

  public GameSetup(PlayerData data){
    this.playerTypes = data.players();
    this.boardSetup = data.board();
    this.pieceList = data.pieces();
    this.pieceIndex = 0;
    this.playerIndex = 0;
    setupGame();
  }

  private void setupGame() {
    playerList = new ArrayList<>();
    int id = 0;
    for (String playerType : playerTypes) {
      playerList.add(createPlayer(playerType, id++));
    }
    initializeSetupView();
  }

  public List<Player> getPlayerList() {
    return playerList;
  }

  private void initializeSetupView() {
    setupView = new SetupView(boardSetup);
    setupView.addObserver(this);
    setupView.setCurrentPiece(pieceList.get(0).getRelativeCoords());
  }

  private Player createPlayer(String playerType, int id) {
    Board b = new Board(boardSetup);
    Map<Integer, Board> enemyMap = createEnemyMap(b, id);
    Player p = null;
    try {
      p = (Player) Class.forName(FILEPATH + playerType).getConstructor(Board.class, int.class,
              Map.class)
          .newInstance(b, id, enemyMap);
    } catch (ClassNotFoundException | InvocationTargetException | InstantiationException |
        IllegalAccessException | NoSuchMethodException e) {
      setupView = new SetupView(boardSetup);
     }
    return p;
  }

  private Map<Integer, Board> createEnemyMap(Board b, int id) {
    Map<Integer, Board> boardMap = new HashMap<>();
    for (int i = 0; i < playerTypes.size(); i++) {
      if (i == id) continue;
      boardMap.put(i, b.copyOf());
    }
    return boardMap;
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

  // add
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
    System.out.println(piece.getHPList());
    setupView.placePiece(piece.getHPList(), "Piece type");
    pieceIndex++;
    if (pieceIndex != pieceList.size()) {
      setupView.setCurrentPiece(pieceList.get(pieceIndex).getRelativeCoords());
    }
    else {
      setupView.activateConfirm();
    }
  }

  public SetupView getSetupView() {
    return setupView;
  }
}

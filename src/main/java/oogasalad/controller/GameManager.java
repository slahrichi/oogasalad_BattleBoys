package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Scene;
import oogasalad.GameData;
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.tiles.enums.Marker;
import oogasalad.view.Info;
import oogasalad.view.GameView;

public class GameManager extends PropertyObservable implements PropertyChangeListener {

  private List<Player> playerList;
  private Map<Integer, Player> idMap;
  private GameView view;
  //current player, separate from ID
  private int playerIndex;
  private int numShots;
  private int allowedShots;
  private int size;

  public GameManager(GameData data) {
    view = new GameView(data);
    this.view.addObserver(this);
    initialize(data);
  }

  public void updateShipsLeft(List<Piece> pieceList) {
    view.updateShipsLeft(pieceList);
  }

  public Scene createScene() {
    return view.createScene();
  }

//  public void playGame() {
//    while (canStillPlay())
//    for (Player player : playerList) {
//      promptPlayerToPlayTurn();
//      player.playTurn();
//    }
//  }

  public void promptPlayerToPlayTurn() {
    view.promptPlayTurn();
  }


  private void initialize(GameData data) {
     this.playerList = data.players();
     playerIndex = 0;
     size = playerList.size();
     numShots = 0;
     allowedShots = 3;
     createIDMap();
  }

  private void createIDMap() {
    idMap = new HashMap<>();
    for (Player player : playerList) {
      idMap.put(player.getID(), player);
    }
  }


  private boolean canStillPlay() {
    return playerList.size() != 1;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    System.out.println("ID: " + ((Info)evt.getNewValue()).ID());
    int id = ((Info)evt.getNewValue()).ID();
    int row = ((Info)evt.getNewValue()).row();
    int col = ((Info)evt.getNewValue()).col();
    if (makeShot(new Coordinate(row, col), id)) {
      view.displayShotAt(row, col, Marker.HIT_SHIP);
      numShots++;
    };

  }

  public List<Player> getPlayerList() {
    return playerList;
  }

  private boolean makeShot(Coordinate c, int id) {
    Player currentPlayer = playerList.get(playerIndex);
    Player enemy = idMap.get(id);
    if (enemy.canBeStruck(c)) {
      CellState result = null;
      currentPlayer.updateEnemyBoard(c, id, result);
    }
    return false;
  }

}

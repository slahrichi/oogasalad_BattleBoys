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
import oogasalad.model.utilities.tiles.enums.Marker;
import oogasalad.view.Info;
import oogasalad.view.GameView;

public class GameManager extends PropertyObservable implements PropertyChangeListener {

  private List<Player> playerList;
  private Map<Integer, Player> idMap;
  private GameView view;

  public GameManager(GameData data) {
    view = new GameView(data);
    this.view.addObserver(this);
    this.playerList = data.players();
    initialize();
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

  private void initialize() {
    idMap = new HashMap<>();
    int id = 0;
    for (Player p : playerList) {
      idMap.put(id++, p);
    }
  }

  public void executeMove(int id, Coordinate c) {
    Player enemy = idMap.get(id);
    enemy.strike(c);
    if (enemy.getHealth() == 0) {
      playerList.remove(enemy);
    }
  }

  private boolean canStillPlay() {
    return playerList.size() != 1;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    System.out.println("ID: " + ((Info)evt.getNewValue()).ID());
    int row = ((Info)evt.getNewValue()).row();
    int col = ((Info)evt.getNewValue()).col();
    view.displayShotAt(row, col, Marker.HIT_SHIP);
  }

  public List<Player> getPlayerList() {
    return playerList;
  }
}

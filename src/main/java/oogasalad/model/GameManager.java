package oogasalad.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Scene;
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.view.ShotInfo;
import oogasalad.view.View;

public class GameManager extends PropertyObservable implements PropertyChangeListener {

  private List<Player> playerList;
  private Map<Integer, Player> idMap;
  private View view;

  public GameManager(List<Player> playerList) {
    view = new View();
    this.view.addObserver(this);
    this.playerList = playerList;
    initialize();
  }

  public Scene createScene() {
    return view.createViewFromPlayers(playerList);
  }

  public void playGame() {
    while (canStillPlay())
    for (Player player : playerList) {
      promptPlayerToPlayTurn();
      player.playTurn();
    }
  }

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
    System.out.println("ID: " + ((ShotInfo)evt.getNewValue()).ID());
    int row = ((ShotInfo)evt.getNewValue()).y();
    int col = ((ShotInfo)evt.getNewValue()).x();
    view.displayShotAt(row, col, true);
//    view.placePiece(List.of(new Coordinate(row, col), new Coordinate(row + 1, col)), "bruh");
  }
}

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
import oogasalad.view.Info;
import oogasalad.view.GameView;

public class GameManager extends PropertyObservable implements PropertyChangeListener {

  private List<Player> playerList;
  private Map<Integer, Player> idMap;
  private GameView view;

  public GameManager(List<Player> playerList) {
    view = new GameView();
    this.view.addObserver(this);
    this.playerList = playerList;
    initialize();
  }

  public Scene createScene() {
    return view.createViewFromPlayers(playerList);
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
    view.displayShotAt(row, col, true);
//    view.placePiece(List.of(new Coordinate(row, col), new Coordinate(row + 1, col)), "bruh");
  }

  public List<Player> getPlayerList() {
    return playerList;
  }
}

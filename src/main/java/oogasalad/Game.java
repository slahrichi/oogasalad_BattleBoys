package oogasalad;

import java.util.List;
import javafx.stage.Stage;
import oogasalad.model.GameSetup;
import oogasalad.model.players.Player;

public class Game {

  private GameSetup setup;
  private Stage myStage;
  private FilePicker fileChooser;

  public Game(Stage stage, List<Player> playerList) {
    myStage = stage;
    setup = new GameSetup(playerList);
    fileChooser = new FilePicker();
  }

  public void chooseDataFile() {




  }

  private void getters(){

  }

  private void setters(){

  }

  public void createSetup() {
    setup.show(myStage);
  }
}

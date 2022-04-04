package oogasalad;

import java.io.File;
import java.util.List;
import javafx.stage.Stage;
import oogasalad.model.GameSetup;
import oogasalad.model.players.Player;

public class Game {

  private GameSetup setup;
  private Stage myStage;
  private FilePicker fileChooser;
  private Parser parser;

  public Game(Stage stage) {
    myStage = stage;
    parser = new Parser();
    fileChooser = new FilePicker();
    PlayerData data = parser.parse(chooseDataFile());
    setup = new GameSetup(data);
    // GameManager should take in list of players and GameData
  }

  public File chooseDataFile() {
    return fileChooser.getFile();
  }

  public void showSetup() {
    myStage.setScene(setup.createScene());
    myStage.show();
  }
}

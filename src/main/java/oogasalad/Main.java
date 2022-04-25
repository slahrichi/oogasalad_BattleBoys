package oogasalad;

import javafx.application.Application;
import javafx.stage.Stage;
import oogasalad.controller.Game;

/**
 * Class responsible for creating the Game class and starting the program
 */
public class Main extends Application {

    /**
     * Start of the program.
     */
    public static void main (String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Game game = new Game(stage);
        game.selectLanguage();
    }
}

package oogasalad;

import javafx.application.Application;
import javafx.stage.Stage;
import oogasalad.controller.Game;

/**
 * Feel free to completely change this code or delete it entirely. 
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

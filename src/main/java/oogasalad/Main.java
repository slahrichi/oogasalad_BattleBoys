package oogasalad;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;
import oogasalad.model.GameManager;
import oogasalad.model.GameSetup;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.view.SetupView;

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
        // get number of players along with each player's attributes (starting health,
        // gold, number of ships, board shape) from the PARSER
        // for now just initializing random boards and players to use in game manager

//        Board board1 = new Board(5, 5);
//        Player human1 = new HumanPlayer(board1, 1);
//        Board board2 = new Board(5, 5);
//        Player human2 = new HumanPlayer(board2, 2);
//        List<Player> players = new ArrayList<>();
//        players.add(human1);
//        players.add(human2);
        // Parse and retrieve List of Player records
        // Create List of Player objects

        Game game = new Game(stage);
        game.showSetup();

//        GameManager game = new GameManager(/* player objects */);
//        stage.setScene(game.createScene());

//        SetupView test = new SetupView(new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}});
//        stage.setScene(test.createSetUp());
//        stage.show();

//        GameManager game = new GameManager(players);
//        stage.setScene(game.createScene());
//        stage.show();
    }


}

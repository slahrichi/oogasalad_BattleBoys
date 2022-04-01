package oogasalad;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.stage.Stage;
import oogasalad.model.GameManager;
import oogasalad.model.players.Player;
import oogasalad.view.SetUpView;

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

//        Board board1 = new Board();
//        Player human1 = new HumanPlayer(board1);
//        Board board2 = new Board();
//        Player human2 = new HumanPlayer(board2);
//        List<Player> players = new ArrayList<>();
//        players.add(human1);
//        players.add(human2);

//        GameManager game = new GameManager(new ArrayList<Player>());
//        stage.setScene(game.createScene());

        SetUpView test = new SetUpView();
        stage.setScene(test.createSetUp());

        stage.show();
    }
}

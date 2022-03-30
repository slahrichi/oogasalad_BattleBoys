package oogasalad;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import oogasalad.view.BoardView;
import oogasalad.view.ShapeType;

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
        BorderPane pane = new BorderPane();
        int[][] arr = new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        BoardView view = new BoardView(new ShapeType(), arr, 0);
        Controller c = new Controller(view);
        pane.setCenter(view.getBoard());
        Scene scene = new Scene(pane, 500, 500, Color.LIGHTGRAY);
        stage.setScene(scene);
        stage.show();

    }
}

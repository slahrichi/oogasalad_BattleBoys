package oogasalad;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import oogasalad.view.BoardView;
import oogasalad.view.ShapeType;
import oogasalad.view.View;

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
        View view = new View();
        Controller c = new Controller(view);
        stage.setScene(view.getScene());
        stage.show();

    }
}

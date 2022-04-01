package oogasalad.view;

import java.util.List;
import java.util.Stack;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import oogasalad.view.board.BoardView;
import oogasalad.view.board.SetupBoardView;
import oogasalad.view.board.ShapeType;

public class SetUpView {

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final String SCREEN_TITLE = "Set Up Your Ships";
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "stylesheet.css";

  private BorderPane myPane;
  private StackPane myCenterPane;
  private Scene myScene;
  private HBox titleBox;
  private VBox configBox;

  public SetUpView(){

    myPane = new BorderPane();
    myPane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

    myCenterPane = new StackPane();
    titleBox = new HBox();
    configBox = new VBox();

    createTitlePanel();
    createCenterPanel();
    createConfigPanel();

  }

  public Scene createSetUp(){
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
    return myScene;
  }

  private void createTitlePanel(){
    titleBox.setId("titleBox");
    myPane.setTop(titleBox);
    titleBox.getChildren().add(new Label(SCREEN_TITLE));

  }

  private void createConfigPanel(){
    configBox.setId("configBox");
    myPane.setRight(configBox);
    configBox.getChildren().add(new Label("Test"));




  }

  private void createCenterPanel(){
    myCenterPane.setId("boardBox");
    myPane.setCenter(myCenterPane);

    BoardView board = new SetupBoardView(new ShapeType(), new int[][]{{0, 1, 0}, {1, 1, 1}, {0, 1, 0}}, 1);
    board.getBoardPane().setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), null)));
    myCenterPane.getChildren().add(board.getBoardPane());


  }


}

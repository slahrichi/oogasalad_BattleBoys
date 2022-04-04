package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.view.board.SetupBoardView;
import oogasalad.view.board.ShapeType;
import oogasalad.view.panes.LegendPane;
import oogasalad.view.panes.SetShipPane;

public class SetupView extends PropertyObservable implements PropertyChangeListener, ErrorDisplayer, BoardVisualizer {

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final String SCREEN_TITLE = "Set Up Your Ships";
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "stylesheet.css";

  private BorderPane myPane;
  private StackPane myCenterPane;
  private SetupBoardView setupBoard;
  private Scene myScene;
  private HBox titleBox;
  private VBox configBox;
  private LegendPane legendPane;
  private SetShipPane shipPane;

  private String currentPlayerTitle;

  // current piece that is being placed
  private List<Coordinate> currentPiece;

  public SetupView(List<Player> playerList) {

    myPane = new BorderPane();
    myPane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    currentPiece = new ArrayList<>();
    myCenterPane = new StackPane();
    titleBox = new HBox();
    configBox = new VBox();
    legendPane = new LegendPane();
    shipPane = new SetShipPane(new Coordinate[][]{{new Coordinate(1, 1), new Coordinate(2, 2)}});

    currentPlayerTitle = "Player 1: " + SCREEN_TITLE;

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

  public void setCurrentPiece(List<Coordinate> nextPiece) {
    currentPiece = nextPiece;
  }

  private void createTitlePanel(){
    titleBox.setId("titleBox");
    myPane.setTop(titleBox);
    Label titleLabel = new Label(currentPlayerTitle);
    titleLabel.setId("titleText");
    titleBox.getChildren().add(titleLabel);
  }

  private void createConfigPanel(){
    configBox.setId("configBox");
    myPane.setRight(configBox);
    configBox.getChildren().add(new Label("Test"));
    configBox.getChildren().addAll(shipPane.getShipPane(), legendPane.getLegendPane());

  }

  private void createCenterPanel(){

    myPane.setCenter(myCenterPane);
    myCenterPane.setId("boardBox");

    setupBoard = new SetupBoardView(new ShapeType(), new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}}, 1);
    setupBoard.addObserver(this);
    setupBoard.getBoardPane().setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(0), null)));
    myCenterPane.getChildren().add(setupBoard.getBoardPane());
  }


  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    Info info = (Info) evt.getNewValue();
    notifyObserver("Inside SetupView", new Coordinate(info.row(), info.col()));
  }

  public void setCurrentPlayerNum(int playerNum) {
    currentPlayerTitle = "Player " + playerNum + ": " + SCREEN_TITLE;
  }

  @Override
  public void placePiece(Collection<Coordinate> coords, String type) {
    for(Coordinate c : coords) {
      setupBoard.setColorAt(c.getRow(), c.getColumn(), Color.BLACK);
    }
  }

  @Override
  public void removePiece(Collection<Coordinate> coords) {
    for(Coordinate c : coords) {
      setupBoard.setColorAt(c.getRow(), c.getColumn(), Color.LIGHTBLUE);
    }
  }

  public void clearBoard() {
    setupBoard.clear();
  }

  @Override
  public void showError(String errorMsg) {
    Alert alert = new Alert(AlertType.ERROR, errorMsg);
    Node alertNode = alert.getDialogPane();
    alertNode.setId("alert");
    alert.showAndWait();
  }

  @Override
  public void showErrorAndQuit(String errorMsg) {
    Alert alert = new Alert(AlertType.ERROR, errorMsg);
    Node alertNode = alert.getDialogPane();
    alertNode.setId("alert");

    alert.showAndWait();
    Platform.exit();
    System.exit(0);

  }
}

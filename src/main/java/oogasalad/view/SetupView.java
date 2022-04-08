package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.view.board.BoardView;
import oogasalad.view.board.SetupBoardView;
import oogasalad.view.board.BoardShapeType;
import oogasalad.view.panes.LegendPane;
import oogasalad.view.panes.SetShipPane;

public class SetupView extends PropertyObservable implements PropertyChangeListener, ErrorDisplayer, BoardVisualizer {

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final String SCREEN_TITLE = ": Set Up Your Ships";
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "stylesheet.css";

  private BorderPane myPane;
  private Button confirm;
  private VBox centerBox;
  private StackPane myCenterPane;
  private BoardView setupBoard;
  private Scene myScene;
  private HBox titleBox;
  private VBox configBox;
  private LegendPane legendPane;
  private SetShipPane shipPane;
  private Label title;


  private int currentPlayer;

  // current piece that is being placed
  private Collection<Coordinate> currentPiece;

  public SetupView(int[][] board) {
    myPane = new BorderPane();
    myPane.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    currentPiece = new ArrayList<>();
    myCenterPane = new StackPane();
    titleBox = new HBox();
    configBox = new VBox();
    centerBox = new VBox();
    legendPane = new LegendPane();
    shipPane = new SetShipPane(100, 100);

    setupBoard = new SetupBoardView(new BoardShapeType(600, 600), board, 0);

    currentPlayer = 1;

    title = new Label("Player " + currentPlayer + SCREEN_TITLE);
    title.setId("titleText");

    createTitlePanel();
    createConfirm();
    createCenterPanel();
    createConfigPanel();
  }

  public void activateConfirm() {
    confirm.setDisable(false);
  }

  public void displayCompletion() {shipPane.showListCompletion();}

  public Scene getScene() {
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
    return myScene;
  }

  public void setCurrentPiece(Collection<Coordinate> nextPiece) {
    currentPiece = nextPiece;
    shipPane.updateShownPiece(nextPiece);
  }

  private void createTitlePanel(){
    titleBox.setId("titleBox");
    myPane.setTop(titleBox);
    titleBox.getChildren().add(title);
  }

  private void createConfigPanel(){
    configBox.setId("configBox");
    myPane.setRight(configBox);
    configBox.getChildren().add(new Label("Test"));
    configBox.getChildren().addAll(shipPane.getShipPane(), legendPane.getLegendPane());

  }

  private void createConfirm() {
    confirm = new Button("Confirm");
    confirm.setDisable(true);
    confirm.setOnAction(e -> handleConfirm());
  }

  private void handleConfirm() {
    setCurrentPlayerNum();
    clearBoard();
    confirm.setDisable(true);
    notifyObserver("moveToNextPlayer", null);
  }

  private void createCenterPanel(){
    centerBox.getChildren().addAll(myCenterPane, confirm);
    centerBox.setAlignment(Pos.CENTER);
    centerBox.setSpacing(20);
    myPane.setCenter(centerBox);

    myCenterPane.setId("boardBox");
    setupBoard.addObserver(this);
    myCenterPane.getChildren().addAll(setupBoard.getBoardPane());
  }


  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (confirm.isDisabled()) {
      Info info = (Info) evt.getNewValue();
      notifyObserver("placePiece", new Coordinate(info.row(), info.col()));
    }
  }

  public void setCurrentPlayerNum() {
    currentPlayer++;
    updateTitle();
  }

  private void updateTitle() {
    titleBox.getChildren().remove(title);
    title.setText("Player " + currentPlayer + SCREEN_TITLE);
    titleBox.getChildren().add(title);
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

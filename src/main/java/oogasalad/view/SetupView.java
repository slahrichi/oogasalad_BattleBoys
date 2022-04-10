package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.board.BoardView;
import oogasalad.view.board.SetupBoardView;
import oogasalad.view.panels.TitlePanel;
import oogasalad.view.panes.LegendPane;
import oogasalad.view.panes.SetShipPane;

public class SetupView extends PropertyObservable implements PropertyChangeListener, ErrorDisplayer,
    BoardVisualizer {

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final String SCREEN_TITLE = ": Set Up Your Ships";
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "setupStylesheet.css";


  private BorderPane myPane;
  private Button confirm;
  private VBox centerBox;
  private StackPane myCenterPane;
  private BoardView setupBoard;
  private Scene myScene;
  private TitlePanel myTitle;
  private VBox configBox;
  private LegendPane legendPane;
  private SetShipPane shipPane;


  private int currentPlayer;

  // current piece that is being placed
  private Collection<Coordinate> currentPiece;

  public SetupView(CellState[][] board) {
    myPane = new BorderPane();
    myPane.setBackground(
        new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

    currentPiece = new ArrayList<>();
    setupBoard = new SetupBoardView(50, board, 0);
    currentPlayer = 1;

    createTitlePanel();
    createConfirmButton();
    createCenterPanel();
    createConfigPanel();

  }

  public void activateConfirm() {
    confirm.setDisable(false);
  }

  public void displayCompletion() {
    shipPane.showListCompletion();
  }

  public Scene getScene() {
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + STYLESHEET).toExternalForm());
    return myScene;
  }

  public void setCurrentPiece(Collection<Coordinate> nextPiece) {
    currentPiece = nextPiece;
    shipPane.updateShownPieces(List.of(nextPiece));
  }

  private void createConfigPanel() {

    // FIXME: Move magic numbers to private static / resourcebundle

    configBox = new VBox();
    legendPane = new LegendPane();
    shipPane = new SetShipPane(20);
    shipPane.setText("Current Ship");

    configBox.setId("configBox");
    configBox.setMinWidth(300);
    myPane.setRight(configBox);
    configBox.getChildren().addAll(shipPane, legendPane);

  }

  private void createConfirmButton() {
    confirm = new Button("Confirm");
    confirm.setPrefHeight(40);
    confirm.setPrefWidth(80);
    confirm.setDisable(true);
    confirm.setOnAction(e -> handleConfirm());
  }

  private void handleConfirm() {
    setCurrentPlayerNum();
    clearBoard();
    confirm.setDisable(true);
    notifyObserver("moveToNextPlayer", null);
  }

  private void createCenterPanel() {
    myCenterPane = new StackPane();
    centerBox = new VBox();

    centerBox.getChildren().addAll(myCenterPane, confirm);
    centerBox.setAlignment(Pos.CENTER);
    centerBox.setSpacing(20);
    myPane.setCenter(centerBox);

    myCenterPane.setId("boardBox");
    setupBoard.addObserver(this);
    myCenterPane.getChildren().addAll(setupBoard.getBoardPane());
  }

  private void createTitlePanel() {
    myTitle = new TitlePanel("Player " + currentPlayer + SCREEN_TITLE);
    myPane.setTop(myTitle);
  }


  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (confirm.isDisabled()) {
      Info info = (Info) evt.getNewValue();
      notifyObserver("placePiece", new Coordinate(info.row(), info.col()));
    }
  }

  // FIXME: Make it so that we take player number from the player's list

  public void setCurrentPlayerNum() {
    currentPlayer++;
    updateTitle();
  }

  private void updateTitle() {
    myTitle.changeTitle("Player " + currentPlayer + SCREEN_TITLE);
  }

  @Override
  public void placePiece(Collection<Coordinate> coords, CellState type) {
    for (Coordinate c : coords) {
      setupBoard.setColorAt(c.getRow(), c.getColumn(), Color.BLACK);
    }
  }

  @Override
  public void removePiece(Collection<Coordinate> coords) {
    for (Coordinate c : coords) {
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

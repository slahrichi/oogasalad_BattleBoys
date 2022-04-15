package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.LinkedHashMap;
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
import oogasalad.view.interfaces.BoardVisualizer;
import oogasalad.view.interfaces.ErrorDisplayer;
import oogasalad.view.maker.ButtonMaker;
import oogasalad.view.panels.TitlePanel;
import oogasalad.view.panes.LegendPane;
import oogasalad.view.panes.SetPiecePane;

public class SetupView extends PropertyObservable implements PropertyChangeListener, ErrorDisplayer,
    BoardVisualizer {

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final String SCREEN_TITLE = ": Set Up Your Ships";
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "stylesheets/setupStylesheet.css";


  private BorderPane myPane;
  private Button confirmButton;
  private VBox centerBox;
  private StackPane myCenterPane;
  private BoardView setupBoard;
  private Scene myScene;
  private TitlePanel myTitle;
  private VBox configBox;
  private LegendPane legendPane;
  private SetPiecePane shipPane;
  private PassComputerMessageView passComputerMessageView;
  private CellState[][] myCellBoard;
  private int currentPlayer;

  public SetupView(CellState[][] board) {
    myPane = new BorderPane();
    myPane.setBackground(
        new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    myPane.setId("setup-view-pane");
    myCellBoard = board;
    setupBoard = new SetupBoardView(50, myCellBoard, 0);
    currentPlayer = 1;

    createTitlePanel();
    createConfirmButton();
    createCenterPanel();
    createConfigPanel();
    createPassMessageView();
  }

  private void createPassMessageView() {
    passComputerMessageView = new PassComputerMessageView();
    passComputerMessageView.setButtonOnMouseClicked(e -> myScene.setRoot(myPane));
  }

  public void activateConfirm() {
    confirmButton.setDisable(false);
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
    shipPane.updateShownPieces(List.of(nextPiece));
  }

  private void createConfigPanel() {
    // FIXME: Move magic numbers to private static / resourcebundle

    setupLegendPane();
    shipPane = new SetPiecePane(20);
    shipPane.setText("Current Ship");

    configBox = BoxMaker.makeVBox("configBox", 20, Pos.CENTER, shipPane, legendPane);
    configBox.setMinWidth(300);
    myPane.setRight(configBox);
  }

  private void setupLegendPane() {
    LinkedHashMap<String, Color> colorMap = new LinkedHashMap<>();
    for(CellState state : CellState.values()) {
      colorMap.put(state.name(), Color.valueOf(GameView.CELL_STATE_RESOURCES.getString(GameView.FILL_PREFIX + state.name())));
    }
    legendPane = new LegendPane(colorMap);
  }

  private void createConfirmButton() {
    confirmButton = ButtonMaker.makeTextButton("confirm-button", e -> handleConfirm(), "Confirm");
    confirmButton.setDisable(true);
  }

  private void handleConfirm() {
    setCurrentPlayerNum();
    switchPlayerMessage(String.valueOf(currentPlayer));
    clearBoard();
    confirmButton.setDisable(true);
    notifyObserver("moveToNextPlayer", null);
  }

  private void createCenterPanel() {
    setupBoard.addObserver(this);

    myCenterPane = new StackPane();
    myCenterPane.setId("boardBox");
    centerBox = BoxMaker.makeVBox("setup-center-box", 20, Pos.CENTER, myCenterPane, confirmButton);
    myPane.setCenter(centerBox);
    myCenterPane.getChildren().add(setupBoard.getBoardPane());
  }

  private void createTitlePanel() {
    myTitle = new TitlePanel("Player " + currentPlayer + SCREEN_TITLE);
    myTitle.setId("setup-title");
    myPane.setTop(myTitle);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (confirmButton.isDisabled()) {
      Info info = (Info) evt.getNewValue();
      notifyObserver("placePiece", new Coordinate(info.row(), info.col()));
    }
  }

  // FIXME: Make it so that we take player number from the player's list

  public void setCurrentPlayerNum() {
    currentPlayer++;
    updateTitle();
  }

  private void switchPlayerMessage(String nextPlayer) {
    passComputerMessageView.setPlayerName(nextPlayer);
    myScene.setRoot(passComputerMessageView);
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
    myCenterPane.getChildren().remove(setupBoard.getBoardPane());
    setupBoard = new SetupBoardView(50, myCellBoard, 0);
    setupBoard.addObserver(this);
    myCenterPane.getChildren().add(setupBoard.getBoardPane());
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

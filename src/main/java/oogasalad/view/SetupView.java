package oogasalad.view;

import static oogasalad.view.GameView.CELL_STATE_RESOURCES;
import static oogasalad.view.GameView.FILL_PREFIX;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import oogasalad.PropertyObservable;
import oogasalad.controller.GameSetup;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.board.BoardView;
import oogasalad.view.board.SetupBoardView;
import oogasalad.view.interfaces.BoardVisualizer;
import oogasalad.view.interfaces.ErrorDisplayer;
import oogasalad.view.maker.BoxMaker;
import oogasalad.view.maker.ButtonMaker;
import oogasalad.view.panels.TitlePanel;
import oogasalad.view.panes.LegendPane;
import oogasalad.view.panes.SetPiecePane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetupView extends PropertyObservable implements PropertyChangeListener, ErrorDisplayer,
    BoardVisualizer {

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final String SCREEN_TITLE = ": Set Up Your Ships";
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "stylesheets/setupStylesheet.css";
  private static final Logger LOG = LogManager.getLogger(SetupView.class);


  private BorderPane myPane;
  private Button confirmButton;
  private VBox centerBox;
  private HBox bottomPanel;
  private VBox removePiecePanel;
  private StackPane myCenterPane;
  private Collection<Coordinate> lastPlaced;
  private BoardView setupBoard;
  private Scene myScene;
  private TitlePanel myTitle;
  private VBox configBox;
  private LegendPane legendPane;
  private SetPiecePane shipPane;
  private PassComputerMessageView passComputerMessageView;
  private CellState[][] myCellBoard;
  private int currentPlayerNumber;
  private String currentPlayerName;

  public SetupView(CellState[][] board) {
    myPane = new BorderPane();
    myPane.setBackground(
        new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    myPane.setId("setup-view-pane");
    myCellBoard = board;
    setupBoard = new SetupBoardView(50, myCellBoard, 0);
    currentPlayerNumber = 1;
    currentPlayerName = "Player";
    lastPlaced = new ArrayList<>();

    createTitlePanel();
    createBottomPanel();
    createCenterPanel();
    createConfigPanel();
    createPassMessageView();
  }

  private void createPassMessageView() {
    passComputerMessageView = new PassComputerMessageView();
    passComputerMessageView.setButtonOnMouseClicked(e -> {
      myScene.setRoot(myPane);
      updateTitle("Player");
      promptForName();
    });
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

    configBox = BoxMaker.makeVBox("configBox", 0, Pos.TOP_CENTER, shipPane, legendPane);
    configBox.setMinWidth(300);
    myPane.setRight(configBox);
  }

  private void setupLegendPane() {
    LinkedHashMap<String, Color> colorMap = new LinkedHashMap<>();
    for(CellState state : CellState.values()) {
      colorMap.put(state.name(), Color.valueOf(CELL_STATE_RESOURCES.getString(FILL_PREFIX + state.name())));
    }
    legendPane = new LegendPane(colorMap);
  }

  private void createBottomPanel() {
    confirmButton = ButtonMaker.makeTextButton("confirm-button", e -> handleConfirm(), "Confirm");
    confirmButton.setDisable(true);
    Button removeLastPiece = ButtonMaker.makeTextButton("remove-last-button", e -> removePiece(lastPlaced), "Remove Last Placed Piece");
    Button removeAll = ButtonMaker.makeTextButton("remove-all-button", e -> removeAllPieces(), "Remove All Pieces");
    removePiecePanel = BoxMaker.makeVBox("remove-piece-panel", 10, Pos.CENTER, removeLastPiece, removeAll);
    bottomPanel = BoxMaker.makeHBox("bottom-panel", 20, Pos.CENTER, removePiecePanel, confirmButton);

  }


  public void handleConfirm() {
    currentPlayerNumber++;
    switchPlayerMessage(" "+ currentPlayerNumber);
    clearBoard();
    confirmButton.setDisable(true);
    notifyObserver("moveToNextPlayer", null);
  }

  private void createCenterPanel() {
    setupBoard.addObserver(this);

    myCenterPane = new StackPane();
    myCenterPane.setId("boardBox");
    centerBox = BoxMaker.makeVBox("setup-center-box", 20, Pos.CENTER, myCenterPane, bottomPanel);
    myPane.setCenter(centerBox);
    myCenterPane.getChildren().add(setupBoard.getBoardPane());
  }

  private void createTitlePanel() {
    myTitle = new TitlePanel("Player " + currentPlayerNumber + SCREEN_TITLE);
    myTitle.setId("setup-title");
    myPane.setTop(myTitle);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (confirmButton.isDisabled()) {
      Info info = (Info) evt.getNewValue();
      notifyObserver(evt.getPropertyName(), info.row() + " " + info.col());
    }
  }

  public void promptForName() {
    TextInputDialog dialog = new TextInputDialog();

    dialog.setTitle("Setup");
    dialog.setHeaderText("Player " + currentPlayerNumber + ", enter your name:");
    dialog.setContentText("Name:");

    Optional<String> result = dialog.showAndWait();

    result.ifPresent(name -> {
      currentPlayerName = name;
      updateTitle(currentPlayerName);
      notifyObserver("assignCurrentPlayerName", name);
    });
  }

  // FIXME: Make it so that we take player number from the player's list

  private void switchPlayerMessage(String nextPlayer) {
    passComputerMessageView.setPlayerName(nextPlayer);
    myScene.setRoot(passComputerMessageView);
  }

  private void updateTitle(String playerName) {
    myTitle.changeTitle(playerName + SCREEN_TITLE);
  }

  @Override
  public void placePiece(Collection<Coordinate> coords, CellState type) {
    for (Coordinate c : coords) {
      setupBoard.setColorAt(c.getRow(), c.getColumn(), Paint.valueOf(CELL_STATE_RESOURCES.getString(FILL_PREFIX + type.name())));
    }
    lastPlaced = coords;
  }

  public void setLastPlaced(Collection<Coordinate> coords) {
    lastPlaced = coords;
  }

  @Override
  public void removePiece(Collection<Coordinate> coords) {
    for (Coordinate c : coords) {
      setupBoard.setColorAt(c.getRow(), c.getColumn(), Paint.valueOf(CELL_STATE_RESOURCES.getString(FILL_PREFIX + CellState.WATER.name())));
    }
    confirmButton.setDisable(true);
    notifyObserver("removePiece", null);

  }

  public void removeAllPieces() {
    lastPlaced = new ArrayList<>();
    confirmButton.setDisable(true);
    clearBoard();
    notifyObserver("removeAllPieces", null);
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

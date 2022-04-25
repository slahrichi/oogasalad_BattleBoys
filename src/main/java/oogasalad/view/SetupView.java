package oogasalad.view;

import static oogasalad.view.GameView.CELL_STATE_RESOURCES;
import static oogasalad.view.GameView.FILL_PREFIX;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.board.BoardView;
import oogasalad.view.board.SetupBoardView;
import oogasalad.view.interfaces.BoardVisualizer;
import oogasalad.view.interfaces.ErrorDisplayer;
import oogasalad.view.maker.BoxMaker;
import oogasalad.view.maker.ButtonMaker;
import oogasalad.view.maker.DialogMaker;
import oogasalad.view.panels.TitlePanel;
import oogasalad.view.panes.LegendPane;
import oogasalad.view.panes.SetPiecePane;
import oogasalad.view.screens.AbstractScreen;
import oogasalad.view.screens.PassComputerScreen;
import oogasalad.view.screens.SetUpShipsScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Purpose: The purpose of the SetupView is the GUI for users to set up their pieces/ships. It's a
 * border pane that contains a board and the ships that are about to be placed.
 * <p>
 * Assumption: After the start button is clicked on the StartView and the user selects a file, the
 * user is directed to the SetupView to select ships to place down.
 * <p>
 * Dependencies: Depends on the StartView and GameSetup to correctly create and use it. It also
 * depends on the controller to correctly pass the ships that it placed on the Board to the
 * backend.
 * <p>
 * Created in the GameSetup and handles its integration with the backend
 *
 * @author: Eric Xie, Minjun Kwak, Edison Ooi
 */
public class SetupView extends PropertyObservable implements PropertyChangeListener, ErrorDisplayer,
    BoardVisualizer {

  private ResourceBundle myResources;

  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String DAY_STYLESHEET = "stylesheets/setupStylesheet.css";

  private static final Logger LOG = LogManager.getLogger(SetupView.class);
  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;
  private static final double BOARD_SIZE = 50;
  private static final int SETUP_BOARD_ID = 0;
  private static final int CURRENT_PLAYER_NUMBER = 1;
  private static final double SET_PIECE_PANE_SIZE = 20;
  private static final int NO_SPACING = 0;
  private static final int SMALL_SPACING = 10;
  private static final int LARGE_SPACING = 20;
  private static final int CONFIG_BOX_WIDTH = 300;

  private static final String INVALID_METHOD = "Invalid method name given";
  private static final String PLACE_PIECE = "placePiece";
  private static final String MOVE_NEXT_PLAYER_OPERATION = "moveToNextPlayer";
  private static final String ASSIGN_CURRENT_PLAYER_OPERATION = "assignCurrentPlayerName";
  private static final String REMOVE_PIECE_OPERATION = "removePiece";
  private static final String REMOVE_ALL_OPERATION = "removeAllPieces";

  // ID Strings
  private static final String SETUP_PANE_ID = "setup-view-pane";
  private static final String CONFIG_BOX_ID = "configBox";
  private static final String CONFIRM_BTN_ID = "confirm-button";
  private static final String REMOVE_LAST_ID = "remove-last-button";
  private static final String REMOVE_ALL_ID = "remove-all-button";
  private static final String REMOVE_PIECE_ID = "remove-piece-panel";
  private static final String BOTTOM_PANEL_ID = "bottom-panel";
  private static final String BOARD_BOX_ID = "boardBox";
  private static final String SETUP_CENTER_ID = "setup-center-box";
  private static final String SETUP_TITLE_ID = "setup-title";
  private static final String PLAYER_NAME_ID = "player-name";
  private static final String OK_BTN_ID = "ok-button";
  private static final String SETUP_ALERT_ID = "setupview-alert";

  // Resource Strings
  private static final String SETUP_TITLE_RESOURCE = "SetupTitlePrefix";
  private static final String CURRENT_SHIP_RESOURCE = "CurrentShip";
  private static final String CONFIRM_BUTTON_RESOURCE = "ConfirmButton";
  private static final String REMOVE_LAST_RESOURCE = "RemoveLastButton";
  private static final String REMOVE_ALL_RESOURCE = "RemoveAllButton";
  private static final String PROMPT_PREFIX_RESOURCE = "PromptPrefix";
  private static final String PROMPT_SUFFIX_RESOURCE = "PromptSuffix";
  private static final String ENTER_NAME_RESOURCE = "EnterName";
  private static final String LEGEND_KEY_RESOURCE = "LegendText";
  private static final String PROMPT_LABEL_RESOURCE = "PromptLabel";

  private BorderPane myPane;
  private VBox centerBox;
  private HBox bottomPanel;
  private VBox removePiecePanel;
  private VBox configBox;
  private Button confirmButton;

  private StackPane myCenterPane;
  private Collection<Coordinate> lastPlaced;
  private Collection<Coordinate> nextToPlace;
  private BoardView setupBoard;
  private Scene myScene;
  private TitlePanel myTitle;

  private LegendPane legendPane;
  private SetPiecePane shipPane;
  private AbstractScreen passComputerMessageView;
  private CellState[][] myCellBoard;

  private int currentPlayerNumber;
  private String currentPlayerName;
  private String SCREEN_TITLE;

  /**
   * Purpose: The constructor for SetupView objects
   * <p>
   * Assumed to be used correctly by the GameSetup class in being created and displayed at the
   * correct time; Also assumed to be correctly passed a CellState[][] board and the correct
   * ResourceBundle
   * <p>
   * Exceptions can occur if passed the incorrect parameters
   *
   * @param board,          a 2D array of CellState to be displayed to the user placing their ships
   * @param resourceBundle, the ResourceBundle to be used for program language
   */
  public SetupView(CellState[][] board, ResourceBundle resourceBundle) {
    myPane = new BorderPane();
    myPane.setId(SETUP_PANE_ID);
    myCellBoard = board;
    setupBoard = new SetupBoardView(BOARD_SIZE, myCellBoard, SETUP_BOARD_ID);
    lastPlaced = new ArrayList<>();
    myResources = resourceBundle;
    currentPlayerNumber = CURRENT_PLAYER_NUMBER;
    currentPlayerName = myResources.getString(PROMPT_PREFIX_RESOURCE);
    nextToPlace = new ArrayList<>();
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    SCREEN_TITLE = myResources.getString(SETUP_TITLE_RESOURCE);
    createTitlePanel();
    createBottomPanel();
    createCenterPanel();
    createConfigPanel();
    createPassMessageView();
  }

  /**
   * Displays the intro screen for the SetupView
   * <p>
   * Assumed to be used by the GameSetup when necessary to display the intro screen.
   *
   * @return void
   */
  public void displayIntroScreen() {
    SetUpShipsScreen screen = new SetUpShipsScreen(myResources);
    myScene.setRoot(screen);
  }

  // create a pass computer message to other users
  private void createPassMessageView() {
    passComputerMessageView = new PassComputerScreen(e -> {
      myScene.setRoot(myPane);
      updateTitle(myResources.getString(PROMPT_PREFIX_RESOURCE) + currentPlayerNumber);
      promptForName();
    }, myResources);
  }


  public void displayAIShipsPlaced() {

  }

  /**
   * Used by the GameSetup to activate the confirm button whenever the user finishes placing all
   * their pieces
   * <p>
   * Assumed to be used correctly when the user finishes placing all their pieces and the button to
   * be disabled when the user isn't
   */
  public void activateConfirm() {
    confirmButton.setDisable(false);
  }

  /**
   * Used to change the shipPane to show all the pieces have been completely placed
   * <p>
   * Assumed to be shown after all pieces are detected to be placed by the user
   */
  public void displayCompletion() {
    nextToPlace = new ArrayList<>();
    shipPane.showListCompletion();
  }

  /**
   * Creates the scene of the SetupView to be shown on the stage inside the GameSetup class
   * <p>
   * Assumed to be created when it's time for the SetupView to be displayed
   *
   * @return Scene, the Scene of the created SetupView GUI
   */
  public Scene getScene() {
    myScene.getStylesheets()
        .add(getClass().getResource(DEFAULT_RESOURCE_PACKAGE + DAY_STYLESHEET).toExternalForm());
    return myScene;
  }

  /**
   * Updates the nextToPlace variable of the next piece that should be placed and tells the Ship
   * Pane to update itself to show the nextPiece
   *
   * @param nextPiece, the next piece/ship that should be displayed on the pane
   */
  public void setCurrentPiece(Collection<Coordinate> nextPiece) {
    nextToPlace = nextPiece;
    shipPane.updateShownPieces(List.of(nextPiece));
  }

  // creates the configuration panel on the right side
  private void createConfigPanel() {
    setupLegendPane();
    shipPane = new SetPiecePane(SET_PIECE_PANE_SIZE, myResources);
    shipPane.setText(myResources.getString(CURRENT_SHIP_RESOURCE));
    configBox = BoxMaker.makeVBox(CONFIG_BOX_ID, NO_SPACING, Pos.TOP_CENTER, shipPane, legendPane);
    configBox.setMinWidth(CONFIG_BOX_WIDTH);
    myPane.setRight(configBox);
  }

  // creates the legend pane on the right side that displays the key to the colors of the board
  private void setupLegendPane() {
    legendPane = new LegendPane(myResources);
  }

  // creates the bottom panel of the setupview, showing the buttons that allow the user to unplace and confirm
  // the placement of their ships
  private void createBottomPanel() {
    confirmButton = ButtonMaker.makeTextButton(CONFIRM_BTN_ID, e -> handleConfirm(),
        myResources.getString(CONFIRM_BUTTON_RESOURCE));
    confirmButton.setDisable(true);
    Button removeLastPiece = ButtonMaker.makeTextButton(REMOVE_LAST_ID,
        e -> removePiece(lastPlaced), myResources.getString(REMOVE_LAST_RESOURCE));
    Button removeAll = ButtonMaker.makeTextButton(REMOVE_ALL_ID, e -> removeAllPieces(),
        myResources.getString(REMOVE_ALL_RESOURCE));
    removePiecePanel = BoxMaker.makeVBox(REMOVE_PIECE_ID, SMALL_SPACING, Pos.CENTER,
        removeLastPiece, removeAll);
    bottomPanel = BoxMaker.makeHBox(BOTTOM_PANEL_ID, LARGE_SPACING, Pos.CENTER, removePiecePanel,
        confirmButton);
  }


  /**
   * Handles the confirm button when it's pressed by the user
   * <p>
   * Assumed to correctly be used in the GameSetup to notify the class to move on to the next stage
   * or the next part of the program
   */
  public void handleConfirm() {
    currentPlayerNumber++;
    clearBoard();
    confirmButton.setDisable(true);
    notifyObserver(MOVE_NEXT_PLAYER_OPERATION, null);
  }

  // creates the center panel of the setupview that displays the board
  private void createCenterPanel() {
    setupBoard.addObserver(this);

    myCenterPane = new StackPane();
    myCenterPane.setId(BOARD_BOX_ID);
    centerBox = BoxMaker.makeVBox(SETUP_CENTER_ID, LARGE_SPACING, Pos.CENTER, myCenterPane,
        bottomPanel);
    myPane.setCenter(centerBox);
    myCenterPane.getChildren().add(setupBoard.getBoardPane());
  }

  // creates the title panel for the setupview
  private void createTitlePanel() {
    myTitle = new TitlePanel(
        myResources.getString(PROMPT_PREFIX_RESOURCE) + currentPlayerNumber + SCREEN_TITLE);
    myTitle.setId(SETUP_TITLE_ID);
    myPane.setTop(myTitle);
  }

  /**
   * Used to perform reflection to show when a cell is highlighted (hovered vs clicked)
   *
   * @param evt, the PropertyChangeEvent that it's passed
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    try {
      Method m = this.getClass().getDeclaredMethod(evt.getPropertyName(), Coordinate.class);
      m.invoke(this, evt.getNewValue());
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
        NullPointerException e) {
      throw new NullPointerException(INVALID_METHOD);
    }
  }

  // This method is called by reflection
  // Notifies the GameSetup class to place a piece at the location specified by coordinate
  private void cellClicked(Coordinate coordinate) {
    if (confirmButton.isDisabled()) {
      notifyObserver(PLACE_PIECE, coordinate.getRow() + " " + coordinate.getColumn());
    }
  }

  // This method is called by reflection
  // Highlights the cells on the board to reflect where the current ship would be placed
  // if clicked
  private void cellHovered(Coordinate coordinate) {
    Collection<Coordinate> coordsToHighlight = initializeCoordsToColor(coordinate);
    for (Coordinate c : coordsToHighlight) {
      if (checkIfValid(c.getRow(), c.getColumn())) {
        setupBoard.setColorAt(c.getRow(), c.getColumn(), Paint.valueOf(
            CELL_STATE_RESOURCES.getString(FILL_PREFIX + CellState.SHIP_HOVER.name())));
      }
    }
  }

  // checks validity of the cell and returns a boolean if true or not
  private boolean checkIfValid(int row, int col) {
    return row >= 0 && row < myCellBoard.length && col >= 0 &&
        col < myCellBoard[0].length && setupBoard.getColorAt(row, col) != Paint.valueOf(
        CELL_STATE_RESOURCES.getString(FILL_PREFIX + CellState.SHIP_HEALTHY.name()));
  }

  // checks if cell exited
  private void cellExited(Coordinate coordinate) {
    Collection<Coordinate> coordsToReplace = initializeCoordsToColor(coordinate);
    for (Coordinate c : coordsToReplace) {
      if (checkIfValid(c.getRow(), c.getColumn())) {
        setupBoard.setColorAt(c.getRow(), c.getColumn(), Paint.valueOf(
            CELL_STATE_RESOURCES.getString(FILL_PREFIX + CellState.WATER.name())));
      }
    }
  }

  // initializes the Coordinates to a Color after being passed a Coordiante and returns a collection of them
  private Collection<Coordinate> initializeCoordsToColor(Coordinate coordinate) {
    List<Coordinate> coords = new ArrayList<>();
    for (Coordinate c : nextToPlace) {
      int absRow = c.getRow() + coordinate.getRow();
      int absCol = c.getColumn() + coordinate.getColumn();
      coords.add(new Coordinate(absRow, absCol));
    }
    return coords;
  }

  /**
   * Prompts the user for their name, creates a popup window for them to input the name
   */
  public void promptForName() {
    TextInputDialog dialog = DialogMaker.makeTextInputDialog(
        myResources.getString(ENTER_NAME_RESOURCE),
        myResources.getString(PROMPT_PREFIX_RESOURCE) + currentPlayerNumber + myResources.getString(
            PROMPT_SUFFIX_RESOURCE), myResources.getString(PROMPT_LABEL_RESOURCE),
        myResources.getString(PROMPT_PREFIX_RESOURCE) + currentPlayerNumber, PLAYER_NAME_ID,
        OK_BTN_ID);
    dialog.getEditor().textProperty().addListener(e -> updateTitle(dialog.getEditor().getText()));
    Optional<String> result = dialog.showAndWait();
    String name;
    if (result.isPresent()) {
      name = dialog.getEditor().getText();
    } else {
      name = myResources.getString(PROMPT_PREFIX_RESOURCE) + currentPlayerNumber;
    }
    currentPlayerName = name;
    updateTitle(currentPlayerName);
    notifyObserver(ASSIGN_CURRENT_PLAYER_OPERATION, name);
  }

  /**
   * Displays a screen that tells the current player to pass the computer to the next player
   *
   * @param nextPlayer Name of next player that should receive computer
   */
  public void displayPassComputerMessage(String nextPlayer) {
    passComputerMessageView.setLabelText(nextPlayer);
    myScene.setRoot(passComputerMessageView);
  }

  // updates the title of the screen
  private void updateTitle(String playerName) {
    myTitle.changeTitle(playerName + SCREEN_TITLE);
  }

  /**
   * Places a Piece of a certain type at the specified coordinates
   *
   * @param coords Coordinates to place Piece at
   * @param type   Type of piece being placed
   */
  @Override
  public void placePiece(Collection<Coordinate> coords, CellState type) {
    for (Coordinate c : coords) {
      setupBoard.setColorAt(c.getRow(), c.getColumn(),
          Paint.valueOf(CELL_STATE_RESOURCES.getString(FILL_PREFIX + type.name())));
    }
    lastPlaced = coords;
  }

  /**
   * Takes a collection of coordinates and sets the lastPlaced variable to it
   *
   * @param coords
   */
  public void setLastPlaced(Collection<Coordinate> coords) {
    lastPlaced = coords;
  }

  /**
   * Removes any Pieces that are at the coordinates contained in coords.
   */
  @Override
  public void removePiece(Collection<Coordinate> coords) {
    for (Coordinate c : coords) {
      setupBoard.setColorAt(c.getRow(), c.getColumn(),
          Paint.valueOf(CELL_STATE_RESOURCES.getString(FILL_PREFIX + CellState.WATER.name())));
    }
    confirmButton.setDisable(true);
    notifyObserver(REMOVE_PIECE_OPERATION, null);
  }

  /**
   * Removes all the pieces on the board and makes the user place them again
   */
  public void removeAllPieces() {
    lastPlaced = new ArrayList<>();
    confirmButton.setDisable(true);
    clearBoard();
    notifyObserver(REMOVE_ALL_OPERATION, null);
  }

  /**
   * Clears the board on which the user places their pieces
   */
  public void clearBoard() {
    myCenterPane.getChildren().remove(setupBoard.getBoardPane());
    setupBoard = new SetupBoardView(BOARD_SIZE, myCellBoard, SETUP_BOARD_ID);
    setupBoard.addObserver(this);
    myCenterPane.getChildren().add(setupBoard.getBoardPane());
  }

  /**
   * Displays an error with a message in a user-friendly way.
   *
   * @param errorMsg message to appear on error
   */
  @Override
  public void showError(String errorMsg) {
    Alert alert = DialogMaker.makeAlert(errorMsg, SETUP_ALERT_ID);
    alert.showAndWait();
  }

}

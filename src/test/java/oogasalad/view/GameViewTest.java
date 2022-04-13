package oogasalad.view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import oogasalad.GameData;
import oogasalad.controller.GameManager;
import oogasalad.model.players.AIPlayer;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.WinConditions.WinCondition;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import util.DukeApplicationTest;
import org.junit.jupiter.api.Test;

public class GameViewTest extends DukeApplicationTest {

  private int numPlayers = 3;
  private Button rightButton;
  private Polygon cell0;
  private Button shopButton;

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  @BeforeEach
  public void setupStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Override
  public void start(Stage stage) {
    List<CellState[][]> firstPlayerBoards = new ArrayList<>();
    CellState[][] selfBoard = new CellState[][]{
        {CellState.WATER, CellState.WATER, CellState.SHIP_HEALTHY, CellState.SHIP_HEALTHY},
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.SHIP_HEALTHY},
        {CellState.SHIP_HEALTHY, CellState.WATER, CellState.WATER, CellState.WATER},
        {CellState.SHIP_HEALTHY, CellState.SHIP_HEALTHY, CellState.WATER, CellState.WATER}};
    CellState[][] markerBoards = new CellState[][]{
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER},
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER},
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER},
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER}};
    firstPlayerBoards.add(selfBoard);
    for (int i = 1; i < numPlayers; i++) {
      firstPlayerBoards.add(markerBoards);
    }
    GameView view = new GameView(firstPlayerBoards);
    stage.setScene(view.createScene());
    stage.show();

    cell0 = lookup("#view-pane #view-center-pane #board-view #board-view-base #cell-view-0-0-0").query();
    rightButton = lookup("#view-pane #view-center-pane #board-button-box #right-button").query();
    shopButton = lookup("#view-pane #configBox #view-shop").query();
  }

  @Test
  public void testSwitchBoard() {
    rightClickOn(rightButton);
    //Polygon cell1 = lookup("#view-pane #view-center-pane #board-view #board-view-base #cell-view-0-0-0").query();
//    clickOn(cell1);
  }

  @Test
  public void testOpenShop() {

    rightClickOn(shopButton);
    assertEquals("Shop Opened\n", outContent.toString());
  }
}

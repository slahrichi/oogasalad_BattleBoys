package oogasalad.view;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.KeyCode;
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
import util.DukeApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ViewTest extends DukeApplicationTest {

  private Polygon myCell0;
  private CellState[][] cellBoard;
  private Player p1;
  private Player p2;
  
  @BeforeEach
  void setup() {
    int[][] board = new int[][]{{1, 1, 1}, {0, 1, 1}};
    cellBoard = new CellState[board.length][board[0].length];
    for (int i = 0; i < cellBoard.length; i++) {
      for (int j = 0; j < cellBoard[0].length; j++) {
        cellBoard[i][j] = CellState.WATER;
      }
    }
    Board b1 = new Board(cellBoard);
    Board b2 = new Board(cellBoard);
    p1 = new HumanPlayer(b1, 0, null);
    p2 = new AIPlayer(b2, 1, null);
  }

  @Override
  public void start (Stage stage) {
    // create application and add scene for testing to given stage
    GameManager game = new GameManager(new GameData(List.of(p1, p2), cellBoard, new ArrayList<Piece>(), new ArrayList<WinCondition>()));
    stage.setScene(game.createScene());
    stage.show();
    myCell0 = lookup("#view-pane #view-center-pane #board-view #board-view-base #cell-view-0-0-0").query();
  }

  @Test
  public void testFireShot() {
    clickOn(myCell0);
    assertEquals(myCell0.getFill(), Color.RED);
  }

  @Test
  public void testSwitchBoard() {
    
  }

  @Test
  public void testSetupView() {
    List<Player> players = new ArrayList<>();
    SetupView view = new SetupView(new CellState[][]{});
  }
}

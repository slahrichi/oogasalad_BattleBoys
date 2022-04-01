package oogasalad.view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import oogasalad.model.GameManager;
import oogasalad.model.players.Player;
import util.DukeApplicationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class ViewTest extends DukeApplicationTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  private Polygon myCell0;

  @BeforeEach
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Override
  public void start (Stage stage) {
    // create application and add scene for testing to given stage
    GameManager game = new GameManager(new ArrayList<Player>());
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
    clickOn(myCell0);
    assertEquals("ID: 0\n", outContent.toString());
    press(KeyCode.RIGHT);
    Polygon myCell1 = lookup("#view-pane #view-center-pane #board-view #board-view-base #cell-view-1-1-1").query();
    clickOn(myCell1);
    assertEquals("ID: 0\n"
        + "Showing board 1\n"
        + "ID: 1\n", outContent.toString());
  }

  @Test
  public void testSetupView() {
    SetupView view = new SetupView();
  }
}

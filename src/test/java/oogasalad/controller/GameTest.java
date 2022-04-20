package oogasalad.controller;

import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.DukeApplicationTest;

public class GameTest extends DukeApplicationTest {
  private Game game;

  @BeforeEach
  void setup() {

  }

  @Test
  void testBasicGame() {
    javafxRun(() -> game = new Game(new Stage()));
  }

}

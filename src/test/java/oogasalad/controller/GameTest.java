package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import oogasalad.view.StartView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import util.DukeApplicationTest;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Comprehensive testing for Game
 *
 * @author Matthew Giglio
 */
public class GameTest extends DukeApplicationTest {

private Game spy;
private File file;

  private final ResourceBundle myResources = ResourceBundle.getBundle("/languages/English");


  @BeforeEach
  void setup() {
    file = new File(System.getProperty("user.dir") + "/data/ExampleDataFile.properties");
  }

  @Test
  void testBasicGame() throws InterruptedException{
    javafxRun(() ->
    {
      spy = Mockito.spy(new Game(new Stage()));
      Mockito.doReturn(file).when(spy).chooseDataFile();
      spy.showStart();
      spy.propertyChange(new PropertyChangeEvent(new StartView(myResources),
          "loadFile", null, null));
    });
    Thread.sleep(3000);
    assertNotEquals(null, lookup("#pass-computer-message-button").query());
  }

  @Test
  void testInvalidFile() throws InterruptedException{
    javafxRun(() ->
    {
      spy = Mockito.spy(new Game(new Stage()));
      Mockito.doReturn(new File(System.getProperty("user.dir") +
          "/data/FakeFile.properties")).when(spy).chooseDataFile();
      spy.showStart();
      spy.propertyChange(new PropertyChangeEvent(new StartView(myResources),
          "loadFile", null, null));
    });
    Thread.sleep(1500);
    assertNotEquals(null, lookup("#game-alert").query());
  }

  @Test
  void testInvalidMethod() {
    javafxRun(() ->
    {
      spy = Mockito.spy(new Game(new Stage()));
      Mockito.doReturn(file).when(spy).chooseDataFile();
      spy.showStart();
      assertThrows(NullPointerException.class, () ->
          spy.propertyChange(new PropertyChangeEvent(new StartView(myResources),
          "fakeMethod", null, null)));
    });
  }

  @Test
  void testStartGame() throws InterruptedException {
    javafxRun(() ->
    {
      spy = Mockito.spy(new Game(new Stage()));
      Mockito.doReturn(file).when(spy).chooseDataFile();
      spy.showStart();
      spy.propertyChange(new PropertyChangeEvent(new StartView(myResources),
          "loadFile", null, null));
      javafxRun(() -> spy.propertyChange(new PropertyChangeEvent(new StartView(myResources),
          "startGame", null, null)));
    });
    Thread.sleep(1000);
    assertNotEquals(null, lookup("#view-shop").query());

  }
}

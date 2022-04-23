package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import oogasalad.view.StartView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import util.DukeApplicationTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GameTest extends DukeApplicationTest {

private Game spy;

  private final ResourceBundle myResources = ResourceBundle.getBundle("/languages/English");


  @BeforeEach
  void setup() {

  }

  @Test
  void testBasicGame() throws InterruptedException{
    javafxRun(() ->
    {
      spy = Mockito.spy(new Game(new Stage()));
      Mockito.doReturn(new File(System.getProperty("user.dir") + "/data/ExampleDataFile.properties")).when(spy).chooseDataFile();
      spy.showStart();
      spy.propertyChange(new PropertyChangeEvent(new StartView(myResources),
          "loadFile", null, null));
    });
    Thread.sleep(3000);
    assertNotEquals(null, lookup("#pass-computer-message-button").query());
  }
}

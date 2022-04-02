package oogasalad;

import java.io.File;
import javafx.stage.Stage;

public class FileChooser {

  private static String FILE_CHOOSER_TITLE = "Choose Data File";

  public FileChooser() {

    FileChooser chooser = new FileChooser();
    chooser.setTitle(FILE_CHOOSER_TITLE);
    Stage dialogStage = new Stage();
    File file = chooser.showOpenDialog(dialogStage);
  }
}

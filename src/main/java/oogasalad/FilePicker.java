package oogasalad;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FilePicker {

  private static String FILE_CHOOSER_TITLE = "Choose Data File";

  private FileChooser chooser;
  public FilePicker() {
    chooser = new FileChooser();
  }

  public File getFile() {
<<<<<<< HEAD
=======

    chooser.setInitialDirectory(new File("data"));
    chooser.getExtensionFilters().addAll(new ExtensionFilter("valid save files", "*.properties"));
>>>>>>> master
    chooser.setTitle(FILE_CHOOSER_TITLE);
    Stage dialogStage = new Stage();
    return chooser.showOpenDialog(dialogStage);

  }
}

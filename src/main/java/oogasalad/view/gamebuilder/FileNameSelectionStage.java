package oogasalad.view.gamebuilder;

import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
/**
 * A class for setting up File name for the desired game setting file. To which the parser later
 * will write the user selected data. Depends on JavaFX. No assumptions.
 *
 * @author Luka Mdivani
 */
public class FileNameSelectionStage extends BuilderStage {

  private String fileName;
  private BorderPane myPane;
  private String DEFAULT_FILENAME = "Custom_Game";
  private TextArea fileNameText;

  public FileNameSelectionStage(){
    myPane=new BorderPane();
    fileNameText = makeTextAreaWithDefaultValue(DEFAULT_FILENAME);
    HBox centerPane = new HBox();
    centerPane.getChildren().addAll(fileNameText, makeButton("Choose File Name", e -> saveName()));
    myPane.setCenter(centerPane);
  }
  @Override
  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {

    return null;
  }

  private void saveName() {
    fileName = fileNameText.getText();

    closeWindow();
  }

  @Override
  protected Object launch() {
    setUpStage(myPane);
    return fileName;
  }

  @Override
  protected void saveAndContinue() {
  }
}

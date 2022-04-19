package oogasalad.view.gameBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public abstract class BuilderStage {

  private ResourceBundle myBuilderResources;
  private int selectedType;
  private List<Rectangle> colorOptionList = new ArrayList<>();

  public BuilderStage() {
    myBuilderResources = ResourceBundle.getBundle("/BuilderInfo");
  }

  protected ResourceBundle getMyBuilderResources() {
    return myBuilderResources;
  }

  protected int[][] initializeMatrixWithValue(int height, int width,int initialValue) {
    int[][] stateMap = new int[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        stateMap[i][j] = initialValue;
      }
    }
    return stateMap;
  }

  protected Button makeButton(String name, EventHandler<ActionEvent> handler) {
    Button result = new Button(name);
    result.setOnAction(handler);

    return result;
  }


  protected Group arrangeCells(int height, int width, double cellHeight,double cellWidth, int[][] stateMap) {
    Group cellGroup = new Group();
    double xPos;
    double yPos = 0;
    for (int i = 0; i < height; i++) {
      xPos = 0;
      for (int j = 0; j < width; j++) {
        cellGroup.getChildren().add(createCell(xPos, yPos, i, j, stateMap[i][j]));

        xPos = xPos + cellWidth;
      }
      yPos = yPos + cellHeight;
    }
    return cellGroup;
  }

  protected VBox displayColorChoice(String[] DEFAULT_STATE_OPTIONS, List<Color> colorList) {
    VBox result = new VBox();
    for (Color c : colorList) {
      Rectangle option = createColorOptionRectangle(c, colorList);
      colorOptionList.add(option);
      result.getChildren().add(new HBox(new Text(DEFAULT_STATE_OPTIONS[colorList.indexOf(c)]),
          option));

    }
    result.setId("ColorChoiceBox");

    return result;
  }

  protected int getSelectedType() {
    return selectedType;
  }

  private Rectangle createColorOptionRectangle(Color c, List<Color> colorList) {
    Rectangle result = new Rectangle(50, 25);
    result.setFill(c);
    result.setId("ColorOption");

    result.setOnMouseClicked(e -> {
      removeSelectionStrokeFromOption();
      result.setStroke(Color.RED);
      selectedType = colorList.indexOf(c);
    });
    return result;
  }

  private void removeSelectionStrokeFromOption() {
    for (Rectangle option : colorOptionList) {
      option.setStroke(null);
    }
  }

  protected Button makeContinueButton() {

    Button continueButton = new Button("Continue");
    continueButton.setOnAction(e -> saveAndContinue());
    continueButton.setId("ContinueButton");
    return continueButton;
  }

  protected Boolean checkIntConversion(String string) {
    try {
      int x = Integer.parseInt(string);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  protected ComboBox makeComboBox(String[] options) {
    ComboBox comboBox = new ComboBox();
    for (String option : options) {
      comboBox.getItems().add(option);

    }

    return comboBox;
  }

  protected abstract Rectangle createCell(double xPos, double yPos, int i, int j, int state);

  protected abstract Object saveAndContinue();
}

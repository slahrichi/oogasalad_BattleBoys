package oogasalad.view.gamebuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import oogasalad.model.utilities.tiles.enums.CellState;

public class ColorSelectionStage extends BuilderStage {

  private BorderPane myPane = new BorderPane();
  private String[] optionList;
  private Map<CellState, Color> colorMap;
  private List<ColorPicker> colorPickers = new ArrayList<>();
  private Map<String, ColorPicker> colorPickerMap;

  public ColorSelectionStage() {
    super();
    colorPickerMap = new HashMap<>();
    optionList = getMyBuilderResources().getString("possibleCellState").split(",");
    myPane.setCenter(buildOptionDisplay());
    myPane.setRight(makeContinueButton());


  }

  @Override
  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    return null;
  }

  @Override
  protected Object launch() {
    setUpStage(myPane);
    return colorMap;
  }

  private VBox buildOptionDisplay() {
    VBox result = new VBox();
    int index = 0;
    for (String option : optionList) {
      ColorPicker colorPicker = new ColorPicker();
      colorPickerMap.put(option, colorPicker);
      result.getChildren().add(new HBox(new Text(option), colorPicker));

    }

    return result;
  }

  protected void saveAndContinue() {
    colorMap = new HashMap<>();
    for (String cellStateName : colorPickerMap.keySet()) {
      colorMap.put(CellState.valueOf(cellStateName), colorPickerMap.get(cellStateName).getValue());
    }
    closeWindow();
  }

}

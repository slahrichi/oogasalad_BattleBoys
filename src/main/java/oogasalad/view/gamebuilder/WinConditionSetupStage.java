package oogasalad.view.gamebuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.winconditions.WinCondition;
import oogasalad.model.utilities.winconditions.WinState;
import oogasalad.view.maker.LabelMaker;

public class WinConditionSetupStage extends BuilderStage {

  private BorderPane myPane = new BorderPane();
  private Stage myStage = new Stage();
  private String[] possibleWinTypes;
  private String[] needsCellToHitSelection;
  private String[] needsWinConditionSelection;
  private VBox centerPane;
  private Map<String, TextArea> varInputBoxes;
  private ComboBox cellToHitOptionBox;
  private ComboBox winStateOptionBox;
  private List<Object> winConditions;

  public WinConditionSetupStage() {
    possibleWinTypes = getMyBuilderResources().getString("possibleWinConditionType").split(",");
    needsCellToHitSelection = getMyBuilderResources().getString("needsCellsToHitSelection")
        .split(",");
    needsWinConditionSelection = getMyBuilderResources().getString("needsWinConditionSelection")
        .split(",");
    myPane.setTop(makeConditionSelectionPrompt(possibleWinTypes));
    myPane.setRight(setUpObjectView());
    myPane.setBottom(makeContinueButton());

  }

  private VBox makeConditionSelectionPrompt(String[] options) {
    VBox result = new VBox();
    ComboBox comboBox = makeComboBox(options);
    centerPane = new VBox();
    result.getChildren()
        .addAll(comboBox, makeButton("Select", e -> handleConditionChoice(comboBox)), centerPane);

    return result;
  }

  private void handleConditionChoice(ComboBox comboBox) {
    try {
      resetSelection();

      String selection = comboBox.getValue().toString();
      Boolean needsCellToHitBool = Arrays.stream(needsCellToHitSelection)
          .anyMatch(selection::equals);
      if (needsCellToHitBool) {
        addCellToHitSelectionOption();
      }
      addVariableInputBoxes(selection);
      Boolean needsWinConditionBool = Arrays.stream(needsWinConditionSelection)
          .anyMatch(selection::equals);
      if (needsWinConditionBool) {
        addWinStateSelectionOption();
      }
      centerPane.getChildren()
          .add(makeButton("Add Condition",
              e -> saveWinCondition(selection, needsCellToHitBool, needsWinConditionBool)));

    } catch (NullPointerException e) {
      System.out.println("Please Make Selection");
      e.printStackTrace();
    }

  }

  private void saveWinCondition(String selection, Boolean needsCellToHit,
      Boolean needsWinCondition) {
    winConditions = new ArrayList<>();
    String entryName = selection;
    List<Object> parameterList = new ArrayList<>();
    if (needsCellToHit) {
      String cellType = (String) cellToHitOptionBox.getValue();
      entryName += cellType;
      parameterList.add(CellState.valueOf(cellType));
    }
    parameterList.addAll(getVariableInput());
    if (needsWinCondition) {
      parameterList.add(WinState.valueOf((String) winStateOptionBox.getValue()));
    }
    Object[] parameters = new Object[parameterList.size()];
    parameterList.toArray(parameters);
    Class<?>[] parameterTypes = getParameterTypes(parameters);

    try {
      winConditions.add(createInstance("oogasalad.model.utilities.winconditions." + selection,
          parameterTypes, parameters));
      resetSelection();
      addToObjectList(entryName);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private Class<?>[] getParameterTypes(Object[] parameters) {
    Class<?>[] parameterTypes = new Class<?>[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      if (parameters[i] instanceof Integer) {
        parameterTypes[i] = int.class;
      } else {
        parameterTypes[i] = parameters[i].getClass();
      }
    }
    return parameterTypes;
  }

  private List<Integer> getVariableInput() {
    List<Integer> variableList = new ArrayList<>();
    for (String variable : varInputBoxes.keySet()) {
      String input = varInputBoxes.get(variable).getText();
      if (checkIntConversion(input)) {
        variableList.add(Integer.parseInt(input));
      }
    }

    return variableList;
  }

  private void addCellToHitSelectionOption() {
    cellToHitOptionBox = makeComboBox(
        getMyBuilderResources().getString("possibleCellState").split(","));
    centerPane.getChildren()
        .add(cellToHitOptionBox);
  }

  private void addWinStateSelectionOption() {
    winStateOptionBox = makeComboBox(
        getMyBuilderResources().getString("possibleWinState").split(","));
    centerPane.getChildren()
        .add(winStateOptionBox);
  }

  private void addVariableInputBoxes(String selectedWinCondition) {
    String[] variables = getMyBuilderResources().getString(selectedWinCondition + "Variables")
        .split(",");
    varInputBoxes = new HashMap<>();
    for (String var : variables) {
      TextArea varInput = makeTextAreaWithDefaultValue("1");
      varInputBoxes.put(var, varInput);
      centerPane.getChildren().add(new HBox(LabelMaker.makeLabel(var, var + "_label"), varInput));
    }

  }

  private void resetSelection() {
    cellToHitOptionBox = null;
    winStateOptionBox = null;
    centerPane.getChildren().clear();
  }

  @Override
  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    return null;
  }

  @Override
  protected Object launch() {
    setUpStage(myPane);
    return winConditions;
  }

  @Override
  protected Object saveAndContinue() {
    closeWindow();
    return null;
  }
}

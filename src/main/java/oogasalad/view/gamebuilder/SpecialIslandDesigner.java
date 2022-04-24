package oogasalad.view.gamebuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.view.maker.LabelMaker;

public class SpecialIslandDesigner extends BuilderStage {

  private BorderPane myPane;
  private String[] stringInputFilter;
  private String[] modifierTypes;
  private VBox centerPane;
  private Map<String, TextArea> varInputBoxes;
  private TextArea idInputBox;
  private final ListView<String> modifierListView = new ListView<>();
  private ObservableList<String> modifierList = FXCollections.observableArrayList();
  private List<Object> islandList;
  private Cell islandCell;

  public SpecialIslandDesigner() {
    myPane = new BorderPane();
    myPane.setBottom(makeContinueButton());
    centerPane = new VBox();
    modifierListView.setItems(modifierList);
    modifierListView.setMaxSize(200, 300);
    modifierTypes = getMyBuilderResources().getString("possibleModifierTypes").split(",");
    stringInputFilter = getMyBuilderResources().getString("needsStringInputFilter").split(",");
    islandList = new ArrayList<>();
    myPane.setCenter(makeModifierChoice(modifierTypes));
    myPane.setRight(setUpObjectView());
  }

  private VBox makeModifierChoice(String[] modifiers) {
    VBox result = new VBox();
    ComboBox comboBox = makeComboBox(modifiers);

    idInputBox = makeTextAreaWithDefaultValue("default Island");
    result.getChildren().add(idInputBox);
    result.getChildren()
        .addAll(comboBox, makeButton("Select", e -> handleModifierChoice(comboBox)), centerPane);

    return result;
  }

  private void handleModifierChoice(ComboBox comboBox) {
    resetSelection();
    islandCell = new IslandCell(new Coordinate(0, 0), 1);
    String selection = comboBox.getValue().toString();
    String[] variables = getMyBuilderResources().getString(selection + "Variables").split(",");
    varInputBoxes = new HashMap<>();

    setUpVariableInputs(variables);
    centerPane.getChildren()
        .addAll(makeButton("Add Modifier", e -> addModifier(selection, islandCell)),
            modifierListView);
    centerPane.getChildren().add(makeButton("Save Island", e -> saveIsland(islandCell)));
  }

  private void saveIsland(Cell currentIslandCell) {
    addToObjectList(idInputBox.getText());
    islandList.add(currentIslandCell);
    myPane.setCenter(null);
    centerPane.getChildren().clear();
    modifierList.clear();
    myPane.setCenter(makeModifierChoice(modifierTypes));
  }

  private void addModifier(String selection, Cell currentIslandCell) {
    List<Object> parametersList = getVariableInput();
    Class<?>[] parameterType = getParameterList(parametersList);
    Object[] parameters = new Object[parametersList.size()];
    parametersList.toArray(parameters);
    try {
      currentIslandCell.addModifier(
          (Modifiers) createInstance("oogasalad.model.utilities.tiles.Modifiers." + selection,
              parameterType, parameters));
      modifierList.add(selection + parametersList);
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  private Class<?>[] getParameterList(List<Object> parameters) {
    Class<?>[] parametersList = new Class<?>[parameters.size()];
    int counter = 0;
    for (Object parameter : parameters) {
      if (parameter instanceof Integer) {
        parametersList[counter] = int.class;
      } else {
        parametersList[counter] = parameter.getClass();
      }
      counter++;
    }
    return parametersList;
  }

  private List<Object> getVariableInput() {
    List<Object> variableList = new ArrayList<>();
    for (String variable : varInputBoxes.keySet()) {
      String input = varInputBoxes.get(variable).getText();
      if (Arrays.stream(stringInputFilter).anyMatch(variable::equals)) {
        variableList.add(input);
      } else {
        if (checkIntConversion(input)) {
          variableList.add(Integer.parseInt(input));
        }
      }
    }

    return variableList;
  }


  private void setUpVariableInputs(String[] variables) {
    for (String var : variables) {
      TextArea varInput = makeTextAreaWithDefaultValue("1");
      varInputBoxes.put(var, varInput);
      centerPane.getChildren().add(new HBox(LabelMaker.makeLabel(var, var + "_label"), varInput));
    }
  }

  private void resetSelection() {
    centerPane.getChildren().clear();
  }

  @Override
  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    return null;
  }

  @Override
  protected Object launch() {
    setUpStage(myPane);
    return islandList;
  }

  @Override
  protected void saveAndContinue() {
    closeWindow();
  }
}

package oogasalad.view.gamebuilder;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import oogasalad.view.maker.LabelMaker;

/**
 * A subclass of BuilderStage which handles the input of some game variables, such as numShots
 * gold,etc. Depends on JavaFX, makes no assumptions.
 *
 * @author Luka Mdivani
 */
public class GameVarSetupStage extends BuilderStage {

  private BorderPane myPane = new BorderPane();
  private String[] gameSettingVariables;
  private Map<String, TextArea> varInputBoxes = new HashMap<>();
  private VBox centerPane = new VBox();
  private Map<String, Integer> variableInputMap;
  private static final String DEFAULT_VALUE = "1";
  private static final String TITLE = "SET GAME VARIABLES";

  public GameVarSetupStage() {
    gameSettingVariables = getMyBuilderResources().getString("gameVariables").split(",");
    variableInputMap = new HashMap<>();
    myPane.setCenter(centerPane);

  }

  protected void setUp() {
    setTitle(TITLE);
    setUpVariableInput(gameSettingVariables);
  }

  protected void setUpVariableInput(String[] variables) {
    makeVarEditor(variables);
    centerPane.getChildren().add(makeContinueButton());
  }

  private void makeVarEditor(String[] variables) {
    for (String var : variables) {
      TextArea varInput = makeTextAreaWithDefaultValue(DEFAULT_VALUE);
      varInputBoxes.put(var, varInput);
      centerPane.getChildren().add(new HBox(LabelMaker.makeLabel(var, var + "_label"), varInput));
    }
  }

  @Override
  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    return null;
  }

  @Override
  protected Object launch() {
    setUp();
    setUpStage(myPane);
    return variableInputMap;
  }

  private void saveVariableData() {
    for (String variable : varInputBoxes.keySet()) {
      String input = varInputBoxes.get(variable).getText();
      try {
        int inputInt = Integer.parseInt(input);
        variableInputMap.put(variable, inputInt);
      } catch (NumberFormatException e) {
        System.out.println(getDictionaryResources().getString("validIntError"));
      }
    }
  }

  @Override
  protected void saveAndContinue() {
    saveVariableData();
    closeWindow();
  }
}

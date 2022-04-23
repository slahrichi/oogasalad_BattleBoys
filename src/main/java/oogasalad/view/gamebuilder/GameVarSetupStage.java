package oogasalad.view.gamebuilder;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import oogasalad.view.maker.LabelMaker;

public class GameVarSetupStage extends BuilderStage{
  private BorderPane myPane=new BorderPane();
  private String[] gameSettingVariables;
  private Map<String,TextArea>  varInputBoxes=new HashMap<>();
  private VBox centerPane= new VBox();
  private Map<String,Integer> variableInputMap;

  public GameVarSetupStage(){
    gameSettingVariables=getMyBuilderResources().getString("gameVariables").split(",");
    variableInputMap=new HashMap<>();
    myPane.setCenter(centerPane);
    setUpVariableInput(gameSettingVariables);
  }
  private void setUpVariableInput(String[] variables){
    makeVarEditor(variables);
    centerPane.getChildren().add(makeContinueButton());
  }
  private void makeVarEditor(String[] variables) {
    for (String var : variables) {
      TextArea varInput = makeTextAreaWithDefaultValue("1");
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
    setUpStage(myPane);
    return variableInputMap;
  }

  private void saveVariableData(){
    for(String variable : varInputBoxes.keySet()){
      String input= varInputBoxes.get(variable).getText();
      try{
        int inputInt=Integer.parseInt(input);
        variableInputMap.put(variable,inputInt);
      }
      catch (NumberFormatException e){
        System.out.println("enter valid ints");
      }
    }
  }

  @Override
  protected Object saveAndContinue() {
    saveVariableData();
    closeWindow();
    return null;
  }
}

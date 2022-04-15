package oogasalad.view.gameBuilder;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ColorSelectionStage extends BuilderStage{
  private BorderPane myPane = new BorderPane();
  private String[] optionList;
  private List<Color> colorList=new ArrayList<>();
  private List<ColorPicker> colorPickers=new ArrayList<>();
  private Stage myStage;

  public ColorSelectionStage(){
    super();
    myStage =  new Stage();
    optionList= getMyBuilderResources().getString("possibleCellState").split(",");
    myPane.setCenter(buildOptionDisplay());
    Button continueButton = new Button("Continue");
    continueButton.setOnAction(e->saveAndContinue());
    myPane.setRight(continueButton);

    Scene myScene =  new Scene(myPane, 900, 500);
    myStage.setScene(myScene);
    myStage.show();

  }

  @Override
  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    return null;
  }

  private VBox buildOptionDisplay(){
    VBox result = new VBox();
    int index=0;
    for(String option : optionList){
      ColorPicker test = new ColorPicker();
      colorPickers.add(test);
      result.getChildren().add(new HBox(new Text(option),test));

    }


    return result;
  }

  protected void saveAndContinue(){
    for(ColorPicker cp : colorPickers){
      colorList.add(cp.getValue());
    }
    myStage.close();
    BoardSetUpStage bb = new BoardSetUpStage();
    myStage.close();
  }

}

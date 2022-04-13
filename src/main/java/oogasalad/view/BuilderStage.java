package oogasalad.view;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.shape.Rectangle;

public abstract class BuilderStage {

  private ResourceBundle myBuilderResources ;
  public BuilderStage() {
    myBuilderResources = ResourceBundle.getBundle("/BuilderInfo");
  }
  protected ResourceBundle getMyBuilderResources(){return myBuilderResources;}
  protected int[][] initializeBlankMap(int height, int width) {
    int[][] stateMap = new int[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        stateMap[i][j] = 0;
      }
    }
    return stateMap;
  }

  protected Button makeButton(String name, EventHandler<ActionEvent> handler){
    Button result =  new Button(name);
    result.setOnAction(handler);

    return result;
  }


  protected Group arrangeCells(int height, int width, double cellSize, int[][] stateMap) {
    Group cellGroup = new Group();
    double xPos;
    double yPos = 0;
    for (int i = 0; i < height; i++) {
      xPos = 0;
      for (int j = 0; j < width; j++) {
        cellGroup.getChildren().add(createCell(xPos, yPos, i, j, stateMap[i][j]));

        xPos = xPos + cellSize;
      }
      yPos = yPos + cellSize;
    }
    return cellGroup;
  }

  protected abstract Rectangle createCell(double xPos, double yPos, int i, int j, int state);

  protected abstract void saveAndContinue();
}

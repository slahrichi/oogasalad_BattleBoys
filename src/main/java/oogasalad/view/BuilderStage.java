package oogasalad.view;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

public abstract class BuilderStage {

  public BuilderStage() {
  }

  protected void initializeBlankMap(int height, int width, int[][] stateMap) {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        stateMap[i][j] = 1;
      }
    }
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

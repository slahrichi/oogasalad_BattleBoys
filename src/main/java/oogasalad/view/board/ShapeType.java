package oogasalad.view.board;

import java.util.ArrayList;
import java.util.List;

public abstract class ShapeType {

  protected double myBoardWidth;
  protected double myBoardHeight;
  protected static final double X_SPACING = 3;
  protected static final double Y_SPACING = 3;

  public ShapeType(double width, double height){

    myBoardWidth = width;
    myBoardHeight = height;

  }


  public abstract List<Double> calculatePoints(double row, double col, double width, double height);

  protected abstract double calculateXPos(double width, double col);

  protected abstract double calculateYPos(double height, double row);

}

package oogasalad.view.board;


import java.util.ArrayList;
import java.util.List;

public class ShipShapeType extends ShapeType {

  private static final double sizeMultiplier = 20;


  public ShipShapeType(double width, double height) {
    super(width, height);
  }

  @Override
  public List<Double> calculatePoints(double row, double col, double width, double height) {


    List<Double> points = new ArrayList<>();

    int[] xFactor = new int[]{0, 1, 1, 0};
    int[] yFactor = new int[]{0, 0, 1, 1};

    for (int i = 0; i < 4; i++) {
      points.add(calculateXPos(width, col) + sizeMultiplier * xFactor[i]);
      points.add(calculateYPos(height, row) + sizeMultiplier * yFactor[i]);
    }

    return points;  }

  @Override
  protected double calculateXPos(double width, double col) {
    return ((col + 1) * X_SPACING) + (col * sizeMultiplier);
  }

  @Override
  protected double calculateYPos(double height, double row) {
    return ((row + 1) * Y_SPACING) + (row * sizeMultiplier);
  }
}

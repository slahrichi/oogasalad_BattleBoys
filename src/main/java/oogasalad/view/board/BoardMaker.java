package oogasalad.view.board;

import java.util.ArrayList;
import java.util.List;

public class BoardMaker {

  protected double myCellSize;
  protected static final double SPACING = 3;

  public BoardMaker(double size) {
    myCellSize = size;
  }

  public List<Double> calculatePoints(double row, double col) {
    List<Double> points = new ArrayList<>();

    int[] xFactor = new int[]{0, 1, 1, 0};
    int[] yFactor = new int[]{0, 0, 1, 1};

    for (int i = 0; i < 4; i++) {
      points.add(calculatePos(col) + myCellSize * xFactor[i]);
      points.add(calculatePos(row) + myCellSize* yFactor[i]);
    }
    return points;
  }

  private double calculatePos(double index) {
    return (index * SPACING) + (index * myCellSize);
  }
}

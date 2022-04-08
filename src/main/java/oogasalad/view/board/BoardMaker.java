package oogasalad.view.board;

import java.util.ArrayList;
import java.util.List;

public class BoardMaker {

  protected double myBoardSize;
  private double myMaxLength;
  protected static final double SPACING = 3;

  public BoardMaker(double size, double numRows, double numCols) {
    myBoardSize = size;
    myMaxLength = Math.max(numRows, numCols);
  }

  public List<Double> calculatePoints(double row, double col) {
    List<Double> points = new ArrayList<>();

    int[] xFactor = new int[]{0, 1, 1, 0};
    int[] yFactor = new int[]{0, 0, 1, 1};

    for (int i = 0; i < 4; i++) {
      points.add(calculatePos(col) + calculateSize() * xFactor[i]);
      points.add(calculatePos(row) + calculateSize() * yFactor[i]);
    }
    return points;
  }

  private double calculateSize() {
    return (myBoardSize - (myMaxLength - 1) * SPACING) / myMaxLength;
  }

  private double calculatePos(double index) {
    return (index * SPACING) + (index * calculateSize());
  }
}

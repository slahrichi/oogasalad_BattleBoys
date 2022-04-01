package oogasalad.view.board;

import static oogasalad.view.board.BoardView.BOARD_HEIGHT;
import static oogasalad.view.board.BoardView.BOARD_WIDTH;

import java.util.ArrayList;
import java.util.List;

public class ShapeType {

  private static final double X_SPACING = 3;
  private static final double Y_SPACING = 3;
  public ShapeType() {

  }
  public List<Double> getPoints(double col, double row, double width, double height) {
    // FIXME: refactor to not use default square points

    List<Double> points = new ArrayList<>();

    // arrays used as multiplicative factors
    int[] xFactor = new int[]{0, 1, 1, 0};
    int[] yFactor = new int[]{0, 0, 1, 1};

    for (int i = 0; i < 4; i++) {
      points.add(calculateXPos(width, col) + calculateWidth(width) * xFactor[i]);
      points.add(calculateYPos(height, row) + calculateHeight(height) * yFactor[i]);
    }

    return points;
  }

  private double calculateHeight(double height) {
    return (BOARD_HEIGHT - (height + 1) * Y_SPACING) / height;
  }

  private double calculateWidth(double width) {
    return (BOARD_WIDTH - (width + 1) * X_SPACING) / width;
  }

  private double calculateXPos(double width, double col) {
    return ((col + 1) * X_SPACING) + (col * calculateWidth(width));
  }

  private double calculateYPos(double height, double row) {
    return ((row + 1) * Y_SPACING) + (row * calculateHeight(height));
  }
}

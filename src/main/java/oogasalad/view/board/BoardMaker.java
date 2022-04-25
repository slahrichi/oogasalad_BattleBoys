package oogasalad.view.board;

import java.util.ArrayList;
import java.util.List;

/**
 * The helper class of the BoardView that is in charge of making the board by calculating the points
 * of one cell given its row and col in the board array
 *
 * @author Minjun Kwak
 */
public class BoardMaker {

  protected static final double SPACING = 3;

  /**
   * Calculates the position of the vertices of a particular cell to make its shape
   *
   * @param row the row number of this particular cell in the board array
   * @param col the column number of this particular cell in the board array
   * @param size
   * @return the list of vertices that make up this particular cell
   */
  public static List<Double> calculatePoints(double row, double col, double size) {
    List<Double> points = new ArrayList<>();

    int[] xFactor = new int[]{0, 1, 1, 0};
    int[] yFactor = new int[]{0, 0, 1, 1};

    for (int i = 0; i < 4; i++) {
      points.add(calculatePos(col, size) + size * xFactor[i]);
      points.add(calculatePos(row, size) + size * yFactor[i]);
    }
    return points;
  }

  // calculation for determining the top left position of this cell
  private static double calculatePos(double index, double size) {
    return (index * SPACING) + (index * size);
  }
}

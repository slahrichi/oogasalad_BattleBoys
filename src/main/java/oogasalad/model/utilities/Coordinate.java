package oogasalad.model.utilities;

import java.util.Objects;

/**
 * Coordinates Class that keeps track of a cell's location. Also used for relative coordaintes as well
 *
 * @author Brandon Bae
 */
public class Coordinate {

    private int row;
    private int column;

    /**
     * Constructor for Coordinate Class
     * @param row row index of coordinate
     * @param column column index of coordinate
     */
    public Coordinate(int row, int column) {
      this.row = row;
      this.column = column;
    }

    /**
     * Getter method for the row index of a given Coordinate
     *
     * @return the row index of a Coordinate
     */
    public int getRow() {
      return row;
    }

    /**
     * Getter method for the column index of a given Coordinate
     *
     * @return the column index of a Coordinate
     */
    public int getColumn() {
      return column;
    }

    /**
     * method for determining whether two coordinates are equal in position
     *
     * @param o an object to compared with the current coordinate
     * @return boolean whether the two coordinates have the same position
     */
    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Coordinate that = (Coordinate) o;
      return row == that.row && column == that.column;
    }

    public static Coordinate sum(Coordinate c1, Coordinate c2){
      return new Coordinate(c1.row + c2.row, c1.column+ c2.column);
    }

    /**
     * overridden hashcode method developed in order to allow effective use of Coordinates in HashMap
     * objects
     *
     * @return the hashcode of the coordinate
     */
    @Override
    public int hashCode() {
      return Objects.hash(row, column);
    }

    /**
     * method developed for debugging purposes in order to view a given coordiante as a String
     *
     * @return a string representation of a coordinate
     */
    @Override
    public String toString() {
      return "Coordinate{" +
          "row=" + row +
          ", column=" + column +
          '}';
    }
  }

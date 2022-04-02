package oogasalad.model.utilities;

public interface Cell {

  public Coordinate getPosition();

  /**
   * Method that returns string representation of what type of cell the current cell
   * @return String representation of what type of cell it is
   */
  public String getType();
}

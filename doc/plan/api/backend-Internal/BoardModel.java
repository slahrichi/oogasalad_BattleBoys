/**
 * Model class that will be representative of a grid of tiles. This is used in multiple parts of the game
 * such as a grid representation of your board and a grid representation of your predictions.
 * Boards will be represented by a Map with Key being a Coordinate while the value being the Tile
 * at that coordinate.

public class BoardModel {

  /**
   * Returns the state of the specified cell
   * @param x x coordinate of specified cell
   * @param y y coordinate of specified cell
   * @return integer state of cell

  public int getCellState(int x, int y);

  /**
   * Method that updates the board using the functionality defined by the lambda passed through the parameter.
   * @param shotEffect lambda function that defines how board should be updated (representative of shot)

  public void updateBoard(Consumer<Map<Coordinate, Tile>> shotEffect);
}
*/
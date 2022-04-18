package oogasalad.view.board;


import oogasalad.model.utilities.tiles.enums.CellState;

/**
 * This class represents BoardViews that are meant to be displayed during the main game phase.
 *
 * @author Minjun Kwak, Eric Xie
 */
public abstract class GameBoardView extends BoardView {

  public GameBoardView(double size, CellState[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }


  public abstract void initializeCellViews(CellState[][] arrayLayout);
}

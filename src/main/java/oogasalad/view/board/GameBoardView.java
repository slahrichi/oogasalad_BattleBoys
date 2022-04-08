package oogasalad.view.board;


import oogasalad.model.utilities.tiles.enums.CellState;

public abstract class GameBoardView extends BoardView {

  public GameBoardView(double size, CellState[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  public abstract void initializeCellViews(CellState[][] arrayLayout);
}

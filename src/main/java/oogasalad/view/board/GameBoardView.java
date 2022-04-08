package oogasalad.view.board;


public abstract class GameBoardView extends BoardView {

  public GameBoardView(double size, int[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  public abstract void initializeCellViews(int[][] arrayLayout);
}

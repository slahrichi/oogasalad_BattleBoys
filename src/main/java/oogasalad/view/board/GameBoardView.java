package oogasalad.view.board;


public abstract class GameBoardView extends BoardView {

  public GameBoardView(ShapeType shape, int[][] arrayLayout, int id) {
    super(shape, arrayLayout, id);
  }

  public abstract void initializeCellViews(int[][] arrayLayout, ShapeType shape);
}

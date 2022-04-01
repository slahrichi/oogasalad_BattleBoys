package oogasalad.view.board;

import oogasalad.view.CellView;

public class EnemyBoardView extends GameBoardView {

  public EnemyBoardView(ShapeType shape, int[][] arrayLayout, int id) {
    super(shape, arrayLayout, id);
  }

  public void initializeCellViews(int[][] arrayLayout, ShapeType shape) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        if (arrayLayout[row][col] == EMPTY) {
          CellView cell = new CellView(shape, mapCellToColor.get(EMPTY), col, row, arrayLayout[0].length,
              arrayLayout.length);
          cell.addObserver(this);
          myLayout[row][col] = cell;
        }

      }
    }
  }
}

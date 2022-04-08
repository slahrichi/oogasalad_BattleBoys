package oogasalad.view.board;

import oogasalad.view.CellView;

public class EnemyBoardView extends GameBoardView {

  public EnemyBoardView(double size, int[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  public void initializeCellViews(int[][] arrayLayout) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        if (arrayLayout[row][col] == EMPTY) {
          CellView cell = new CellView(myBoardMaker, mapCellToColor.get(EMPTY), row, col, arrayLayout.length,
              arrayLayout[0].length);
          cell.addObserver(this);
          myLayout[row][col] = cell;
        }

      }
    }
  }
}

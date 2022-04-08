package oogasalad.view.board;

import oogasalad.view.CellView;

public class SelfBoardView extends GameBoardView {

  public SelfBoardView(double size, int[][] arrayLayout, int id) {
    super(size, arrayLayout, id);
  }

  public void initializeCellViews(int[][] arrayLayout) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        CellView cell = new CellView(myBoardMaker, arrayLayout[row][col] == HEALTHY_SHIP ? mapCellToColor.get(HEALTHY_SHIP) : arrayLayout[row][col] == SPECIAL ? mapCellToColor.get(SPECIAL) : mapCellToColor.get(EMPTY), row, col, arrayLayout.length,
              arrayLayout[0].length);
        myLayout[row][col] = cell;
      }
    }
  }
}

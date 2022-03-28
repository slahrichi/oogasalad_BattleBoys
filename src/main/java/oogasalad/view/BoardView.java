package oogasalad.view;

import java.awt.LayoutManager;
import java.awt.event.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import javax.swing.plaf.basic.BasicOptionPaneUI.ButtonAreaLayout;

// board
public class BoardView {

  private CellView[][] boardLayout;
  private Pane myBoard;

  // controller passes some kind of parameter to the
  public BoardView(int[][] arrayLayout) {
    setupBoard(arrayLayout);

  }

  private void setupBoard(int[][] arrayLayout) {
    myBoard = new FlowPane();
    for (int row = 0; row < arrayLayout.length; row++) {
      for(int col = 0; col < arrayLayout[0].length; col++) {
        if (arrayLayout[row][col] == 1) {
          boardLayout[row][col] = new CellView(true, row, col);
        }
        else{
          boardLayout[row][col] = new CellView(false, row, col);
        }
      }
    }
  }

  public void addListener(ActionListener listener) {
    for (CellView[] cellRow : boardLayout) {
      for (CellView cell : cellRow) {
        cell.putClientProperty("coords", cell.getCoords());
        cell.addActionListener(listener);
      }
    }
  }

  public void show() {
    for (int i = 0; i < boardLayout.length; i++) {
      for (int j = 0; j < boardLayout[0].length; j++) {
        myBoard.getChildren().add(boardLayout[i][j]);
      }
    }
  }
}

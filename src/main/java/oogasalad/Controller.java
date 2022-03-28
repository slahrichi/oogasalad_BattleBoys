package oogasalad;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import oogasalad.view.BoardView;

public class Controller {
  private BoardView myBoard;

  public Controller() {
    int[][] arr = new int[][]{{0, 0, 0, 1, 1}, {0, 0, 1, 1, 0}, {0, 1, 1, 0, 0}, {1, 1, 0, 0, 0}, {1, 1, 1, 1, 1}};
    myBoard = new BoardView(arr);
    myBoard.addListener(new BoardListener());
    myBoard.show();
  }

  class BoardListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      System.out.println(((JButton)e.getSource()).getClientProperty("coords"));
    }
  }
}

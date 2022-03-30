package oogasalad;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import oogasalad.view.BoardView;

public class Controller extends PropertyObservable implements PropertyChangeListener {

  private BoardView myBoard;

  public Controller(BoardView board) {
    myBoard = board;
    myBoard.addObserver(this);
  }


  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    System.out.println("inside controller " + evt);
  }
}

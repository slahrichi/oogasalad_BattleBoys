package oogasalad;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import oogasalad.view.BoardView;
import oogasalad.view.View;

public class Controller extends PropertyObservable implements PropertyChangeListener {

  private View myView;

  public Controller(View view) {
    myView = view;
    myView.addObserver(this);
  }


  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    System.out.println("inside controller " + evt);
  }
}

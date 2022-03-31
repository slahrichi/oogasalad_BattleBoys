package oogasalad;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PropertyObservable {
  private PropertyChangeSupport pcs;

  public PropertyObservable() {
    pcs = new PropertyChangeSupport(this);
  }

  public void addObserver(PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }

  public void notifyObserver(String name, Object data) {
    pcs.firePropertyChange(name, null, data);
  }
}

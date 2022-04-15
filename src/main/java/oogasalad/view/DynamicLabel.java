package oogasalad.view;

import javafx.scene.control.Label;

/**
 * This class represents a Label object whose text changes frequently and in the same place each time. For example, a
 * label that displays whose turn it is will say
 */
public class DynamicLabel extends Label {
  private String myText;

  public DynamicLabel(String mainText, String dynamicText) {
    if(!mainText.contains("%s")) {
      throw new IllegalArgumentException("No format specifier found for dynamic label text: " + mainText);
    }
    myText = mainText;
    setText(String.format(mainText, dynamicText));
  }

  public void changeDynamicText(String newText) {
    setText(String.format(myText, newText));
  }
}

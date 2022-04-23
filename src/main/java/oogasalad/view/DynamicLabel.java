package oogasalad.view;

import javafx.scene.control.Label;

/**
 * This class represents a Label object whose text changes frequently and in the same place each time. For example, a
 * label that displays whose turn it is will say "Player 1's Turn", but the numeric value will change consistently. This
 * class provides a method, changeDynamicText(), to make it easy to change this customizable value while keeping the rest
 * of the text the same.
 *
 * @author Edison Ooi
 */
public class DynamicLabel extends Label {
  private String myText;

  /**
   * Class constructor. Throws an error if mainText does not contain a valid format specifier.
   *
   * @param mainText Entire text with format specifier
   * @param dynamicText Initial text
   */
  public DynamicLabel(String mainText, String dynamicText) throws IllegalArgumentException {
    if(!mainText.contains("%s")) {
      throw new IllegalArgumentException("No format specifier found for dynamic label text: " + mainText);
    }
    myText = mainText;
    setText(String.format(mainText, dynamicText));
  }

  /**
   * Replaces the modifiable part of this label's text with newText.
   *
   * @param newText new text
   */
  public void changeDynamicText(String newText) {
    setText(String.format(myText, newText));
  }


}

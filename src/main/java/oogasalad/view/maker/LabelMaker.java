package oogasalad.view.maker;

import javafx.scene.control.Label;
import oogasalad.view.DynamicLabel;

/**
 * This class contains static methods to create new Label objects from various arguments. These methods help reduce the
 * amount of repetitive customization code needed in View classes.
 *
 * @author Edison Ooi
 */
public class LabelMaker {

  /**
   * Creates a new DynamicLabel object
   *
   * @param mainText The entire text of the label, along with a format specifier representing the changeable part
   * @param dynamicText The initial argument for the format specifier in mainText
   * @param id The id of this label
   * @return new DynamicLabel
   */
  public static DynamicLabel makeDynamicLabel(String mainText, String dynamicText, String id) {
    DynamicLabel dynamicLabel = new DynamicLabel(mainText, dynamicText);
    dynamicLabel.setId(id);
    return dynamicLabel;
  }

  /**
   * Creates a new generic Label object
   *
   * @param text The text of this label
   * @param id The id of this label
   * @return new Label
   */
  public static Label makeLabel(String text, String id) {
    Label label = new Label(text);
    label.setId(id);
    return label;
  }
}

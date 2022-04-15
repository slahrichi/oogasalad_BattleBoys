package oogasalad.view.maker;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class contains static methods to create new HBox and VBox objects from various arguments.
 * These methods help reduce the amount of repetitive customization code needed in View classes.
 *
 * @author Edison Ooi
 */
public class BoxMaker {

  /**
   * Creates a new VBox.
   *
   * @param id Id of this VBox
   * @param spacing Spacing between child nodes
   * @param alignment Alignment Pos for the children of this VBox
   * @param nodes JavaFX Nodes to be placed in this VBox
   * @return new VBox
   */
  public static VBox makeVBox(String id, double spacing, Pos alignment, Node... nodes) {
    VBox box = new VBox();
    box.setId(id);
    box.setSpacing(spacing);
    box.setAlignment(alignment);
    box.getChildren().addAll(nodes);
    return box;
  }

  /**
   * Creates a new VBox without specified initial Nodes.
   *
   * @param id Id of this VBox
   * @param spacing Spacing between child nodes
   * @param alignment Alignment Pos for the children of this VBox
   * @return new VBox
   */
  public static VBox makeVBox(String id, double spacing, Pos alignment) {
    VBox box = new VBox();
    box.setId(id);
    box.setSpacing(spacing);
    box.setAlignment(alignment);
    return box;
  }

  /**
   * Creates a new HBox.
   *
   * @param id Id of this HBox
   * @param spacing Spacing between child nodes
   * @param alignment Alignment Pos for the children of this HBox
   * @param nodes JavaFX Nodes to be placed in this HBox
   * @return new HBox
   */
  public static HBox makeHBox(String id, double spacing, Pos alignment, Node... nodes) {
    HBox box = new HBox();
    box.setId(id);
    box.setSpacing(spacing);
    box.setAlignment(alignment);
    box.getChildren().addAll(nodes);
    return box;
  }

  /**
   * Creates a new HBox without specified initial Nodes.
   *
   * @param id Id of this HBox
   * @param spacing Spacing between child nodes
   * @param alignment Alignment Pos for the children of this HBox
   * @return new HBox
   */
  public static HBox makeHBox(String id, double spacing, Pos alignment) {
    HBox box = new HBox();
    box.setId(id);
    box.setSpacing(spacing);
    box.setAlignment(alignment);
    return box;
  }
}

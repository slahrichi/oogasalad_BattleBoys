package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ResourceBundle;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import oogasalad.PropertyObservable;
import oogasalad.view.maker.BoxMaker;
import oogasalad.view.maker.LabelMaker;

/**
 * This class represents a scrollable view of all usable items in a player's inventory. Upon
 * clicking on an inventory item, this class will notify an observer with information on the ID of
 * the usable that was clicked so that the player can equip it.
 *
 * @author Edison Ooi, Minjun Kwak
 */
public class InventoryView extends PropertyObservable implements PropertyChangeListener {

  private static final String INVENTORY_BOX_ID = "inventory-box";
  private static final String INVENTORY_VIEW_ID = "inventory-view";
  private static final String EQUIP_USABLE_METHOD = "equipUsable";
  private static final String BASIC_SHOT_ID = "Basic Shot";
  private static final double INVENTORY_SPACING = 10;
  // JavaFX components
  private HBox elementsBox;
  private ScrollPane myPane;

  /**
   * Class constructor.
   */
  public InventoryView() {
    myPane = new ScrollPane();
    myPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    elementsBox = BoxMaker.makeHBox(INVENTORY_BOX_ID, INVENTORY_SPACING, Pos.CENTER);
    myPane.setContent(elementsBox);
    myPane.setId(INVENTORY_VIEW_ID);
  }

  /**
   * Replaces all InventoryElements in this view with new ones generated from the given Map of
   * Usables.
   *
   * @param usables new set of Usables with which to populate this InventoryView
   */
  public void updateElements(List<UsableRecord> usables) {
    clearElements();
    for (UsableRecord usable : usables) {
      addElementToHBox(usable.quantity(), usable.className(), usable.id());
    }
  }

  // Adds a new InventoryElement to this view's HBox
  private void addElementToHBox(double quantity, String className, String id) {
    InventoryElement element = new InventoryElement(quantity, className, id);
    element.addObserver(this);
    elementsBox.getChildren().add(element.getBox());
    if (id.equals(BASIC_SHOT_ID)) {
      element.getBox().setBackground(
          new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }
  }

  // Clears all InventoryElements in this view's HBox
  private void clearElements() {
    elementsBox.getChildren().clear();
  }

  /**
   * Detects an event, such as a mouse click, on an InventoryElement and propagates information
   * about that element to listeners.
   *
   * @param evt event that was triggered
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    for (Node child : elementsBox.getChildren()) {
      ((VBox) child).setBackground(
          new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    notifyObserver(evt.getPropertyName(), evt.getNewValue());
  }

  /**
   * @return ScrollPane that represents this InventoryView
   */
  public ScrollPane getPane() {
    return myPane;
  }


  // This class represents an individual usable item's visual representation in an InventoryView
  private class InventoryElement extends PropertyObservable {

    private static final ResourceBundle WEAPON_IMAGE_RESOURCES = ResourceBundle.getBundle(
        "/WeaponImages");
    private static final String WEAPON_IMAGES_PATH = "images/weapon_images/";
    private static final String INVENTORY_USABLE_ID = "inventory-usable";
    private static final String QUANTITY_LABEL = "x";
    private static final String QUANTITY_LABEL_ID = "inventory-element-quantity-label";
    private static final String ELEMENT_NAME_LABEL = "inventory-element-name-label";
    private static final double ITEM_SIZE = 50;
    private static final String BASIC_SHOT_ID = "Basic Shot";
    private static final double ELEMENT_SPACING = 5;

    // Information about this usable
    private double quantity;
    private String usableID;

    // JavaFX components
    private VBox elementBox;

    /**
     * Class constructor. Initializes this element's usable's attributes and visual components.
     *
     * @param quantity  Amount of this usable the player owns
     * @param className Path to the file representing this usable's image representation
     * @param usableID  ID of usable
     */
    private InventoryElement(double quantity, String className, String usableID) {
      elementBox = BoxMaker.makeVBox(INVENTORY_USABLE_ID, ELEMENT_SPACING, Pos.CENTER);
      elementBox.setOnMouseClicked(e -> handleClicked());
      this.quantity = quantity;
      this.usableID = usableID;
      setupName(usableID);
      setupImage(className);
      setupQuantityLabel();
    }

    private void setupName(String name) {
      Label nameLabel = LabelMaker.makeLabel(name, ELEMENT_NAME_LABEL);
      elementBox.getChildren().add(nameLabel);
    }

    // Sets up quantity label
    private void setupQuantityLabel() {
      if (!usableID.equals(BASIC_SHOT_ID)) {
        Label quantityLabel = LabelMaker.makeLabel("x" + (int) quantity, "inventory-element-quantity-label");
        elementBox.getChildren().add(quantityLabel);
      }
    }

    // Sets up background image
    private void setupImage(String className) {
      ImageView image = new ImageView(
          new Image(WEAPON_IMAGES_PATH + WEAPON_IMAGE_RESOURCES.getString(className)));
      image.setFitWidth(ITEM_SIZE);
      image.setFitHeight(ITEM_SIZE);
      elementBox.getChildren().add(image);
    }

    // Notifies observer when this element is clicked
    private void handleClicked() {
      notifyObserver(EQUIP_USABLE_METHOD, usableID);
      elementBox.setBackground(
          new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    // Returns StackPane representing this InventoryElement
    private VBox getBox() {
      return elementBox;
    }
  }

}

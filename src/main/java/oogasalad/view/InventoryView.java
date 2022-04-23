package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import oogasalad.PropertyObservable;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.view.maker.BoxMaker;
import oogasalad.view.maker.LabelMaker;

/**
 * This class represents a scrollable view of all usable items in a player's inventory. Upon clicking on an inventory
 * item, this class will notify an observer with information on the ID of the usable that was clicked so that the
 * player can equip it.
 *
 * @author Edison Ooi
 */
public class InventoryView extends PropertyObservable implements PropertyChangeListener {

  // JavaFX components
  private HBox elementsBox;
  private ScrollPane myPane;

  /**
   * Class constructor.
   */
  public InventoryView() {
    myPane = new ScrollPane();
    myPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    elementsBox = BoxMaker.makeHBox("inventory-box", 3, Pos.CENTER);
    myPane.setContent(elementsBox);
    myPane.setId("inventory-view");
  }

  /**
   * Replaces all InventoryElements in this view with new ones generated from the given Map of Usables.
   * @param usables new set of Usables with which to populate this InventoryView
   */
  public void updateElements(Map<String, Integer> usables) {
    clearElements();

    for(String usable : usables.keySet()) {
      addElementToHBox(usables.get(usable), "", usable); //TODO: Get image path and integer ID from usable
    }
  }

  // Adds a new InventoryElement to this view's HBox
  private void addElementToHBox(int quantity, String imagePath, String id) {
    InventoryElement element = new InventoryElement(quantity, imagePath, id);
    element.addObserver(this);
    elementsBox.getChildren().add(element.getPane());
  }

  // Clears all InventoryElements in this view's HBox
  private void clearElements() {
    elementsBox.getChildren().clear();
  }

  /**
   * Detects an event, such as a mouse click, on an InventoryElement and propogates information about that element
   * to listeners.
   * @param evt event that was triggered
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
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

    // Information about this usable
    private int quantity;
    private String usableID;

    // JavaFX components
    private Label quantityLabel;
    private StackPane elementPane;

    /**
     * Class constructor. Initializes this element's usable's attributes and visual components.
     * @param quantity Amount of this usable the player owns
     * @param imagePath Path to the file representing this usable's image representation
     * @param usableID ID of usable
     */
    private InventoryElement(int quantity, String imagePath, String usableID) {
      elementPane = new StackPane();
      elementPane.setId("inventory-usable");
      elementPane.setOnMouseClicked(e -> handleClicked());
      this.quantity = quantity;
      this.usableID = usableID;
      setupQuantityLabel();
      setupBackgroundImage(imagePath);
    }

    // Sets up quantity label
    private void setupQuantityLabel() {
      quantityLabel = LabelMaker.makeLabel("x" + quantity, "inventory-element-quantity-label");
      elementPane.getChildren().add(quantityLabel);
      StackPane.setAlignment(quantityLabel, Pos.TOP_RIGHT);
    }

    // Sets up background image
    private void setupBackgroundImage(String imagePath) {

    }
    
    // Notifies observer when this element is clicked
    private void handleClicked() {
      notifyObserver("equipUsable", usableID);
    }
    
    // Returns StackPane representing this InventoryElement
    private StackPane getPane() {
      return elementPane;
    }
  }

}

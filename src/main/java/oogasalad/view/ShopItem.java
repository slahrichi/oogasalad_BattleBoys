package oogasalad.view;


import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import oogasalad.view.maker.ButtonMaker;

public class ShopItem extends VBox {

  private static final double WIDTH = 90;
  private static final double HEIGHT = 50;
  private static final double GAP = 20;

  private Rectangle item; //FIXME replace with ImageView later
  private double price;

  public ShopItem(String itemName, String itemInfo, int index) {
    item = new Rectangle(WIDTH,HEIGHT);
    this.setLayoutX((GAP + WIDTH) * (index % 3));
    this.setLayoutY((GAP + HEIGHT) * (index / 3));
    this.getChildren().add(item);
    this.getChildren()
        .add(ButtonMaker.makeTextButton("Buy" + itemName, e -> applyItemProperties(), "Buy"));
  }

  private void applyItemProperties() {

  }

}

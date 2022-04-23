package oogasalad.view;


import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import oogasalad.PropertyObservable;
import oogasalad.view.maker.BoxMaker;
import oogasalad.view.maker.ButtonMaker;

public class ShopItem extends PropertyObservable {

  private static final double WIDTH = 90;
  private static final double HEIGHT = 50;
  private static final double GAP = 20;

  private Rectangle item; //FIXME replace with ImageView later
  private int myPrice;
  private VBox myVBox;
  private String myName;

  public ShopItem(String itemName, int Price, String usableClassName) {
    myVBox = BoxMaker.makeVBox("VBoxId", 10, Pos.CENTER);
    item = new Rectangle(WIDTH,HEIGHT);
    myPrice = Price;
    myName = itemName;
    /*
    this.setLayoutX((GAP + WIDTH) * (index % 3));
    this.setLayoutY((GAP + HEIGHT) * (index / 3));

     */
    myVBox.getChildren().add(item);
    Button buyButton = ButtonMaker.makeTextButton("Buy" + itemName, e -> applyItemProperties(), "Buy");
    myVBox.getChildren()
        .add(buyButton);
    buyButton.setOnAction(e -> buyItem());
  }

  private void buyItem() {
    notifyObserver("buyItem", myName);
  }

  public VBox getMyVBox() {
    return myVBox;
  }

  private void applyItemProperties() {

  }

}

package oogasalad.view;


import com.stripe.exception.StripeException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import oogasalad.PropertyObservable;
import oogasalad.com.stripe.StripeIntegration;
import oogasalad.view.maker.BoxMaker;
import oogasalad.view.maker.ButtonMaker;
import oogasalad.view.maker.LabelMaker;

public class ShopItem extends PropertyObservable {
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final ResourceBundle WEAPON_IMAGE_RESOURCES = ResourceBundle.getBundle(
          "/WeaponImages");
  private static final String WEAPON_IMAGES_PATH = "images/weapon_images/";

  private static final String PRICE_LABEL_FORMAT = "Price: %d Gold";

  private static final double ITEM_SIZE = 30;
  private static final double X_SPACING = 90;
  private static final double Y_SPACING = 120;
  private static final double GAP = 20;

  private ImageView item; //FIXME replace with ImageView later
  private int myPrice;
  private VBox myVBox;
  private String myName;
  private StripeIntegration stripeIntegration;
  private Button buyButton;
  private Button confirmStripe;
  private Button stripeButton;

  public ShopItem(String itemName, int price, String usableClassName, int index,
      StripeIntegration stripeIntegration) {
    this.stripeIntegration = stripeIntegration;
    //item = new Rectangle(WIDTH,HEIGHT);
    //ResourceBundle weaponImageBundle = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE+WEAPON_IMAGES_RESOURCE_BUNDLE);
    String imageName = WEAPON_IMAGE_RESOURCES.getString(usableClassName);
    item = new ImageView(new Image(WEAPON_IMAGES_PATH+imageName));
    item.setFitHeight(ITEM_SIZE);
    item.setFitWidth(ITEM_SIZE);
    myPrice = price;
    myName = itemName;
    buildButtons();
    Label nameLabel = LabelMaker.makeLabel(myName, "shop-item-name-label");
    Label priceLabel = LabelMaker.makeLabel(String.format(PRICE_LABEL_FORMAT, myPrice), "shop-item-price-label");
    myVBox = BoxMaker.makeVBox("VBoxId", 10, Pos.CENTER, nameLabel, item, priceLabel,
        buyButton, stripeButton, confirmStripe);
    myVBox.setLayoutX((GAP + X_SPACING) * (index % 3));
    myVBox.setLayoutY((GAP + Y_SPACING) * (index / 3));

  }

  private void buildButtons() {
    buyButton = ButtonMaker.makeTextButton("Buy" + myName, e -> buyItem(myName), "Buy");
    confirmStripe = ButtonMaker.makeTextButton("stripeConfirm" + myName, e ->
    {
      try {
        confirmStripePayment();
      } catch (StripeException ex) {

      }
    }, "Confirm Stripe Payment");
    confirmStripe.setDisable(true);
    confirmStripe.setVisible(false);
    stripeButton = ButtonMaker.makeTextButton("stripe" + myName, e ->
    {
      try {
        stripeIntegration.purchaseItem(myName);
        confirmStripe.setDisable(false);
        confirmStripe.setVisible(true);
      } catch (URISyntaxException | IOException | InterruptedException ex) {

      }
    }, "Pay with Stripe");
  }

  private void confirmStripePayment() throws StripeException {
    if (stripeIntegration.hasBeenPaid()) {
      confirmStripe.setVisible(false);
      confirmStripe.setDisable(true);
      buyItem(stripeButton.getId());
    }
  }

  private void buyItem(String name) {
    notifyObserver("buyItem", name);
  }

  public VBox getMyVBox() {
    return myVBox;
  }
}

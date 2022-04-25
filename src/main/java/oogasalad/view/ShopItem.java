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

/**
 * Stores the ImageView as well as the variables for a given ShopItem, has reference with the buy
 * buttpm using observables and listeners to apply the effects of buying an item from the shop.
 *
 * @author Luka Mdivnai, Minjun Kwak, Brandon Bae
 */
public class ShopItem extends PropertyObservable {

  private static final ResourceBundle WEAPON_IMAGE_RESOURCES = ResourceBundle.getBundle(
      "/WeaponImages");
  private static final String WEAPON_IMAGES_PATH = "images/weapon_images/";
  private static final String SHOP_NAME_LABEL_ID = "shop-item-name-label";
  private static final String PRICE_LABEL_FORMAT = "Price: %d Gold";
  private static final String SHOP_PRICE_LABEL_ID = "shop-item-price-label";
  private static final String SHOP_ITEM_ID = "shop-item";
  private static final String BUY_ITEM_METHOD = "buyItem";
  private static final String BUY_ID = "Buy";
  private static final String STRIPE_CONFIRM_ID = "stripeConfirm";
  private static final String PAY_WITH_STRIPE_TEXT = "Pay with Stripe";
  private static final String STRIPE_PREFIX = "stripe";
  private static final String CONFIRM_STRIPE_TEXT = "stripe";
  private static final double ITEM_SPACING = 10;
  private static final double ITEM_SIZE = 30;
  private static final double X_SPACING = 90;
  private static final double Y_SPACING = 150;
  private static final double GAP = 20;

  private ImageView item;
  private int myPrice;
  private VBox myVBox;
  private String myName;
  private String myUsableClassName;
  private StripeIntegration stripeIntegration;
  private Button buyButton;
  private Button confirmStripe;
  private Button stripeButton;

  public ShopItem(String itemName, int price, String usableClassName, int index,
      StripeIntegration stripeIntegration) {
    this.stripeIntegration = stripeIntegration;
    String imageName = WEAPON_IMAGE_RESOURCES.getString(usableClassName);
    item = new ImageView(new Image(WEAPON_IMAGES_PATH + imageName));
    item.setFitHeight(ITEM_SIZE);
    item.setFitWidth(ITEM_SIZE);
    myPrice = price;
    myName = itemName;
    myUsableClassName = usableClassName;
    buildButtons();
    Label nameLabel = LabelMaker.makeLabel(myName, SHOP_NAME_LABEL_ID);
    Label priceLabel = LabelMaker.makeLabel(String.format(PRICE_LABEL_FORMAT, myPrice),
        SHOP_PRICE_LABEL_ID);
    myVBox = BoxMaker.makeVBox(SHOP_ITEM_ID, ITEM_SPACING, Pos.CENTER, nameLabel, item, priceLabel,
        buyButton, stripeButton, confirmStripe);
    myVBox.setLayoutX((GAP + X_SPACING) * (index % 3));
    myVBox.setLayoutY((GAP + Y_SPACING) * (index / 3));

  }

  private void buildButtons() {
    buyButton = ButtonMaker.makeTextButton(BUY_ID + myName, e -> buyItem(myName), BUY_ID);
    confirmStripe = ButtonMaker.makeTextButton(STRIPE_CONFIRM_ID + myName, e ->
    {
      try {
        confirmStripePayment();
      } catch (StripeException ex) {

      }
    }, CONFIRM_STRIPE_TEXT);
    confirmStripe.setDisable(true);
    confirmStripe.setVisible(false);
    stripeButton = ButtonMaker.makeTextButton(STRIPE_PREFIX + myName, e ->
    {
      try {
        stripeIntegration.purchaseItem(myUsableClassName);
        confirmStripe.setDisable(false);
        confirmStripe.setVisible(true);
      } catch (URISyntaxException | IOException | InterruptedException ex) {

      }
    }, PAY_WITH_STRIPE_TEXT);
  }

  private void confirmStripePayment() throws StripeException {
    if (stripeIntegration.hasBeenPaid()) {
      confirmStripe.setVisible(false);
      confirmStripe.setDisable(true);
      buyItem(stripeButton.getId());
    }
  }

  private void buyItem(String name) {
    notifyObserver(BUY_ITEM_METHOD, name);
  }

  /**
   * returns the VBox containing the imageview, purchase buttons as well as name/price tags.
   *
   * @return the Vbox
   */
  public VBox getMyVBox() {
    return myVBox;
  }
}

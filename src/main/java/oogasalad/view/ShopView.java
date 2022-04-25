package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import oogasalad.PropertyObservable;
import oogasalad.com.stripe.StripeIntegration;
import oogasalad.model.utilities.usables.Usable;

/**
 * A Class which creates a view for the shop, it displays items in separate panes, and connects to the
 * backend through the use of observables and listeners. Depends on javaFX. Makes no assumptions.
 *
 *
 * @author Luka Mdivani
 */
public class ShopView extends PropertyObservable implements PropertyChangeListener {

  private static final double SCREEN_WIDTH = 1000;
  private static final double SCREEN_HEIGHT = 600;
  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "stylesheets/startStylesheet.css";

  private Scene myScene;
  private Map<String, ScrollPane> nameToPageMap = new HashMap<>();
  private Map<Tab, ScrollPane> shopPageMap = new HashMap<>();
  private BorderPane myPane;
  private StripeIntegration stripeIntegration;

  public ShopView(List<Usable> shopInventory) {
    myPane = new BorderPane();
    myPane.setId("startPane");
    ResourceBundle myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE+"ShopBuilder");
    stripeIntegration = new StripeIntegration();
    setUpTitle();
    setUpTabPane(myResources.getString("shopCategories").split(","));
    setUpButtons();
    makeShopItems(shopInventory);
  }

  private void setUpTitle() {
    Text title = new Text("Welcome to the Shop");
    title.setId("shopTitle");
    title.setFont(Font.font("Verdana", 20));
    myPane.setTop(title);
  }

  /**
   * returns the scene containing the shop window, so it can be set to a stage.
   *
   * @return the Scene containing the shop
   */
  public Scene getMyScene() {
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    return myScene;
  }

  private void setUpTabPane(String[] categories) {
    TabPane tabPane = new TabPane();
    tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
    tabPane.setSide(Side.LEFT);

    for(String category : categories) {
      tabPane.getTabs().addAll(makeTab(category));
    }
    tabPane.setOnMouseClicked(e -> {
      myPane.setCenter(null);
      myPane.setCenter(shopPageMap.get(tabPane.getSelectionModel().getSelectedItem()));
    });
    myPane.setLeft(tabPane);
    myPane.setCenter(nameToPageMap.get(categories[0]));
  }

  private Tab makeTab(String tabName) {
    Tab result = new Tab(tabName);
    ScrollPane sp = makeShopPage();
    shopPageMap.put(result, sp);
    nameToPageMap.put(tabName,sp);
    return result;
  }

  private ScrollPane makeShopPage() {
    ScrollPane result = new ScrollPane();
    result.setFitToHeight(true);
    Group shopPage = new Group();
    shopPage.maxWidth(200);
    result.setContent(shopPage);
    return result;
  }

  private void setUpButtons() {
  }

  private void makeShopItems(List<Usable> shopInventory) {
    for(Usable currUsable: shopInventory) {
      addShopItem(currUsable.getType(), currUsable.getMyID(), currUsable.getPrice(),
          currUsable.getClass().getSimpleName(),shopInventory.indexOf(currUsable));
    }
  }

  private void addShopItem(String category, String usableID, int price, String usableClassName, int index) {
    Group currentGroup = (Group) nameToPageMap.get(category).getContent();
    ShopItem newItem = new ShopItem(usableID,price,usableClassName, index,
        stripeIntegration);
    newItem.addObserver(this);
    currentGroup.getChildren().add(newItem.getMyVBox());
  }

  /**
   * The listener method of this BoardView that is called when the class it is observing notifies
   * this class
   *
   * @param evt the evt associated with the notification
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName(), evt.getNewValue());
  }
}

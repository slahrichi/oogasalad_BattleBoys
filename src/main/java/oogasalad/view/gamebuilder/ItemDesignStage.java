package oogasalad.view.gamebuilder;

/**
 * Class for selecting which items should be added to the shop, since items are conceptually similar
 * to weapons in their implementation, I extended that class, only changed the source of options.
 *
 * @author Luka Mdivani
 */
public class ItemDesignStage extends WeaponDesignStage {

  private final String PATH = "oogasalad.model.utilities.usables.items.";
  private final String TITLE = "CREATE CUSTOM ITEMS";

  public ItemDesignStage() {
    super();
    setTitle(TITLE);
  }

  @Override
  protected void setUpUsableData() {
    setUsableDataForKey(getMyBuilderResources().getString("possibleItemType"));
  }

  @Override
  protected void setUpClassPath() {
    setClassPath(PATH);
  }
}

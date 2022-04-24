package oogasalad.view.gamebuilder;

public class ItemDesignStage extends WeaponDesignStage{
  private final String PATH="oogasalad.model.utilities.usables.items.";
  public ItemDesignStage(){
    super();
  }
  @Override
  protected void setUpUsableData(){
    setUsableDataForKey( getMyBuilderResources().getString("possibleItemType"));
  }
  @Override
  protected  void setUpClassPath(){setClassPath(PATH);}
}

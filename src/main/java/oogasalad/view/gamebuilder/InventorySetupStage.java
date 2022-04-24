package oogasalad.view.gamebuilder;

public class InventorySetupStage extends GameVarSetupStage {

  String[] dataSource;
  public InventorySetupStage(String[] source) {
    dataSource=source;

  }

  @Override
  protected void setUp() {
    setUpVariableInput(dataSource);
  }
}

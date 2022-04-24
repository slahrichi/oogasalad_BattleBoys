package oogasalad.view.gamebuilder;

/**
 * A class which stores info about the amount of each weapon in a players inventory,at the start of
 * the game. Depends on JavaFX. Conceptually the idea was same as the GameVarSelection so I just
 * extended the class and changed the source of variables which needed to be customized.
 *
 * @author Luka Mdivani
 */
public class InventorySetupStage extends GameVarSetupStage {

  private String[] dataSource;

  public InventorySetupStage(String[] source) {
    dataSource = source;

  }

  @Override
  protected void setUp() {
    setUpVariableInput(dataSource);
  }
}

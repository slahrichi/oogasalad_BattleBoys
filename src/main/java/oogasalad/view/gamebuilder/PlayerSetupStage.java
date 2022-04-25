package oogasalad.view.gamebuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;


/**
 * Creates a window to add all the desired player types, extends BuilderStage, depends on JavaFX,
 * makes no assumptions.
 *
 * @author Luka Mdivani
 */
public class PlayerSetupStage extends BuilderStage {

  private BorderPane myPane;
  private String availablePlayerTypes;
  private String availableEngineTypes;
  private String needEngineTypes;
  private String selectedEngineType;
  private ComboBox engineTypeComboBox;
  private HBox playerSelectionBox;
  private List<String> playerList = new ArrayList<>();
  private List<String> engineList = new ArrayList<>();

  public PlayerSetupStage() {
    myPane = new BorderPane();
    availablePlayerTypes = getMyBuilderResources().getString("possiblePlayerType");
    needEngineTypes = getMyBuilderResources().getString("needsEngineSelection");
    myPane.setTop(makePlayerSelectionBox());
    myPane.setRight(setUpObjectView());
    selectedEngineType = getMyBuilderResources().getString("BlankEngineState");

    myPane.setBottom(makeContinueButton());
  }

  private HBox makePlayerSelectionBox() {
    HBox result = new HBox();
    playerSelectionBox = new HBox();
    ComboBox comboBox = makeComboBox(availablePlayerTypes.split(","));
    result.getChildren().add(comboBox);
    result.getChildren()
        .addAll(makeButton(getDictionaryResources().getString("selectPrompt"),
            e -> handlePlayerSelection(comboBox)), playerSelectionBox);

    return result;
  }

  private void resetSelection() {
    playerSelectionBox.getChildren().clear();
    engineTypeComboBox = null;
  }

  private void handlePlayerSelection(ComboBox comboBox) {
    try {
      resetSelection();

      String selection = comboBox.getValue().toString();
      if (Arrays.stream(needEngineTypes.split(",")).anyMatch(selection::equals)) {
        addEngineSelectionOption(playerSelectionBox, selection);
      }
      playerSelectionBox.getChildren()
          .add(makeButton(getDictionaryResources().getString("addPlayerPrompt"),
              e -> addSavePlayerButton(selection, engineTypeComboBox)));


    } catch (NullPointerException e) {
      showError(getDictionaryResources().getString("makeSelectionError"));
      e.printStackTrace();
    }
  }

  private void addSavePlayerButton(String selectedPlayerType,
      ComboBox engineTypeComboBox) {
    if (engineTypeComboBox != null) {
      try {
        selectedEngineType = engineTypeComboBox.getValue().toString();
      } catch (NullPointerException e) {

      }
    }
    addToObjectList(selectedPlayerType + selectedEngineType);
    playerList.add(selectedPlayerType);
    engineList.add(selectedEngineType);

  }

  /**
   * returns a list of corresponding decision engines.
   *
   * @return the list of DecisionEngines for each added player
   */
  public List<String> getEngineList() {
    return engineList;
  }

  private void addEngineSelectionOption(HBox result, String selection) {
    resetSelection();
    String[] engineOptions = getMyBuilderResources().getString(selection + "EngineOption")
        .split(",");
    engineTypeComboBox = makeComboBox(engineOptions);
    result.getChildren().add(engineTypeComboBox);


  }

  @Override
  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    return null;
  }

  @Override
  protected Object launch() {
    setUpStage(myPane);
    return playerList;
  }

  @Override
  protected void saveAndContinue() {
    closeWindow();
  }
}

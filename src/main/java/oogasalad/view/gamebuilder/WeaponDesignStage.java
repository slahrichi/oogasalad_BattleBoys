package oogasalad.view.gamebuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.usables.weapons.Weapon;
import oogasalad.view.maker.LabelMaker;

public class WeaponDesignStage extends BuilderStage {

  private BorderPane myPane;
  private int[][] stateMap;
  private String availableWeaponTypes;
  private Stage myStage;
  private final int MAX_DIMENSION = 10;
  private VBox centerPane;
  private Map<String, TextArea> varInputBoxes;
  private final String[] DEFAULT_STATE_OPTIONS = {"Inactive", "Active"};
  private final Color DEFAULT_INACTIVE_COLOR = Color.GRAY;
  private final Color DEFAULT_ACTIVE_COLOR = Color.LIME;
  private final int DEFAULT_CELL_SIZE = 20;
  private List<Color> colorList = new ArrayList<>();
  private String[] needAOEMapList;
  private int[][] damageMatrix;
  private final String DEFAULT_WEAPON_ID = "CustomWeapon";
  private TextArea idInputBox;
  private List<Integer> weaponStats;
  private List<Object> parameterList;
  private Object[] parameters;
  private Class<?>[] parameterTypes;
  private List<Class<?>> parameterTypesList;
  private List<Weapon> weaponList = new ArrayList<>();

  public WeaponDesignStage() {
    colorList.add(DEFAULT_INACTIVE_COLOR);
    colorList.add(DEFAULT_ACTIVE_COLOR);
    myPane = new BorderPane();
    stateMap = initializeMatrixWithValue(MAX_DIMENSION, MAX_DIMENSION, 0);
    availableWeaponTypes = getMyBuilderResources().getString("possibleWeaponType");
    needAOEMapList = getMyBuilderResources().getString("needsAOEMapWeapon").split(",");
    myStage = new Stage();
    myPane.setTop(makeWeaponSelectionPrompt(availableWeaponTypes.split(",")));
    myPane.setBottom(makeContinueButton());
    myPane.setRight(setUpObjectView());
    centerPane = new VBox();

    Scene myScene = new Scene(myPane, 1000, 500);
    myStage.setScene(myScene);
  }

  @Override
  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    Rectangle newCell = new Rectangle(xPos, yPos, DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE);
    newCell.setStroke(Color.BLACK);
    newCell.setFill(colorList.get(state));
    newCell.setOnMouseClicked(mouseEvent -> {
      stateMap[i][j] = getSelectedType();
      newCell.setFill(colorList.get(getSelectedType()));
      damageMatrix[i][j] = getSelectedType();
      if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
        makePopUpDialog(i, j);
      }
    });

    return newCell;
  }

  @Override
  protected Object launch() {
    myStage.showAndWait();
    return null;
  }

  private void makePopUpDialog(int i, int j) {
    TextInputDialog td = new TextInputDialog(
        String.valueOf(damageMatrix[i][j]));
    td.setHeaderText("Set Damage");
    td.setTitle("Damage");
    Optional<String> result = td.showAndWait();
    result.ifPresent(input -> {
      if (checkIntConversion(input)) {
        damageMatrix[i][j] = Integer.parseInt(input);
      }
    });
  }

  @Override
  protected Object saveAndContinue() {
    myStage.close();
    PlayerSetupStage bgds = new PlayerSetupStage();
    return null;
  }

  private VBox makeWeaponSelectionPrompt(String[] options) {
    VBox result = new VBox();
    ComboBox comboBox = makeComboBox(options);

    result.getChildren()
        .addAll(comboBox, makeButton("Select", e -> handleWeaponChoice(comboBox)));

    return result;
  }

  private void resetSelection() {
    myPane.setCenter(null);
    centerPane.getChildren().clear();
  }

  private void handleWeaponChoice(ComboBox comboBox) {
    try {
      resetSelection();
      String selection = comboBox.getValue().toString();
      String[] variables = getMyBuilderResources().getString(selection + "Variables").split(",");
      varInputBoxes = new HashMap<>();
      idInputBox = makeTextAreaWithDefaultValue(DEFAULT_WEAPON_ID);
      centerPane.getChildren()
          .add(new HBox(LabelMaker.makeLabel("id", "id" + "_label"), idInputBox));
      makeStatEditor(variables);
      if (needsGridDesignOption(selection)) {
        addGridDesignOption();
      }
      centerPane.getChildren().add(makeButton("Save Weapon", e -> saveWeapon(selection)));
      myPane.setCenter(centerPane);

    } catch (NullPointerException e) {
      System.out.println("Please Make Selection");
      e.printStackTrace();
    }
  }

  private Boolean needsGridDesignOption(String selection) {
    return Arrays.stream(needAOEMapList).anyMatch(selection::equals);
  }

  private void makeStatEditor(String[] variables) {
    for (String var : variables) {
      TextArea varInput = makeTextAreaWithDefaultValue("1");
      varInputBoxes.put(var, varInput);
      centerPane.getChildren().add(new HBox(LabelMaker.makeLabel(var, var + "_label"), varInput));
    }
  }


  private Boolean checkAllInput() {
    Boolean allInputsValid = true;
    for (String variable : varInputBoxes.keySet()) {
      if (!checkIntConversion(varInputBoxes.get(variable).getText())) {
        allInputsValid = false;
      }
    }
    if (idInputBox.getText().isEmpty()) {
      allInputsValid = false;
    }
    return allInputsValid;
  }

  private void getWeaponStats() {
    weaponStats = new ArrayList<>();
    for (String variable : varInputBoxes.keySet()) {
      int currentBoxInput = Integer.parseInt(varInputBoxes.get(variable).getText());
      weaponStats.add(currentBoxInput);
    }
  }

  private void saveWeapon(String selectedWeapon) {
    parameterList = new ArrayList<>();
    parameterTypesList = new ArrayList<>();
    if (checkAllInput()) {
      String weaponID = idInputBox.getText();
      parameterList.add(weaponID);
      getWeaponStats();
      for (int i : weaponStats) {
        parameterList.add(i);
      }
      if (needsGridDesignOption(selectedWeapon)) {
        parameterList.add(translateGridToMap());

      }
      getParameterClasses();
      addToObjectList(idInputBox.getText() + "_" + selectedWeapon);
      resetSelection();
      parameters = new Object[parameterList.size()];
      parameterList.toArray(parameters);

      parameterTypes = new Class<?>[parameterTypesList.size()];
      parameterTypesList.toArray(parameterTypes);
      String selectedWeaponClass = "oogasalad.model.utilities.usables.weapons."+selectedWeapon;
      try {
        weaponList.add((Weapon) createInstance(selectedWeaponClass, parameterTypes, parameters));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

  }

  private void getParameterClasses() {
    for (Object param : parameterList) {
      parameterTypesList.add(param.getClass());
    }
  }

  private Map<Coordinate, Integer> translateGridToMap() {
    Map<Coordinate, Integer> relativeCoordinatesMap = new HashMap<>();
    for (int i = 0; i < MAX_DIMENSION; i++) {
      for (int j = 0; j < MAX_DIMENSION; j++) {
        relativeCoordinatesMap.put(new Coordinate(i, j), stateMap[i][j]);
      }
    }
    return relativeCoordinatesMap;
  }

  private void addGridDesignOption() {
    damageMatrix = initializeMatrixWithValue(MAX_DIMENSION, MAX_DIMENSION, 0);
    stateMap = initializeMatrixWithValue(MAX_DIMENSION, MAX_DIMENSION, 0);
    stateMap[5][5] = 1;
    centerPane.getChildren().add(new HBox(
        arrangeCells(MAX_DIMENSION, MAX_DIMENSION, DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE, stateMap),
        displayColorChoice(DEFAULT_STATE_OPTIONS, colorList)));
  }

}

package oogasalad.view.gamebuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import oogasalad.model.utilities.Coordinate;
import oogasalad.view.maker.LabelMaker;

/**
 * Creates a design environment for weapons, generates all required variable design options for
 * specific weapons, subclass of BuilderStage, depends on JavaFX. Uses reflection to create correct
 * objects.
 *
 * @author Luka Mdivani
 */
public class WeaponDesignStage extends BuilderStage {

  private BorderPane myPane;
  private int[][] stateMap;
  private String availableUsableTypes;
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
  private final String DEFAULT_WEAPON_ID = "CustomUsable";
  private TextArea idInputBox;
  private List<Integer> weaponStats;
  private List<Object> parameterList;
  private Object[] parameters;
  private Class<?>[] parameterTypes;
  private List<Class<?>> parameterTypesList;
  private List<Object> weaponList = new ArrayList<>();
  private String classPath;
  private final String PATH = "oogasalad.model.utilities.usables.weapons.";
  private final String TITLE="Create Weapons";

  public WeaponDesignStage() {
    colorList.add(DEFAULT_INACTIVE_COLOR);
    colorList.add(DEFAULT_ACTIVE_COLOR);
    setUpClassPath();
    myPane = new BorderPane();
    stateMap = initializeMatrixWithValue(MAX_DIMENSION, MAX_DIMENSION, 0);
    needAOEMapList = getMyBuilderResources().getString("needsAOEMapWeapon").split(",");
    setUpUsableData();
    myPane.setTop(makeWeaponSelectionPrompt(availableUsableTypes.split(",")));
    myPane.setBottom(makeContinueButton());
    myPane.setRight(setUpObjectView());
    centerPane = new VBox();

  }

  protected void setUpUsableData() {
    availableUsableTypes = getMyBuilderResources().getString("possibleWeaponType");
  }

  protected void setClassPath(String path) {
    classPath = path;
  }

  protected void setUpClassPath() {
    classPath = PATH;
  }

  protected void setUsableDataForKey(String newData) {
    availableUsableTypes = newData;
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
    setTitle(TITLE);
    setUpStage(myPane);
    return weaponList;
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
  protected void saveAndContinue() {
    closeWindow();

  }

  private VBox makeWeaponSelectionPrompt(String[] options) {
    VBox result = new VBox();
    ComboBox comboBox = makeComboBox(options);

    result.getChildren()
        .addAll(comboBox, makeButton(getDictionaryResources().getString("selectPrompt"),
            e -> handleWeaponChoice(comboBox)));

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
        centerPane.getChildren()
            .add(LabelMaker.makeLabel(getDictionaryResources().getString("rightClickInfo"),
                "infoLabel"));
      }
      centerPane.getChildren().add(
          makeButton(getDictionaryResources().getString("savePrompt"), e -> saveWeapon(selection)));
      myPane.setCenter(centerPane);

    } catch (NullPointerException e) {
      showError(getDictionaryResources().getString("selectionError"));
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
      String selectedWeaponClass = classPath + selectedWeapon;
      try {
        weaponList.add(createInstance(selectedWeaponClass, parameterTypes, parameters));
      } catch (IOException e) {
        showError(getDictionaryResources().getString("reflectionError"));
      }
    }

  }

  private void getParameterClasses() {
    for (Object param : parameterList) {
      if (param instanceof Integer) {
        parameterTypesList.add(int.class);
      } else if (param instanceof HashMap) {
        parameterTypesList.add(Map.class);
      } else {
        parameterTypesList.add(param.getClass());
      }
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
    int centerCoord = 5;
    stateMap[centerCoord][centerCoord] = 1;
    centerPane.getChildren().add(new HBox(
        arrangeCells(MAX_DIMENSION, MAX_DIMENSION, DEFAULT_CELL_SIZE, DEFAULT_CELL_SIZE, stateMap),
        displayColorChoice(DEFAULT_STATE_OPTIONS, colorList)));
  }

  /**
   * returns Ids of all created weapons.
   *
   * @return id of all the user created weapons
   */
  public String[] getCreatedWeaponIds() {
    return getObjectListAsArray();
  }
}

package oogasalad.view.gamebuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
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
import oogasalad.model.utilities.tiles.ShipCell;

public class PieceDesignStage extends BuilderStage {

  private final int MAX_DIMENSION = 5;

  private int[][] stateMap;
  private List<Object> pieceList;
  private Map<String, int[][]> editableStats;
  private BorderPane myPane;
  private Object pieceType;
  private List<Color> colorList = new ArrayList<>();
  private final String[] DEFAULT_STATE_OPTIONS = {"Inactive", "Active"};
  private final Color DEFAULT_INACTIVE_COLOR = Color.GRAY;
  private final Color DEFAULT_ACTIVE_COLOR = Color.LIME;
  private Consumer<String> pathUpdateConsumer;
  private String availablePieceTypes;
  private final ListView<String> pathListView = new ListView<>();
  private ObservableList<String> piecePathList = FXCollections.observableArrayList();
  private final int MAX_LIST_WIDTH = 100;
  private final int MAX_LIST_HEIGHT = 400;
  private final double CELL_SIZE = 20;
  private TextArea idInputBox;
  private final String DEFAULT_PIECE_NAME = "Custom Piece";
  private List<Coordinate> patrolPath = new ArrayList<>();


  private String[] customizableStats;

  public PieceDesignStage() {
    myPane = new BorderPane();
    availablePieceTypes = getMyBuilderResources().getString("possiblePieceType");
    customizableStats = getMyBuilderResources().getString("pieceCellCustomParameters")
        .split(",");
    initializeCharacteristicMatrix();
    pathUpdateConsumer = e -> updatePath(e);

    pathListView.setItems(piecePathList);
    pathListView.setMaxSize(MAX_LIST_WIDTH, MAX_LIST_HEIGHT);

    myPane.setRight(setUpObjectView());

    colorList.add(DEFAULT_INACTIVE_COLOR);
    colorList.add(DEFAULT_ACTIVE_COLOR);

    myPane.setTop(makeSelectionComboBox(availablePieceTypes.split(",")));
    myPane.setBottom(makeContinueButton());

  }

  private void initializeCharacteristicMatrix() {

    editableStats = new HashMap<>();
    for (String stat : customizableStats) {
      editableStats.put(stat,
          initializeMatrixWithValue(MAX_DIMENSION, MAX_DIMENSION, 1));
    }
  }

  private VBox makeSelectionComboBox(String[] options) {
    VBox result = new VBox();
    ComboBox comboBox = makeComboBox(options);
    result.getChildren().add(comboBox);
    result.getChildren().add(makeButton("Select", e -> selectPieceType(result, comboBox)));
    return result;
  }

  private void makePopUpDialog(int i, int j) {

    for (String characteristic : customizableStats) {
      TextInputDialog td = new TextInputDialog(
          String.valueOf(editableStats.get(characteristic)[i][j]));
      td.setTitle(characteristic);
      Optional<String> result = td.showAndWait();
      result.ifPresent(input -> {
        if (checkIntConversion(input)) {
          editableStats.get(characteristic)[i][j] = Integer.parseInt(input);
        }
      });
    }

  }

  private void resetCustomization() {
    myPane.setCenter(null);
    stateMap = initializeMatrixWithValue(MAX_DIMENSION, MAX_DIMENSION, 0);
    stateMap[2][2]=1;
    piecePathList.clear();
    patrolPath.clear();
    myPane.setLeft(null);
  }


  @Override
  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    Rectangle newCell = new Rectangle(xPos, yPos, 20, 20);
    newCell.setStroke(Color.BLACK);
    newCell.setFill(colorList.get(state));
    newCell.setOnMouseClicked(mouseEvent -> {
      stateMap[i][j] = getSelectedType();
      newCell.setFill(colorList.get(getSelectedType()));
      if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
        makePopUpDialog(i, j);
      }
    });

    return newCell;
  }

  @Override
  protected Object launch() {
    setUpStage(myPane);
    return pieceList;
  }

  @Override
  protected void saveAndContinue() {
    closeWindow();

  }


  private void selectPieceType(VBox result, ComboBox comboBox) {
    pieceType = comboBox.getValue();
    if (!pieceType.equals(null)) {
      resetCustomization();
      result = makeSelectionComboBox(availablePieceTypes.split(","));
      myPane.setTop(result);
      String[] reqVars = getMyBuilderResources().getString(pieceType + "PieceRequiredInfo")
          .split(",");
      if (!reqVars[0].isEmpty()) {
        result.getChildren()
            .add(makeComboBoxWithVariable(reqVars, "Add to Path", pathUpdateConsumer));
      }
      myPane.setLeft(pathListView);
      idInputBox = makeTextAreaWithDefaultValue(DEFAULT_PIECE_NAME);
      String pieceTypeString = pieceType.toString();
      Boolean needsPath = !reqVars[0].isEmpty();
      myPane.setCenter(
          new VBox(idInputBox,
              new HBox(arrangeCells(MAX_DIMENSION, MAX_DIMENSION, CELL_SIZE, CELL_SIZE, stateMap),
                  displayColorChoice(DEFAULT_STATE_OPTIONS, colorList)),
              makeButton("Save Piece", e -> savePiece(pieceTypeString, needsPath))));
    }
  }

  private List<ShipCell> makeCellList(int[][] pieceMap) {
    List<ShipCell> cellList = new ArrayList<>();

    for (int i = 0; i < pieceMap.length; i++) {
      for (int j = 0; j < pieceMap[0].length; j++) {
        if (pieceMap[i][j] != 0) {
          cellList.add(
              new ShipCell(editableStats.get(customizableStats[0])[i][j], new Coordinate(i, j),
                  editableStats.get(customizableStats[1])[i][j],
                  idInputBox.getText() + "cell"));
        }
      }

    }
    return cellList;
  }


  private void savePiece(String selectedPieceType, Boolean needsPath) {
    String selectedPieceClass = "oogasalad.model.utilities." + selectedPieceType + "Piece";
    findReferencePoint(stateMap);
    int[][] pieceMap = cropToActiveGrid(stateMap);
    List<Object> parametersList = new ArrayList<>();
    parametersList.add(makeCellList(pieceMap));
    if (needsPath) {
      parametersList.add(patrolPath);
    }
    parametersList.add(makeRelativeCoordinateMap(pieceMap));
    parametersList.add(idInputBox.getText());

    Object[] parameters = new Object[parametersList.size()];
    parametersList.toArray(parameters);
    pieceList = new ArrayList<>();
    try {
      pieceList.add(
          createInstance(selectedPieceClass, getParameterType(parameters), parameters));

      addToObjectList(idInputBox.getText());
    } catch (IOException e) {
      e.printStackTrace();
    }

    findReferencePoint(stateMap);
    resetCustomization();
  }

  private Class<?>[] getParameterType(Object[] parameters) {
    Class<?>[] parameterTypes = new Class<?>[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      if (parameters[i] instanceof ArrayList) {
        parameterTypes[i] = List.class;
      } else {
        parameterTypes[i] = parameters[i].getClass();
      }
    }

    return parameterTypes;
  }

  private void updatePath(String path) {
    String pathElements[] = path.split(",");
    String pathDirection = pathElements[0];
    String pathLength = pathElements[1];
    if (checkIntConversion(pathLength)) {
      int length = Integer.parseInt(pathLength);
      int xDelta = Integer.parseInt(getMyBuilderResources().getString(pathDirection + "X"));
      int yDelta = Integer.parseInt(getMyBuilderResources().getString(pathDirection + "Y"));
      patrolPath.add(new Coordinate(xDelta * length, yDelta * length));
      piecePathList.add(path);
    }
  }


}

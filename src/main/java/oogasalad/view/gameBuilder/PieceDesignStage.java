package oogasalad.view.gameBuilder;

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

public class PieceDesignStage extends BuilderStage {

  private final int MAX_DIMENSION = 5;

  private int[][] stateMap;
  private Map<String, int[][]> editableStats;
  private BorderPane myPane;
  private Object pieceType;
  private List<Color> colorList = new ArrayList<>();
  private final String[] DEFAULT_STATE_OPTIONS = {"Inactive", "Active"};
  private final Color DEFAULT_INACTIVE_COLOR = Color.GRAY;
  private final Color DEFAULT_ACTIVE_COLOR = Color.LIME;
  private Consumer<String> pathUpdateConsumer;
  private String availablePieceTypes;
  private final ListView<String> listView = new ListView<>();
  private ObservableList<String> items = FXCollections.observableArrayList();
  private final int MAX_LIST_WIDTH = 100;
  private final int MAX_LIST_HEIGHT = 400;
  private final double CELL_SIZE = 20;
  private String[] customizable;

  public PieceDesignStage() {
    myPane = new BorderPane();
    stateMap = initializeMatrixWithValue(MAX_DIMENSION, MAX_DIMENSION, 0);
    availablePieceTypes = getMyBuilderResources().getString("possiblePieceType");
    customizable = getMyBuilderResources().getString("pieceCellCustomParameters")
        .split(",");
    initializeCharacteristicMatrix();
    pathUpdateConsumer = e -> updatePath(e);

    listView.setItems(items);
    listView.setMaxSize(MAX_LIST_WIDTH, MAX_LIST_HEIGHT);

    colorList.add(DEFAULT_INACTIVE_COLOR);
    colorList.add(DEFAULT_ACTIVE_COLOR);

    myPane.setTop(makePieceSelectionBox(availablePieceTypes.split(",")));
    myPane.setRight(displayColorChoice(DEFAULT_STATE_OPTIONS, colorList));
    myPane.setBottom(makeContinueButton());
    Stage myStage = new Stage();

    Scene myScene = new Scene(myPane, 1000, 500);
    myStage.setScene(myScene);
    myStage.showAndWait();
  }

  private void initializeCharacteristicMatrix() {

    editableStats = new HashMap<>();
    for (String charactristic : customizable) {
      editableStats.put(charactristic,
          initializeMatrixWithValue(MAX_DIMENSION, MAX_DIMENSION, 1));
    }
  }

  private void makePopUpDialog(int i, int j) {

    for (String characteristic : customizable) {
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
    items.clear();
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
      if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
        if (mouseEvent.getClickCount() == 2) {
          makePopUpDialog(i, j);
        }
      }
    });

    return newCell;
  }

  @Override
  protected Object saveAndContinue() {
    findReferencePoint();
    return null;
  }

  private VBox makePieceSelectionBox(String[] options) {
    VBox result = new VBox();
    ComboBox comboBox = makeComboBox(options);
    result.getChildren().add(comboBox);
    result.getChildren().add(makeButton("Select", e -> selectPieceType(result, comboBox)));
    return result;
  }


  private void selectPieceType(VBox result, ComboBox comboBox) {
    pieceType = comboBox.getValue();
    if (!pieceType.equals(null)) {
      resetCustomization();
      result = makePieceSelectionBox(availablePieceTypes.split(","));
      myPane.setTop(result);
      String[] reqVars = getMyBuilderResources().getString(pieceType + "PieceRequiredInfo")
          .split(",");
      if (!reqVars[0].isEmpty()) {
        result.getChildren()
            .add(makeComboBoxWithVariable(reqVars, "Add to Path", pathUpdateConsumer));
      }
      myPane.setLeft(listView);
      myPane.setCenter(arrangeCells(MAX_DIMENSION, MAX_DIMENSION, CELL_SIZE, CELL_SIZE, stateMap));
    }
  }

  private void updatePath(String path) {
    items.add(path);
  }

  private void findReferencePoint() {
    int minX = MAX_DIMENSION;
    int minY = MAX_DIMENSION;
    int maxX = 0;
    int maxY = 0;
    for (int i = 0; i < stateMap.length; i++) {
      for (int j = 0; j < stateMap[0].length; j++) {
        if (stateMap[i][j] != 0) {
          if (i <= minX && j <= minY) {
            minX = i;
            minY = j;
          }
          if (i >= minX && j >= minY) {
            maxX = i;
            maxY = j;
          }
        }
      }
    }
  }

  // private Piece makePiece(){
  //   Piece result = new StaticPiece();
  //       ShipCell a = new
  // }

  private HBox makeComboBoxWithVariable(String[] options, String buttonText,
      Consumer<String> consumer) {

    TextArea infoBox = new TextArea();
    ComboBox comboBox = makeComboBox(options);
    infoBox.setMaxSize(50, 20);
    HBox result = new HBox(comboBox);
    result.getChildren().add(infoBox);
    result.getChildren()
        .add(makeButton(buttonText, e -> consumer.accept(comboBox.getValue() + infoBox.getText())));
    return result;
  }


}

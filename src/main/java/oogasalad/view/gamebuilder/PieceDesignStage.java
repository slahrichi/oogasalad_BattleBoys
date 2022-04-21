package oogasalad.view.gamebuilder;

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
  private final ListView<String> pathListView = new ListView<>();
  private ObservableList<String> piecePathList = FXCollections.observableArrayList();
  private final int MAX_LIST_WIDTH = 100;
  private final int MAX_LIST_HEIGHT = 400;
  private final double CELL_SIZE = 20;
  private TextArea idInputBox;
  private final String DEFAULT_PIECE_NAME = "Custom Piece";

  private String[] customizableStats;
  private Stage myStage;

  public PieceDesignStage() {
    myPane = new BorderPane();
    stateMap = initializeMatrixWithValue(MAX_DIMENSION, MAX_DIMENSION, 0);
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
    myStage = new Stage();

    Scene myScene = new Scene(myPane, 1000, 500);
    myStage.setScene(myScene);
    myStage.show();
  }

  private void initializeCharacteristicMatrix() {

    editableStats = new HashMap<>();
    for (String charactristic : customizableStats) {
      editableStats.put(charactristic,
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
    piecePathList.clear();
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
    myStage.close();
    WeaponDesignStage wds = new WeaponDesignStage();
    return null;
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
      myPane.setCenter(
          new VBox(idInputBox,
              new HBox(arrangeCells(MAX_DIMENSION, MAX_DIMENSION, CELL_SIZE, CELL_SIZE, stateMap),
                  displayColorChoice(DEFAULT_STATE_OPTIONS, colorList)),
              makeButton("Save Piece", e -> savePiece())));
    }
  }

  private void savePiece() {
    addToObjectList(idInputBox.getText());
    resetCustomization();
  }

  private void updatePath(String path) {
    piecePathList.add(path);
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


}

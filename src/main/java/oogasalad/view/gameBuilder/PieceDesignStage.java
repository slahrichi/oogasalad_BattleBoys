package oogasalad.view.gameBuilder;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class PieceDesignStage extends BuilderStage {

  private final int MAX_DIMENSION = 5;

  private int[][] stateMap;
  private BorderPane myPane;
  private Object pieceType;
  private List<String> piecePath = new ArrayList<>();
  private List<Color> colorList = new ArrayList<>();
  private final String[] DEFAULT_STATE_OPTIONS = {"Inactive", "Active"};
  private final Color DEFAULT_INACTIVE_COLOR = Color.GRAY;
  private final Color DEFAULT_ACTIVE_COLOR = Color.LIME;
  private String availablePieceTypes;
  private final ListView<String> listView = new ListView<>();
  private ObservableList<String> items = FXCollections.observableArrayList();
  private final int MAX_LIST_WIDTH=100;
  private final int MAX_LIST_HEIGHT=400;
  private final double CELL_SIZE=20;

  public PieceDesignStage() {
    myPane = new BorderPane();
    stateMap = initializeBlankMap(MAX_DIMENSION, MAX_DIMENSION);
    availablePieceTypes = getMyBuilderResources().getString("possiblePieceType");

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

  private void resetCustomization(){
    myPane.setCenter(null);
    stateMap = initializeBlankMap(MAX_DIMENSION, MAX_DIMENSION);
    items.clear();
    myPane.setLeft(null);
  }



  @Override
  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    Rectangle newCell = new Rectangle(xPos, yPos, 20, 20);
    newCell.setStroke(Color.BLACK);
    newCell.setFill(colorList.get(state));
    newCell.setOnMouseClicked(e -> {
      stateMap[i][j] = 1;
      newCell.setFill(colorList.get(getSelectedType()));
    });

    return newCell;
  }

  @Override
  protected void saveAndContinue() {

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
      result=makePieceSelectionBox(availablePieceTypes.split(","));
      myPane.setTop(result);
      String[] reqVars = getMyBuilderResources().getString(pieceType + "PieceRequiredInfo")
          .split(",");
      if (!reqVars[0].isEmpty()) {
        result.getChildren().add(makeComboBoxWithVariable(reqVars));
      }
      myPane.setLeft(listView);
      myPane.setCenter(arrangeCells(MAX_DIMENSION, MAX_DIMENSION, CELL_SIZE,CELL_SIZE, stateMap));
    }
  }

  private void updatePath(String path) {
    items.add(path);
  }

  private HBox makeComboBoxWithVariable(String[] options) {

    TextArea infoBox = new TextArea();
    ComboBox comboBox = makeComboBox(options);
    infoBox.setMaxSize(50, 20);
    HBox result = new HBox(comboBox);
    result.getChildren().add(infoBox);
    result.getChildren()
        .add(makeButton("Add to Path", e -> updatePath(comboBox.getValue() + infoBox.getText())));
    return result;
  }


}

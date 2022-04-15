package oogasalad.view.gameBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class BoardSetUpStage extends BuilderStage {


  private int width;
  private int height;
  private static final int DEFAULT_DIMENSION = 10;
  private static final int DEFAULT_INPUT_BOX_WIDTH = 60;
  private static final int DEFAULT_INPUT_BOX_HEIGHT = 20;
  private double MAX_GRID_WIDTH = 400;
  private double MAX_GRID_HEIGHT = 400;


  private BorderPane myPane = new BorderPane();
  private int selectedType = 1;
  private List<Color> colorList = new ArrayList<>();
  private int[][] stateMap;
  private TextArea widthInput;
  private TextArea heightInput;
  private static final Color DEFAULT_INACTIVE_COLOR = Color.GRAY;
  private static final Color DEFAULT_ACTIVE_COLOR = Color.BLUE;
  private static final String[] DEFAULT_STATE_OPTIONS = {"Inactive", "Active"};

  private Consumer<Integer> widthChange;
  private Consumer<Integer> heightChange;
  private Stage myStage;

  public BoardSetUpStage() {
    width = DEFAULT_DIMENSION;
    height = DEFAULT_DIMENSION;

    colorList.add(DEFAULT_INACTIVE_COLOR);
    colorList.add(DEFAULT_ACTIVE_COLOR);

    widthChange = i -> setWidth(i);
    heightChange = i -> setHeight(i);

    stateMap = initializeBlankMap(height, width);
    drawGrid();
    myPane.setTop(makeInfoInput());
    myPane.setRight(displayColorChoice(DEFAULT_STATE_OPTIONS, colorList));
    myPane.setBottom(makeContinueButton());
    myStage = new Stage();
    myStage.setScene(getScene());
    myStage.show();
  }

  private void setWidth(int newWidth) {
    width = newWidth;
  }

  private void setHeight(int newHeight) {
    height = newHeight;
  }

  private void drawGrid() {
    myPane.setCenter(null);
    myPane.setCenter(
        arrangeCells(height, width, MAX_GRID_HEIGHT / height, MAX_GRID_WIDTH / width, stateMap));
  }

  public Scene getScene() {
    return new Scene(myPane, 900, 500);
  }

  private HBox makeInfoInput() {
    HBox result = new HBox();
    widthInput = makeTextArea();
    heightInput = makeTextArea();
    Label widthLabel = new Label("Width");
    Label heightLabel = new Label("Height");
    result.getChildren()
        .addAll(widthLabel, widthInput, heightLabel, heightInput, setDimensionButton());
    return result;
  }

  private Button setDimensionButton() {
    Button result = new Button("Set Dimension");
    result.setOnAction(e -> {
      checkAndSetDimension(widthInput.getText(), widthChange);
      checkAndSetDimension(heightInput.getText(), heightChange);
    });
    return result;
  }

  private void checkAndSetDimension(String s, Consumer<Integer> changeConsumer) {
    if (!s.isEmpty() && checkIntConversion(s)) {
      changeConsumer.accept(Integer.valueOf(s));

      stateMap = initializeBlankMap(height, width);
      drawGrid();
    } else {
      widthInput.clear();
    }


  }


  private TextArea makeTextArea() {
    TextArea result = new TextArea();
    result.setMaxSize(DEFAULT_INPUT_BOX_WIDTH, DEFAULT_INPUT_BOX_HEIGHT);

    return result;
  }

  private void makeInputBox() {

  }


  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    Rectangle newCell = new Rectangle(xPos, yPos,
        MAX_GRID_WIDTH / width,MAX_GRID_HEIGHT / height);
    newCell.setStroke(Color.BLACK);
    newCell.setFill(colorList.get(state));
    newCell.setOnMouseClicked(e -> {
      stateMap[i][j] = getSelectedType();
      drawGrid();
    });

    return newCell;
  }


  protected void saveAndContinue() {
    //write to file FIXME
    myStage.close();
    PieceDesignStage p = new PieceDesignStage();
  }
}

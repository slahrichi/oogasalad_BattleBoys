package oogasalad.view.gamebuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.maker.LabelMaker;

/**
 * A class for setting up the dimensions and the shape of the board, depends on JavaFX and
 * BuilderStage.java no assumptions made.
 *
 * @author Luka Mdivani
 */
public class BoardSetUpStage extends BuilderStage {


  private int width;
  private int height;
  private static final int DEFAULT_DIMENSION = 10;
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
  private CellState[][] board;
  private static final int INITIAL_STATUS = 1;

  private Consumer<Integer> widthChange;
  private Consumer<Integer> heightChange;

  public BoardSetUpStage() {
    width = DEFAULT_DIMENSION;
    height = DEFAULT_DIMENSION;

    colorList.add(DEFAULT_INACTIVE_COLOR);
    colorList.add(DEFAULT_ACTIVE_COLOR);

    widthChange = i -> setWidth(i);
    heightChange = i -> setHeight(i);

    stateMap = initializeMatrixWithValue(height, width, INITIAL_STATUS);
    drawGrid();
    myPane.setTop(makeInfoInput());
    myPane.setRight(displayColorChoice(DEFAULT_STATE_OPTIONS, colorList));
    myPane.setBottom(makeContinueButton());

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


  private HBox makeInfoInput() {
    HBox result = new HBox();
    widthInput = makeTextArea();
    heightInput = makeTextArea();
    Label widthLabel = LabelMaker.makeLabel(getDictionaryResources().getString("widthPrompt"),
        "width-label");
    Label heightLabel = LabelMaker.makeLabel(getDictionaryResources().getString("heightPrompt"),
        "height-label");
    result.getChildren()
        .addAll(widthLabel, widthInput, heightLabel, heightInput, setDimensionButton());
    return result;
  }

  private Button setDimensionButton() {
    Button result = new Button(getDictionaryResources().getString("dimensionPrompt"));
    result.setOnAction(e -> {
      checkAndSetDimension(widthInput.getText(), widthChange);
      checkAndSetDimension(heightInput.getText(), heightChange);
    });
    return result;
  }

  private void checkAndSetDimension(String s, Consumer<Integer> changeConsumer) {
    if (!s.isEmpty() && checkIntConversion(s)) {
      changeConsumer.accept(Integer.valueOf(s));

      stateMap = initializeMatrixWithValue(height, width, INITIAL_STATUS);
      drawGrid();
    } else {
      widthInput.clear();
    }


  }


  protected Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    Rectangle newCell = new Rectangle(xPos, yPos, MAX_GRID_WIDTH / width, MAX_GRID_HEIGHT / height);
    newCell.setStroke(Color.BLACK);
    newCell.setFill(colorList.get(state));
    newCell.setOnMouseClicked(e -> {
      stateMap[i][j] = getSelectedType();
      drawGrid();
    });

    return newCell;
  }

  @Override
  protected Object launch() {
    setUpStage(myPane);
    return board;
  }

  private void convertToCellStates() {
    board = new CellState[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        board[i][j] = CellState.of(stateMap[i][j]);
      }
    }
  }


  protected void saveAndContinue() {
    findReferencePoint(stateMap);
    stateMap = cropToActiveGrid(stateMap);
    width = stateMap.length;
    height = stateMap[0].length;

    convertToCellStates();
    closeWindow();

  }
}

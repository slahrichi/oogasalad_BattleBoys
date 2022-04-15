package oogasalad.view;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import oogasalad.view.maker.LabelMaker;

public class BoardSetUpStage extends BuilderStage {


  private int width;
  private int height;
  private static final int DEFAULT_DIMENSION = 10;
  private static final int DEFAULT_INPUT_BOX_WIDTH = 60;
  private static final int DEFAULT_INPUT_BOX_HEIGHT = 20;
  private double cellSize = 50;


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

    stateMap =  initializeBlankMap(height, width);
    drawGrid();
    myPane.setTop(makeInfoInput());
    myPane.setRight(displayColorChoice());
    Button continueButton = new Button("Continue");
    continueButton.setOnAction(e->saveAndContinue());
    myPane.setBottom(continueButton);
     myStage= new Stage();
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
    myPane.setCenter(arrangeCells(height, width, cellSize, stateMap));
  }

  public Scene getScene() {
    return new Scene(myPane, 1100, 500);
  }

  private HBox makeInfoInput() {
    HBox result = new HBox();
    widthInput = makeTextArea();
    heightInput = makeTextArea();
    Label widthLabel = LabelMaker.makeLabel("Width", "width-label");
    Label heightLabel = LabelMaker.makeLabel("Height", "height-label");
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
    if (!s.isEmpty()) {
      try {
        changeConsumer.accept(Integer.valueOf(s));

        stateMap = initializeBlankMap(height, width);
        drawGrid();
      } catch (NumberFormatException e) {

        widthInput.clear();
      }

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
    Rectangle newCell = new Rectangle(xPos, yPos, cellSize, cellSize);
    newCell.setStroke(Color.BLACK);
    newCell.setFill(colorList.get(state));
    newCell.setOnMouseClicked(e -> {
      stateMap[i][j] = selectedType;
      drawGrid();
    });

    return newCell;
  }

  private VBox displayColorChoice() {
    VBox result = new VBox();
    for (Color c : colorList) {
      result.getChildren().add(new HBox(new Text(DEFAULT_STATE_OPTIONS[colorList.indexOf(c)]),
          createColorOptionRectangle(c)));

    }

    return result;
  }

  private Rectangle createColorOptionRectangle(Color c) {
    Rectangle result = new Rectangle(50, 25);
    result.setFill(c);
    result.setOnMouseClicked(e -> {
      result.setStroke(Color.RED);
      selectedType = colorList.indexOf(c);
    });
    return result;
  }

  protected void saveAndContinue() {
    //write to file FIXME
    myStage.close();
    PieceDesignStage p=new PieceDesignStage();
  }
}

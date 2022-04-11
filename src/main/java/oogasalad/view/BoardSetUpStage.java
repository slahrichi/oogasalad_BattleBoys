package oogasalad.view;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javafx.scene.Group;
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

public class BoardSetUpStage {


  private int width;
  private int height;
  private static final int DEFAULT_DIMENSION = 10;
  private static final int DEFAULT_INPUT_BOX_WIDTH = 60;
  private static final int DEFAULT_INPUT_BOX_HEIGHT = 20;
  private double cellSize = 50;


  private BorderPane myPane = new BorderPane();
  private int selectedType = 1;
  private List<Color> cl = new ArrayList<>();
  private int[][] stateMap;
  private TextArea widthInput;
  private TextArea heightInput;

  private Consumer<Integer> widthChange;
  private Consumer<Integer> heightChange;

  public BoardSetUpStage() {
    width = DEFAULT_DIMENSION;
    height = DEFAULT_DIMENSION;
    cl.add(Color.BLUE);
    cl.add(Color.GRAY);
    cl.add(Color.RED);

    widthChange= i->setWidth(i);
    heightChange=i->setHeight(i);


    stateMap = new int[height][width];
    initializeBlankMap();
    drawGrid();
    myPane.setTop(makeInfoInput());
    myPane.setRight( displayColorChoice());
  }

  private void setWidth(int newWidth){
    width=newWidth;
  }
  private void setHeight(int newHeight){
    height=newHeight;
  }

  private void drawGrid(){
    myPane.setCenter(null);
    myPane.setCenter(arrangeCells());
  }

  public Scene getScene() {
    return new Scene(myPane, 1100, 500);
  }

  private HBox makeInfoInput() {
   HBox result = new HBox();
   widthInput = makeTextArea();
   heightInput = makeTextArea();
   Label widthLabel = new Label("Width");
   Label heightLabel = new Label("Height");
   result.getChildren().addAll(widthLabel,widthInput,heightLabel,heightInput,setDimensionButton());
   return  result;
  }

  private Button setDimensionButton(){
    Button result = new Button("Set Dimension");
    result.setOnAction(e->{
      checkAndSetDimension(widthInput.getText(),widthChange);
      checkAndSetDimension(heightInput.getText(),heightChange);
    });
    return result;
  }

  private void checkAndSetDimension(String s, Consumer<Integer> changeConsumer){
    if (!s.isEmpty()){
      try{
        changeConsumer.accept( Integer.valueOf(s) );

        stateMap = new int[height][width];
        initializeBlankMap();
        drawGrid();
      }
      catch (NumberFormatException e){

        widthInput.clear();
      }

    }
  }


  private TextArea makeTextArea(){
    TextArea result = new TextArea();
    result.setMaxSize(DEFAULT_INPUT_BOX_WIDTH,DEFAULT_INPUT_BOX_HEIGHT);

    return result;
  }

  private void makeInputBox(){

  }

  private void initializeBlankMap() {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        stateMap[i][j] = 0;
      }
    }
  }

  protected Group arrangeCells() {
    Group cellGroup = new Group();
    double xPos;
    double yPos = 0;
    for (int i = 0; i < height; i++) {
      xPos = 0;
      for (int j = 0; j < width; j++) {
        cellGroup.getChildren().add(createCell(xPos, yPos, i, j, stateMap[i][j]));

        xPos = xPos + cellSize;
      }
      yPos = yPos + cellSize;
    }
    return cellGroup;
  }

  private void setDimension() {

  }

  private void changeState() {

  }


  private Rectangle createCell(double xPos, double yPos, int i, int j, int state) {
    Rectangle newCell = new Rectangle(xPos, yPos, cellSize, cellSize);
    newCell.setStroke(Color.BLACK);
    newCell.setFill(cl.get(state));
    newCell.setOnMouseClicked(e -> {
      stateMap[i][j] = selectedType;
      drawGrid();
    });

    return newCell;
  }

  private VBox displayColorChoice(){
    VBox result = new VBox();
    for(Color c : cl){
      result.getChildren().add(new HBox(new Text("test"),  createColorOptionRectangle(c) ));

    }

    return  result;
  }

  private Rectangle createColorOptionRectangle(Color c){
    Rectangle result = new Rectangle(50,25);
    result.setFill(c);
    result.setOnMouseClicked(e->{selectedType=cl.indexOf(c);});
    return  result;
  }


}

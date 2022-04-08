package oogasalad.view.panes;

import java.util.Arrays;
import java.util.Collection;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.ShipIndicatorView;

// need to hold the ship list as well as create the ships to display

public class SetShipPane extends TitledPane{

  private ShipIndicatorView shipIndicatorView;
  private boolean lastPiecePlaced = false;
  private double myShipSize;

  public SetShipPane(double size){
    myShipSize = size;
    shipIndicatorView = new ShipIndicatorView(myShipSize, new CellState[][]{{CellState.WATER}}, 0);
    setUpPane();
  }


  private void setUpPane(){
    this.setContent(shipIndicatorView.getBoardPane());
    this.setPrefSize(300, 300);
    this.setId("shipPane");
    this.setText("Ships");
    this.setExpanded(true);
  }

//  private void setUpShips(){
//    StringBuilder sb = new StringBuilder();
//    Label testLabel = new Label();
//    for(Coordinate[] coord : shipList){
//      sb.append(coord.toString() + "\n");
//    }
//    testLabel.setText(sb.toString());
//    shipPane.setContent(testLabel);
//  }

  public void updateShownPiece(Collection<Coordinate> relativeCoords) {
    // Change ship indicator image
    System.out.println(relativeCoords);
    this.setContent(new ShipIndicatorView(myShipSize, getArrayRepresentation(relativeCoords), 0).getBoardPane());
  }

  public void showListCompletion(){
    this.setContent(new Label("All ships placed."));
  }

  private CellState[][] getArrayRepresentation(Collection<Coordinate> relativeCoords) {
    int numRows = 0;
    int numCols = 0;

    for(Coordinate c : relativeCoords) {
      numRows = Math.max(numRows, c.getRow() + 1);
      numCols = Math.max(numCols, c.getColumn() + 1);
    }

    CellState[][] arrayRepresentation = new CellState[numRows][numCols];

    for(int i = 0; i < arrayRepresentation.length; i++) {
      for (int j = 0; j < arrayRepresentation[0].length; j++) {
        arrayRepresentation[i][j] = CellState.NOT_DEFINED;
      }
    }

    for(Coordinate c : relativeCoords) {
      arrayRepresentation[c.getRow()][c.getColumn()] = CellState.SHIP_HEALTHY;
    }

    return arrayRepresentation;
  }


}

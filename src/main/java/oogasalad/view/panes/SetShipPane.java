package oogasalad.view.panes;


import java.util.Collection;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import oogasalad.model.utilities.Coordinate;
import oogasalad.view.ShipIndicatorView;

// need to hold the ship list as well as create the ships to display

public class SetShipPane {

  private TitledPane shipPane;
  private ShipIndicatorView shipIndicatorView;
  private boolean lastPiecePlaced = false;
  private double myShipSize;

  public SetShipPane(double size){
    myShipSize = size;
    shipPane = new TitledPane();
    shipIndicatorView = new ShipIndicatorView(myShipSize, new int[][]{{1}}, 0);
    setUpPane();
  }


  private void setUpPane(){
    shipPane.setContent(shipIndicatorView.getBoardPane());
    shipPane.setPrefSize(300, 300);
    shipPane.setId("shipPane");
    shipPane.setText("Ships");
    shipPane.setExpanded(true);
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
    shipPane.setContent(new ShipIndicatorView(myShipSize, getArrayRepresentation(relativeCoords), 0).getBoardPane());
  }

  public void showListCompletion(){
    shipPane.setContent(new Label("All ships placed."));
  }

  private int[][] getArrayRepresentation(Collection<Coordinate> relativeCoords) {
    int numRows = 0;
    int numCols = 0;

    for(Coordinate c : relativeCoords) {
      numRows = Math.max(numRows, c.getRow() + 1);
      numCols = Math.max(numCols, c.getColumn() + 1);
    }

    int[][] arrayRepresentation = new int[numRows][numCols];

    for(Coordinate c : relativeCoords) {
      arrayRepresentation[c.getRow()][c.getColumn()] = 1;
    }

    return arrayRepresentation;
  }

  public TitledPane getShipPane(){
    return shipPane;
  }

}

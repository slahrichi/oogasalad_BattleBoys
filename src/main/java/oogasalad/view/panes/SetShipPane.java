package oogasalad.view.panes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.ShipIndicatorView;

// need to hold the ship list as well as create the ships to display

public class SetShipPane {

  private TitledPane shipPane;
  private List<ShipIndicatorView> shipViews;
  private VBox shipIndicatorsBox;
  private boolean lastPiecePlaced = false;
  private double myShipSize;

  public SetShipPane(double size){
    myShipSize = size;
    shipPane = new TitledPane();
    shipViews = new ArrayList<>();
    shipIndicatorsBox = new VBox();
    shipIndicatorsBox.setAlignment(Pos.CENTER);
    shipIndicatorsBox.setSpacing(20);
    shipViews.add(new ShipIndicatorView(myShipSize, new CellState[][]{{CellState.WATER}}, 0));
    setUpPane();
  }


  private void setUpPane(){
    for(ShipIndicatorView view : shipViews) {
      shipIndicatorsBox.getChildren().add(view.getBoardPane());
    }
    shipPane.setContent(shipIndicatorsBox);
    shipPane.setPrefSize(300, 300);
    shipPane.setId("shipPane");
    shipPane.setExpanded(true);
  }

  public void updateShownPieces(List<Collection<Coordinate>> relativeCoords) {
    // Change ship indicator image
    shipIndicatorsBox.getChildren().clear();

    for(Collection<Coordinate> coords : relativeCoords) {
      shipIndicatorsBox.getChildren().add(new ShipIndicatorView(myShipSize, getArrayRepresentation(coords), 0).getBoardPane());
    }
  }

  public void showListCompletion(){
    shipPane.setContent(new Label("All ships placed."));
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

  public TitledPane getShipPane(){
    return shipPane;
  }

}

package oogasalad.view.panes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;
import oogasalad.view.maker.BoxMaker;
import oogasalad.view.ShipIndicatorView;
import oogasalad.view.maker.LabelMaker;

// need to hold the ship list as well as create the ships to display

public class SetPiecePane extends TitledPane {

  private List<ShipIndicatorView> shipViews;
  private VBox shipIndicatorsBox;
  private boolean lastPiecePlaced = false;
  private double myShipSize;

  public SetPiecePane(double size) {
    myShipSize = size;
    shipViews = new ArrayList<>();
    shipIndicatorsBox = BoxMaker.makeVBox("shipBox", 20, Pos.CENTER);
//    shipViews.add(new ShipIndicatorView(myShipSize, new CellState[][]{{CellState.WATER}}, 0));
    setUpPane();
  }



  private void setUpPane() {
//    for(ShipIndicatorView view : shipViews) {
//      shipIndicatorsBox.getChildren().add(view.getBoardPane());
//    }
    setContent(shipIndicatorsBox);
    setPrefWidth(300);
    setId("shipPane");
    setExpanded(true);
  }

  public void updateShownPieces(Collection<Collection<Coordinate>> relativeCoords) {
    // Change ship indicator image
    shipIndicatorsBox.getChildren().clear();
    for (Collection<Coordinate> coords : relativeCoords) {
      shipIndicatorsBox.getChildren()
          .add(new ShipIndicatorView(myShipSize, getArrayRepresentation(coords), 0).getBoardPane());
    }
    setContent(shipIndicatorsBox);
  }

  public void showListCompletion() {
    shipIndicatorsBox.getChildren().clear();
    Label shipsPlaced = LabelMaker.makeLabel("All ships placed. Press confirm to move on.", "all-ships-placed-label");
    shipsPlaced.setMaxWidth(200);
    shipsPlaced.setWrapText(true);
    shipIndicatorsBox.getChildren().add(shipsPlaced);
  }

  private CellState[][] getArrayRepresentation(Collection<Coordinate> relativeCoords) {
    int numRows = 0;
    int numCols = 0;

    for (Coordinate c : relativeCoords) {
      numRows = Math.max(numRows, c.getRow() + 1);
      numCols = Math.max(numCols, c.getColumn() + 1);
    }

    CellState[][] arrayRepresentation = new CellState[numRows][numCols];

    for (int i = 0; i < arrayRepresentation.length; i++) {
      for (int j = 0; j < arrayRepresentation[0].length; j++) {
        arrayRepresentation[i][j] = CellState.NOT_DEFINED;
      }
    }

    for (Coordinate c : relativeCoords) {
      arrayRepresentation[c.getRow()][c.getColumn()] = CellState.SHIP_HEALTHY;
    }

    return arrayRepresentation;
  }


}

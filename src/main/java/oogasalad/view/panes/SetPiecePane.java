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
import oogasalad.view.maker.BoxMaker;
import oogasalad.view.ShipIndicatorView;
import oogasalad.view.maker.LabelMaker;

// need to hold the ship list as well as create the ships to display

/**
 * This class represents a TitledPane that shows visual representations of ships on the side of
 * the main screen. These ships may represent the opponent's remaining ships, the current ship
 * being placed in setup phase, etc.
 *
 * @author Edison Ooi, Eric Xie
 */
public class SetPiecePane extends TitledPane {

  private static final String INDICATOR_ID = "shipBox";
  private static final double INDICATOR_SPACING = 20;
  private static final double PANE_WIDTH = 300;
  private static final String PANE_ID = "shipPane";
  private static final String LABEL_TEXT = "All ships placed. Press confirm to move on.";
  private static final String LABEL_ID = "all-ships-placed-label";

  // The visual representations of all ships to be shown in this pane
  private List<ShipIndicatorView> shipViews;
  // Holds all ShipIndicatorViews to be shown
  private VBox shipIndicatorsBox;
  // Indicates whether this player has placed all of their ships
  private boolean lastPiecePlaced = false;
  private double myShipSize;

  /**
   * Class constructor.
   * @param size
   */
  public SetPiecePane(double size) {
    myShipSize = size;
    shipViews = new ArrayList<>();
    shipIndicatorsBox = BoxMaker.makeVBox(INDICATOR_ID, INDICATOR_SPACING, Pos.CENTER);
//    shipViews.add(new ShipIndicatorView(myShipSize, new CellState[][]{{CellState.WATER}}, 0));
    setUpPane();
  }


  // Initializes key attributes of this TitledPane
  private void setUpPane() {
//    for(ShipIndicatorView view : shipViews) {
//      shipIndicatorsBox.getChildren().add(view.getBoardPane());
//    }
    setContent(shipIndicatorsBox);
    setPrefWidth(PANE_WIDTH);
    setId(PANE_ID);
    setExpanded(true);
  }

  /**
   * Updates the Pieces shown to reflect the input relativeCoords of the Pieces to be shown
   *
   * @param relativeCoords Coordinates of every Piece to be shown in this pane
   */
  public void updateShownPieces(Collection<Collection<Coordinate>> relativeCoords) {
    // Change ship indicator image
    shipIndicatorsBox.getChildren().clear();
    for (Collection<Coordinate> coords : relativeCoords) {
      shipIndicatorsBox.getChildren()
          .add(new ShipIndicatorView(myShipSize, getArrayRepresentation(coords), 0).getBoardPane());
    }
    setContent(shipIndicatorsBox);
  }

  /**
   * Shows a message that indicates that there are no more ships to show or place.
   */
  public void showListCompletion() {
    shipIndicatorsBox.getChildren().clear();
    Label shipsPlaced = LabelMaker.makeLabel(LABEL_TEXT, LABEL_ID);
    shipsPlaced.setMaxWidth(200);
    shipsPlaced.setWrapText(true);
    shipIndicatorsBox.getChildren().add(shipsPlaced);
  }

  // Converts a Collection of Coordinates to a rectangular array representation of their relative
  // locations
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

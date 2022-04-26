package oogasalad.model.utilities;

import oogasalad.model.utilities.tiles.enums.CellState;

/**
 * MarkerBoard class is the backend model class used to keep track of shots each player have made against their opponent
 * @author Brandon Bae
 */
public class MarkerBoard {

  private CellState[][] myMarkerArray;


  /**
   * Constructor for MarkerBoard Class
   * Assumptions:
   * - Expects only water or notdefined cell states
   * @param cellStates 2d array of CellStates that represents the initial config of the markerboard
   */
  public MarkerBoard(CellState[][] cellStates) {
    myMarkerArray = new CellState[cellStates.length][cellStates[0].length];
    for(int i = 0 ; i< myMarkerArray.length; i++) {
      for(int j = 0; j< myMarkerArray[0].length; j++) {
        myMarkerArray[i][j] = cellStates[i][j];
      }
    }
  }

  /**
   * Places the specified cellstate marker at the coordinate
   * @param coord coordinate to place marker at
   * @param marker cellstate representing result of the hit
   */
  public void placeMarker(Coordinate coord, CellState marker) {
    myMarkerArray[coord.getRow()][coord.getColumn()] = marker;
  }

  /**
   * Make Copy of current MarkerBoard
   * @return copy of current markerboard
   */
  public MarkerBoard copyOf() {
    return new MarkerBoard(myMarkerArray);
  }

  /**
   * returns the 2d cellState array that makes up the marker board
   * @return 2d cellState array myMarkerArray
   */
  public CellState[][] getBoard() {
    return myMarkerArray;
  }

}

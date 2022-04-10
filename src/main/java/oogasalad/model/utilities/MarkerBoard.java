package oogasalad.model.utilities;

import oogasalad.model.utilities.tiles.enums.CellState;

public class MarkerBoard {

  private CellState[][] myMarkerArray;

  public MarkerBoard(CellState[][] cellStates) {
    myMarkerArray = cellStates;
  }

  public void placeMarker(Coordinate coord, CellState marker) {
    myMarkerArray[coord.getRow()][coord.getColumn()] = marker;
  }

  public boolean canPlaceAt(Coordinate coord) {
    CellState coordState = myMarkerArray[coord.getRow()][coord.getColumn()];
    return coordState != CellState.WATER_HIT && coordState != CellState.ISLAND_SUNK && coordState != CellState.SHIP_SUNKEN;
  }

  public MarkerBoard copyOf() {
    return new MarkerBoard(myMarkerArray);
  }

  public CellState[][] getMarkerBoard() {
    CellState[][] markerArrayCopy = new CellState[myMarkerArray.length][myMarkerArray[0].length];
    for(int i = 0 ; i< myMarkerArray.length; i++) {
      for(int j = 0; j< myMarkerArray[0].length; j++) {
        markerArrayCopy[i][j] = myMarkerArray[i][j];
      }
    }
    return markerArrayCopy;
  }
}

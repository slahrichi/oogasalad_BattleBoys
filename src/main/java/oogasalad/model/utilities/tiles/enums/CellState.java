package oogasalad.model.utilities.tiles.enums;

public enum CellState {
  NOT_DEFINED, WATER, WATER_HIT, SHIP_HEALTHY, SHIP_DAMAGED, SHIP_SUNKEN, SHIP_HOVER, ISLAND_HEALTHY, ISLAND_DAMAGED, ISLAND_SUNK, SCANNED;

  public static CellState of(int code) {
    if(code >= CellState.values().length) {
      throw new IllegalArgumentException("No CellState defined for code " + code);
    }
    return CellState.values()[code];
  }
}

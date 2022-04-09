package oogasalad.model.utilities.tiles.enums;

public enum CellState {
  NOT_DEFINED, WATER, SHIP_HEALTHY, SHIP_DAMAGED, SHIP_SUNKEN, ISLAND_HEALTHY, ISLAND_DAMAGED, ISLAND_SUNK;

  public static CellState of(int code) {
    if(code >= CellState.values().length) {
      throw new IllegalArgumentException("No CellState defined for code " + code);
    }
    return CellState.values()[code];
  }
}
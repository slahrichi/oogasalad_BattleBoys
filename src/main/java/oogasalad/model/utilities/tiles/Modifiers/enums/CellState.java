package oogasalad.model.utilities.tiles.Modifiers.enums;

public enum CellState {
  NOT_DEFINED, SCANNED, WATER, WATER_HIT, SHIP_HEALTHY, SHIP_DAMAGED, SHIP_SUNKEN, ISLAND_HEALTHY, ISLAND_DAMAGED, ISLAND_SUNK;

  public static CellState of(int code) {
    if(code >= CellState.values().length) {
      throw new IllegalArgumentException("No CellState defined for code " + code);
    }
    return CellState.values()[code];
  }
}

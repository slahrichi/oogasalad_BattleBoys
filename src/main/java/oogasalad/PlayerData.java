package oogasalad;

import java.util.Arrays;
import java.util.List;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.Usables.Weapons.Weapon;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;

public record PlayerData(List<String> players, List<Piece> pieces, CellState[][] board, List<String> decisionEngines,
                         List<Weapon> weapons, List<IslandCell> island) {

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null) return false;
    if(!(o instanceof PlayerData)) return false;
    PlayerData other = (PlayerData) o;
    if(!this.players.equals(other.players)) return false;
    if(!this.pieces.equals(other.pieces)) return false;
    if(!Arrays.deepEquals(this.board, other.board)) return false;
    if(!this.decisionEngines.equals(other.decisionEngines)) return false;
    if(!this.weapons.equals(other.weapons)) return false;
    if(!this.island.equals(other.island)) return false;
    return true;
  }

}

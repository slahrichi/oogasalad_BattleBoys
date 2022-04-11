package oogasalad;

import java.util.Arrays;
import java.util.List;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;

public record PlayerData(List<String> players, List<Piece> pieces, CellState[][] board) {

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null) return false;
    if(!(o instanceof PlayerData)) return false;
    PlayerData other = (PlayerData) o;
    if(!this.players.equals(other.players)) return false;
    if(!this.pieces.equals(other.pieces)) return false;
    if(!Arrays.deepEquals(this.board, other.board)) return false;
    return true;
  }

}

package oogasalad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import oogasalad.model.parsing.ParseBoard;
import oogasalad.model.parsing.ParseDecisionEngines;
import oogasalad.model.parsing.ParsePieces;
import oogasalad.model.parsing.ParsePlayers;
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;

/**
 * ...summary
 * Whenever you add more items to this record, change the make and equals method as well to incorporate the new element
 */
public record PlayerData(List<String> players, List<Piece> pieces, CellState[][] board, List<String> decisionEngines)  {

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null) return false;
    if(!(o instanceof PlayerData)) return false;
    PlayerData other = (PlayerData) o;
    if(!this.players.equals(other.players)) return false;
    if(!this.decisionEngines.equals(other.decisionEngines)) return false;
    if(!this.pieces.equals(other.pieces)) return false;
    if(!Arrays.deepEquals(this.board, other.board)) return false;
    return true;
  }

  public static PlayerData make(List<Object> elements) {
    if(!(elements.get(0).getClass().equals(ArrayList.class) )) {
      return null;
    }
    if(!(elements.get(1).getClass().equals(ArrayList.class) )) {
      return null;
    }
    if(!(elements.get(2).getClass().equals(ArrayList.class) )) {
      return null;
    }
    if(!(elements.get(3).getClass().equals(ArrayList.class) )) {
      return null;
    }
    return new PlayerData(
        (List<String>) elements.get(0),
        (List<Piece>) elements.get(1),
        (CellState[][]) elements.get(2),
        (List<String>) elements.get(3)
    );

  }

  public static List<ParsedElement> makeParsers() {
    List<ParsedElement> parsers;
    parsers = new ArrayList<>();
    parsers.add(new ParsePlayers());
    parsers.add(new ParsePieces());
    parsers.add(new ParseBoard());
    parsers.add(new ParseDecisionEngines());
    return parsers;
  }

  public static List<Object> getItems(PlayerData data) {
    return List.of(data.players(), data.pieces(), data.board(), data.decisionEngines());
  }



}

package oogasalad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;
import oogasalad.model.parsing.ParseBoard;
import oogasalad.model.parsing.ParseCellColors;
import oogasalad.model.parsing.ParseDecisionEngines;
import oogasalad.model.parsing.ParsePieces;
import oogasalad.model.parsing.ParsePlayers;
import oogasalad.model.parsing.ParsePowerUps;
import oogasalad.model.parsing.ParseSpecialIslands;
import oogasalad.model.parsing.ParseWeapons;
import oogasalad.model.parsing.ParseWinConditions;
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.weapons.Weapon;
import oogasalad.model.utilities.winconditions.WinCondition;

/**
 * This class contains important information regarding the construction of players.
 * This class specifically does NOT adhere to the open/closed principle.
 * In short, to make Parser adhere to open/closed, this class cannot adhere due to limitations of Java.
 * See project writeup (Matt/Saad) for longer explanation.
 *
 * IMPORTANT: guidelines for editing this class
 * Whenever you add more items to this record, update all static methods to incorporate the new item.
 * Also implement a new class that extends ParsedElement that parses/saves the appropriate item.
 *
 * Author: Matt Knox
 */
public record ParserData(List<String> players,
                         List<Piece> pieces,
                         CellState[][] board,
                         List<String> decisionEngines,
                         List<WinCondition> winConditions,
                         Map<CellState, Color> cellStateColorMap,
                         List<Weapon> weapons){
  //TODO: implement these four parsers
  //                         List<SpecialIslands> specialIslands,
  //                         List<PowerUps> powerUps)  {
  //Map<String,Integer> playerInventory,
  //Collection<Usable> allUsables;

  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(o == null) return false;
    if(!(o instanceof ParserData)) return false;
    ParserData other = (ParserData) o;
    if(!this.players.equals(other.players)) return false;
    if(!this.pieces.equals(other.pieces)) return false;
    if(!Arrays.deepEquals(this.board, other.board)) return false;
    if(!this.decisionEngines.equals(other.decisionEngines)) return false;
    if(!this.cellStateColorMap.equals(other.cellStateColorMap)) return false;
    if(!this.weapons.equals(other.weapons)) return false;
//    if(!this.specialIslands.equals(other.specialIslands)) return false;
//    if(!this.powerUps.equals(other.powerUps)) return false;
    return true;
  }

  public static ParserData make(List<Object> elements) throws ParserException {
    if(!(elements.get(0).getClass().equals(ArrayList.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 0));
    }
    if(!(elements.get(1).getClass().equals(ArrayList.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 1));
    }
    if(!(elements.get(2).getClass().equals(CellState[][].class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 2));
    }
    if(!(elements.get(3).getClass().equals(ArrayList.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 3));
    }
    if(!(elements.get(4).getClass().equals(ArrayList.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 4));
    }
    if(!(elements.get(5).getClass().equals(Map.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 5));
    }
    if(!(elements.get(6).getClass().equals(ArrayList.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 6));
    }

    return new ParserData(
        (List<String>) elements.get(0),
        (List<Piece>) elements.get(1),
        (CellState[][]) elements.get(2),
        (List<String>) elements.get(3),
        (List<WinCondition>) elements.get(4),
        (Map<CellState, Color>) elements.get(5),
        (List<Weapon>) elements.get(6)
        //(List<SpecialIsland>) elements.get(7),
        //(List<PowerUp>) elements.get(8)
    );

  }

  public static List<ParsedElement> makeParsers() {
    return List.of(
        new ParsePlayers(),
        new ParsePieces(),
        new ParseBoard(),
        new ParseDecisionEngines(),
        new ParseWinConditions(),
        new ParseCellColors(),
        new ParseWeapons());
//    new ParseSpecialIslands(),
//        new ParsePowerUps()
  }

  public static List<Object> getItems(ParserData data) {
    return List.of(
        data.players(),
        data.pieces(),
        data.board(),
        data.decisionEngines(),
        data.winConditions(),
        data.cellStateColorMap(),
        data.weapons());
    // data.specialIslands(),
    // data.powerUps(),
  }



}


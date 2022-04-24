package oogasalad.model.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;
import oogasalad.model.parsing.parsers.ParseAllUsables;
import oogasalad.model.parsing.parsers.ParseBoard;
import oogasalad.model.parsing.parsers.ParseCellColors;
import oogasalad.model.parsing.parsers.ParseDecisionEngines;
import oogasalad.model.parsing.parsers.ParseGold;
import oogasalad.model.parsing.parsers.ParsePieces;
import oogasalad.model.parsing.parsers.ParsePlayerInventory;
import oogasalad.model.parsing.parsers.ParsePlayers;
import oogasalad.model.parsing.parsers.ParsePowerUps;
import oogasalad.model.parsing.parsers.ParseShipMovementRate;
import oogasalad.model.parsing.parsers.ParseShotsPerTurn;
import oogasalad.model.parsing.parsers.ParseSpecialIslands;
import oogasalad.model.parsing.parsers.ParseWeapons;
import oogasalad.model.parsing.parsers.ParseWinConditions;
import oogasalad.model.utilities.Item;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
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
                         List<Weapon> weapons,
                         List<IslandCell> specialIslands,
                         List<Item> powerUps,
                         Map<String,Integer> playerInventory,
                         List<Usable> allUsables,
                         Integer shotsPerTurn,
                         Integer shipMovementRate,
                         Integer gold){

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
    if(!this.specialIslands.equals(other.specialIslands)) return false;
    if(!this.powerUps.equals(other.powerUps)) return false;
    if(!this.playerInventory.equals(other.playerInventory)) return false;
    if(!this.allUsables.equals(other.allUsables)) return false;
    if(!this.shotsPerTurn.equals(other.shotsPerTurn)) return false;
    if(!this.shipMovementRate.equals(other.shipMovementRate)) return false;
    if(!this.gold.equals(other.gold)) return false;
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
    if(!(elements.get(7).getClass().equals(ArrayList.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 7));
    }
    if(!(elements.get(8).getClass().equals(ArrayList.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 8));
    }
    if(!(elements.get(9).getClass().equals(Map.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 9));
    }
    if(!(elements.get(10).getClass().equals(List.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 10));
    }

    if(!(elements.get(11).getClass().equals(Integer.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 11));
    }
    if(!(elements.get(12).getClass().equals(Integer.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 12));
    }
    if(!(elements.get(13).getClass().equals(Integer.class))) {
      throw new ParserException(String.format("Element %d of elements is not of valid type", 12));
    }

    return new ParserData(
        (List<String>) elements.get(0),
        (List<Piece>) elements.get(1),
        (CellState[][]) elements.get(2),
        (List<String>) elements.get(3),
        (List<WinCondition>) elements.get(4),
        (Map<CellState, Color>) elements.get(5),
        (List<Weapon>) elements.get(6),
        (List<IslandCell>) elements.get(7),
        (List<Item>) elements.get(8),
        (Map<String, Integer>) elements.get(9),
        (List<Usable>) elements.get(10),
        (Integer) elements.get(11),
        (Integer) elements.get(12),
        (Integer) elements.get(13)
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
        new ParseWeapons(),
        new ParseSpecialIslands(),
        new ParsePowerUps(),
        new ParsePlayerInventory(),
        new ParseAllUsables(),
        new ParseShotsPerTurn(),
        new ParseShipMovementRate(),
        new ParseGold());
  }

  public static List<Object> getItems(ParserData data) {
    return List.of(
        data.players(),
        data.pieces(),
        data.board(),
        data.decisionEngines(),
        data.winConditions(),
        data.cellStateColorMap(),
        data.weapons(),
        data.specialIslands(),
        data.powerUps(),
        data.playerInventory(),
        data.allUsables(),
        data.shotsPerTurn(),
        data.shipMovementRate(),
        data.gold());
  }
}


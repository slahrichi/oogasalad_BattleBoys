package oogasalad.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.model.parsing.ParserData;
import oogasalad.model.parsing.Parser;
import oogasalad.model.parsing.ParserException;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.EasyDecisionEngine;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Comprehensive testing for PlayerFactory
 *
 * @author Matthew Giglio
 */

public class PlayerFactoryTest {

  private CellState[][] board;
  private List<String> playerTypes;
  private List<String> difficulties;

  @BeforeEach
  void setup() {
    Parser parser = new Parser();
    ParserData parserData = null;
    try {
      parserData = parser.parse("src/main/resources/ExampleDataFile.properties");
    } catch (ParserException e) {
      e.printStackTrace();
    }
    playerTypes = parserData.players();
    board = parserData.board();
    difficulties = new ArrayList<>(parserData.decisionEngines());
  }

  @Test
  void testBasicPlayerFactory() {
    PlayerFactoryRecord pfr = PlayerFactory.initializePlayers(board, playerTypes, difficulties);
    List<Player> playerList = pfr.playerList();
    assertEquals(playerList.size(), 3);
    Map<Player, DecisionEngine> map = pfr.engineMap();
    assertEquals(map.size(), 1);
    List<DecisionEngine> list = new ArrayList<>(map.values());
    assertEquals(list.get(0).getClass(), EasyDecisionEngine.class);

  }

  @Test
  void testInvalidPlayerType() {
    playerTypes.add("Playyer");
    assertThrows(NullPointerException.class, () -> PlayerFactory.initializePlayers(board, playerTypes, difficulties).
        playerList());
  }

  @Test
  void testInvalidPlayerTyle() {
    difficulties.set(1, "Impahssible");
    assertThrows(NullPointerException.class, () -> PlayerFactory.initializePlayers(board, playerTypes, difficulties).
        playerList());
  }



}

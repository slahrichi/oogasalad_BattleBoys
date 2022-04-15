package oogasalad.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.PlayerData;
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

public class PlayerFactoryTest {

  private CellState[][] board;
  private List<String> playerTypes;
  private List<String> difficulties;

  @BeforeEach
  void setup() {
    Parser parser = new Parser();
    PlayerData playerData = null;
    try {
      playerData = parser.parse("src/main/resources/ExampleDataFile.properties");
    } catch (ParserException e) {
      e.printStackTrace();
    }
    playerTypes = playerData.players();
    board = playerData.board();
    difficulties = new ArrayList<>(playerData.decisionEngines());
  }

  @Test
  void testBasicPlayerFactory() {
    PlayerFactoryRecord pfr = PlayerFactory.initializePlayers(board, playerTypes, difficulties);
    List<Player> playerList = pfr.playerList();
    assertEquals(playerList.size(), 3);
    Map<Integer, DecisionEngine> map = pfr.engineMap();
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

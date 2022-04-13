package oogasalad.controller;

import java.util.List;
import oogasalad.FilePicker;
import oogasalad.Parser;
import oogasalad.PlayerData;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerFactoryTest {

  private PlayerFactory pf;
  private List<String> playerTypes;

  @BeforeEach
  void setup() {
    Parser parser = new Parser();
    PlayerData playerData = parser.parse("src/main/resources/ExampleDataFile.properties");
    playerTypes = playerData.players();
    CellState[][] notSoDummyBoard = playerData.board();
    pf = new PlayerFactory(notSoDummyBoard);
  }

  @Test
  void testBasicPlayerFactory() {
    List<Player> playerList = pf.createPlayerList(playerTypes);
    assertEquals(playerList.size(), 3);
  }

  @Test
  void testInvalidFileData() {
    playerTypes.add("Playyer");
    assertThrows(NullPointerException.class, () -> pf.createPlayerList(playerTypes));
  }

}

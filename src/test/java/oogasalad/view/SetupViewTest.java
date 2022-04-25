package oogasalad.view;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import oogasalad.controller.GameData;
import oogasalad.controller.GameSetup;
import oogasalad.controller.PlayerFactory;
import oogasalad.controller.PlayerFactoryRecord;
import oogasalad.model.players.DecisionEngine;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.items.Item;
import oogasalad.model.utilities.usables.weapons.Weapon;
import oogasalad.model.utilities.winconditions.LoseXShipsLossCondition;
import oogasalad.model.utilities.winconditions.WinCondition;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.panels.TitlePanel;
import util.DukeApplicationTest;
import org.junit.jupiter.api.Test;

/**
 * @author Minjun Kwak
 */
public class SetupViewTest extends DukeApplicationTest {

  private ResourceBundle myResources = ResourceBundle.getBundle("/languages/English");
  private Map<CellState, Color> dummyColorMap;

  @Override
  public void start(Stage stage) {
    dummyColorMap = new HashMap<>();
    dummyColorMap.put(CellState.NOT_DEFINED, Color.TRANSPARENT);
    dummyColorMap.put(CellState.WATER, Color.BLUE);
    dummyColorMap.put(CellState.WATER_HIT, Color.WHITE);
    dummyColorMap.put(CellState.SHIP_HEALTHY, Color.BLACK);
    dummyColorMap.put(CellState.SHIP_DAMAGED, Color.ORANGE);
    dummyColorMap.put(CellState.SHIP_SUNKEN, Color.RED);
    dummyColorMap.put(CellState.SHIP_HOVER, Color.GRAY);
    dummyColorMap.put(CellState.SCANNED, Color.PINK);
    dummyColorMap.put(CellState.ISLAND_HEALTHY, Color.YELLOW);
    dummyColorMap.put(CellState.ISLAND_DAMAGED, Color.GREEN);
    dummyColorMap.put(CellState.ISLAND_SUNK, Color.PURPLE);
    CellState[][] board = new CellState[][]{
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER},
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER},
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER},
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER}};
    List<String> stringPlayers = new ArrayList<>();
    stringPlayers.add("HumanPlayer");
    stringPlayers.add("HumanPlayer");
    List<String> decisionEngines = new ArrayList<>();
    decisionEngines.add("None");
    decisionEngines.add("None");
    List<Piece> pieces = new ArrayList<>();
    List<ShipCell> ships1 = new ArrayList<>();
    List<ShipCell> ships2 = new ArrayList<>();
    ships1.add(new ShipCell(1, new Coordinate(0, 0), 10, "0"));
    ships1.add(new ShipCell(1, new Coordinate(0, 1), 10, "1"));
    ships2.add(new ShipCell(1, new Coordinate(0, 0), 10, "0"));
    ships2.add(new ShipCell(1, new Coordinate(1, 0), 10, "1"));
    List<Coordinate> coords1 = new ArrayList<>();
    List<Coordinate> coords2 = new ArrayList<>();
    coords1.add(new Coordinate(0, 0));
    coords1.add(new Coordinate(0, 1));
    coords2.add(new Coordinate(0, 0));
    coords2.add(new Coordinate(1, 0));
    Piece piece1 = new StaticPiece(ships1, coords1, "0");
    Piece piece2 = new StaticPiece(ships2, coords2, "1");
    pieces.add(piece1);
    pieces.add(piece2);
    PlayerFactoryRecord record = PlayerFactory.initializePlayers(board, stringPlayers, new HashMap<>(), 100, null,
        new ArrayList<>());
    List<Player> players = record.playerList();
    List<WinCondition> winConditions = new ArrayList<>();
    winConditions.add(new LoseXShipsLossCondition(2));

    GameData data = new GameData(players, pieces, board, new HashMap<>(), winConditions, dummyColorMap, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(), 1, 0, 100);
    GameSetup setup = new GameSetup(data, myResources);
    stage.setScene(setup.createScene());
    stage.show();
  }

  @Test
  void createSetUp() {
    sleep(2000);
    clickOn(lookup("#pass-computer-message-button").query());
    TitlePanel title = lookup("#setup-view-pane #setup-title").query();
    assertEquals(true, lookup("#setup-view-pane #setup-center-box #confirm-button").query().isDisabled());
    assertEquals("Player 1: Set Up Your Ships", title.getTitle());
  }

  @Test
  void spotClicked() {
    sleep(2000);
    clickOn(lookup("#pass-computer-message-button").query());
    clickOn(lookup("#ok-button").query());
    Polygon cell2_2 = lookup("#setup-view-pane #setup-center-box #boardBox #board-view #board-view-base #cell-view-2-2-0").query();
    clickOn(cell2_2);
    assertEquals(Color.BLACK, cell2_2.getFill());
  }

  @Test
  void removePiece() {
    sleep(2000);
    clickOn(lookup("#pass-computer-message-button").query());
    clickOn(lookup("#ok-button").query());
    Polygon cell2_2 = lookup("#setup-view-pane #setup-center-box #boardBox #board-view #board-view-base #cell-view-2-2-0").query();
    clickOn(cell2_2);
    assertEquals(Color.BLACK, cell2_2.getFill());
    clickOn(lookup("#remove-last-button").query());
    assertEquals(Color.BLUE, cell2_2.getFill());
  }
}
package oogasalad.view;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import oogasalad.GameData;
import oogasalad.controller.GameSetup;
import oogasalad.controller.PlayerFactory;
import oogasalad.controller.PlayerFactoryRecord;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.winconditions.LoseXShipsLossCondition;
import oogasalad.model.utilities.winconditions.WinCondition;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.panels.TitlePanel;
import util.DukeApplicationTest;
import org.junit.jupiter.api.Test;

public class SetupViewTest extends DukeApplicationTest {

  private Button confirm;
  private TitlePanel title;
  private Polygon cell2_2;
  private ResourceBundle myResources = ResourceBundle.getBundle("/languages/English");

  @Override
  public void start(Stage stage) {
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
    PlayerFactoryRecord record = PlayerFactory.initializePlayers(board, stringPlayers, null);
    List<Player> players = record.playerList();
    List<WinCondition> winConditions = new ArrayList<>();
    winConditions.add(new LoseXShipsLossCondition(2));
    GameData data = new GameData(players, board, pieces, winConditions, null);
    GameSetup setup = new GameSetup(data, myResources);
    stage.setScene(setup.createScene());
    stage.show();

    confirm = lookup("#setup-view-pane #setup-center-box #confirm-button").query();
    title = lookup("#setup-view-pane #setup-title").query();
    cell2_2 = lookup("#setup-view-pane #setup-center-box #boardBox #board-view #board-view-base #cell-view-2-2-0").query();
  }

  @Test
  void createSetUp() {
    assertEquals(true, confirm.isDisabled());
    assertEquals("Player 1: Set Up Your Ships", title.getTitle());
  }

  @Test
  void spotClicked() {
    clickOn(cell2_2);
    assertEquals(Color.BLACK, cell2_2.getFill());
  }
}
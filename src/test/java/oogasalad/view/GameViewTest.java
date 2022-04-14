package oogasalad.view;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import oogasalad.GameData;
import oogasalad.controller.GameManager;
import oogasalad.model.players.AIPlayer;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.WinConditions.WinCondition;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import util.DukeApplicationTest;
import org.junit.jupiter.api.Test;

public class GameViewTest extends DukeApplicationTest {

  private int numPlayers = 3;
  private Button rightButton;
  private Polygon cell0;
  private Button shopButton;

  @Override
  public void start(Stage stage) {
    List<CellState[][]> firstPlayerBoards = new ArrayList<>();
    CellState[][] selfBoard = new CellState[][]{
        {CellState.WATER, CellState.WATER, CellState.SHIP_HEALTHY, CellState.SHIP_HEALTHY},
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.SHIP_HEALTHY},
        {CellState.SHIP_HEALTHY, CellState.WATER, CellState.WATER, CellState.WATER},
        {CellState.SHIP_HEALTHY, CellState.SHIP_HEALTHY, CellState.WATER, CellState.WATER}};
    CellState[][] markerBoards = new CellState[][]{
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER},
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER},
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER},
        {CellState.WATER, CellState.WATER, CellState.WATER, CellState.WATER}};
    firstPlayerBoards.add(selfBoard);
    for (int i = 1; i < numPlayers; i++) {
      firstPlayerBoards.add(markerBoards);
    }
    List<Piece> pieces = new ArrayList<>();
    List<ShipCell> ships = new ArrayList<>();
    ships.add(new ShipCell(1, new Coordinate(0, 0), 10, "0"));
    ships.add(new ShipCell(1, new Coordinate(0, 1), 10, "1"));
    ships.add(new ShipCell(1, new Coordinate(1, 1), 10, "2"));
    List<Coordinate> relativeCoords = new ArrayList<>();
    relativeCoords.add(new Coordinate(0, 0));
    relativeCoords.add(new Coordinate(0, 1));
    relativeCoords.add(new Coordinate(1, 1));
    pieces.add(new StaticPiece(ships, relativeCoords, "0"));

    List<ShipCell> ships2 = new ArrayList<>();
    ships2.add(new ShipCell(1, new Coordinate(0, 0), 10, "0"));
    ships2.add(new ShipCell(1, new Coordinate(0, 2), 10, "1"));
    ships2.add(new ShipCell(1, new Coordinate(0, 1), 10, "2"));
    ships2.add(new ShipCell(1, new Coordinate(1, 1), 10, "3"));
    List<Coordinate> relativeCoords2 = new ArrayList<>();
    relativeCoords2.add(new Coordinate(0, 0));
    relativeCoords2.add(new Coordinate(0, 2));
    relativeCoords2.add(new Coordinate(0, 1));
    relativeCoords2.add(new Coordinate(1, 1));
    pieces.add(new StaticPiece(ships2, relativeCoords2, "1"));

    Collection<Collection<Coordinate>> pieceCoords = new ArrayList<>();
    for (Piece piece : pieces) {
      pieceCoords.add(piece.getRelativeCoords());
    }
    GameView view = new GameView(firstPlayerBoards, pieceCoords);
    stage.setScene(view.createScene());
    stage.show();

    cell0 = lookup("#view-pane #view-center-pane #board-view #board-view-base #cell-view-0-0-0").query();
    rightButton = lookup("#view-pane #view-center-pane #board-button-box #right-button").query();
    shopButton = lookup("#view-pane #configBox #view-shop").query();
  }

  @Test
  public void testSwitchBoard() {
    rightClickOn(rightButton);
    Polygon cell1 = lookup("#view-pane #view-center-pane #board-view #board-view-base #cell-view-0-0-1").query();
    clickOn(cell1);
  }

  @Test
  public void testOpenShop() {
    rightClickOn(shopButton);
  }
}

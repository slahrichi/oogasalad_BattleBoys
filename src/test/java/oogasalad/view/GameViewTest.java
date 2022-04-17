package oogasalad.view;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;
import util.DukeApplicationTest;
import org.junit.jupiter.api.Test;

public class GameViewTest extends DukeApplicationTest {

  private int numPlayers = 3;
  private Button rightButton;
  private Button leftButton;
  private Label currentBoardLabel;
  private GameView view;

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
    view = new GameView(firstPlayerBoards, pieceCoords);
    stage.setScene(view.createScene());
    stage.show();

    rightButton = lookup("#view-pane #view-center-pane #board-button-box #right-button").query();
    leftButton = lookup("#view-pane #view-center-pane #board-button-box #left-button").query();
    currentBoardLabel = lookup("#view-pane #view-center-pane #currentBoardLabel").query();
  }

  @Test
  public void testSwitchBoard() {
    assertEquals(view.getCurrentBoardIndex(), 0);
    clickOn(rightButton);
    Polygon cell1 = lookup("#view-pane #view-center-pane #board-view #board-view-base #cell-view-0-0-1").query();
    clickOn(cell1);
    assertEquals(view.getCurrentBoardIndex(), 1);
    clickOn(leftButton);
  }

  @Test
  public void testLabelText() {
    assertEquals(currentBoardLabel.getText(), "Your Board");
    clickOn(rightButton);
    assertEquals(currentBoardLabel.getText(), "Your Shots Against Player 2");
  }
}

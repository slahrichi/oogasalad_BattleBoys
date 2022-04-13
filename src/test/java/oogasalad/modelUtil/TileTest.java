package oogasalad.modelUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.Modifiers.GoldAdder;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TileTest {
  Piece piece;
  ShipCell cell;
  ShipCell c2 ;

  @BeforeEach
  void setup() {
    cell  = new ShipCell(2, new Coordinate(0,0), 100, (Piece) null);

  }

  @Test
  void testBasic() {
    assertEquals(2,cell.getHealth());
    assertEquals(CellState.SHIP_HEALTHY, cell.getCellState());
    cell.hit();
    assertEquals(1,cell.getHealth());
    assertEquals(CellState.SHIP_DAMAGED, cell.getCellState());
    cell.hit();
    assertEquals(0,cell.getHealth());
    assertEquals(CellState.SHIP_SUNKEN, cell.getCellState());
    assertEquals(null, cell.getAssignedShip());
    cell.updateCoordinates(5,5);
    assertEquals(cell.getCoordinates(), new Coordinate(5,5));
  }

  @Test
  void testPlacing(){
    cell.placeAt(new Coordinate(2,2));
    assertEquals(cell.getCoordinates(), new Coordinate(2,2));
  }

  @Test
  void testCarry(){
    assertEquals(false, cell.canCarryObject());
  }

  @Test
  void testGoldAdder(){
    ArrayList<Modifiers> mods = (ArrayList<Modifiers>) cell.update();
    HumanPlayer p1 = new HumanPlayer(null, 0, null);
    HumanPlayer p2 = new HumanPlayer(null, 0, null);
    HumanPlayer[] players = {p1,p2};
    if(mods!= null && !mods.isEmpty()) {
      for (Modifiers m : mods) {
        m.modifierFunction().accept(players);
      }
    }
    assertEquals( 0, p1.getMyCurrency());
    cell.addModifier(new GoldAdder(200));
    cell.hit();
    cell.hit();
    mods = (ArrayList<Modifiers>) cell.update();
    for(Modifiers m: mods){
      m.modifierFunction().accept(players);
    }
    assertEquals(300, p1.getMyCurrency());
  }

}

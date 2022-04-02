package oogasalad.modelUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oogasalad.model.players.AIPlayer;
import oogasalad.model.players.HumanPlayer;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class PieceTest {

  Piece piece;


  @BeforeEach
  void setup() {
    List<ShipCell> shipShape = new ArrayList<>() ;
    ShipCell c1= new ShipCell(0,0,null,0);
    ShipCell c2= new ShipCell(0,1,null,0);
    shipShape.add(c1);
    shipShape.add(c2);
    piece = new StaticPiece(shipShape);
  }

  @Test
  void testBasicDamage() {
    piece.registerDamage(new Coordinate(0,1));
    assertEquals("Damaged",  piece.getStatus());
  }

  @Test
  void testSinkDamage() {
    piece.registerDamage(new Coordinate(0,1));
    piece.registerDamage(new Coordinate(0,0));
    assertEquals("Dead",  piece.getStatus());
  }


}

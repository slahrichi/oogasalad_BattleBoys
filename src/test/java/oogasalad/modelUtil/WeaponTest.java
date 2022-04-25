package oogasalad.modelUtil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.CellInterface;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.items.CircleHeal;
import oogasalad.model.utilities.usables.items.ShipHeal;
import oogasalad.model.utilities.usables.weapons.BasicShot;
import oogasalad.model.utilities.usables.weapons.BlastZoneShot;
import oogasalad.model.utilities.usables.weapons.ClusterShot;
import oogasalad.model.utilities.usables.weapons.EmpoweredShot;
import oogasalad.model.utilities.usables.weapons.HomingShot;
import oogasalad.model.utilities.usables.weapons.LineShot;
import oogasalad.model.utilities.usables.weapons.MoltovShot;
import oogasalad.model.utilities.usables.weapons.SonarShot;
import oogasalad.model.utilities.usables.weapons.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WeaponTest {

  Board myBoard;
  Piece piece1;
  Piece piece2;
  @BeforeEach
  void setup(){
    CellState[][] states = {{CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER},
        {CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER,CellState.WATER}};
    myBoard = new Board(states);

    List<ShipCell> celllist1  = new ArrayList<>();
    celllist1.add(new ShipCell(1,new Coordinate(0,0), 100, "ship1"));
    celllist1.add(new ShipCell(4,new Coordinate(0,1), 100, "ship1"));
    celllist1.add(new ShipCell(1,new Coordinate(1,0), 100, "ship1"));
    celllist1.add(new ShipCell(1,new Coordinate(1,1), 100, "ship1"));

    List<Coordinate> coords1 = new ArrayList<>();
    coords1.add(new Coordinate(0,0));
    coords1.add(new Coordinate(0,1));
    coords1.add(new Coordinate(1,0));
    coords1.add(new Coordinate(1,1));


    List<ShipCell> celllist2  = new ArrayList<>();
    celllist2.add(new ShipCell(1,new Coordinate(0,0), 100, "tile5"));
    celllist2.add(new ShipCell(2,new Coordinate(1,0), 100, "tile6"));
    celllist2.add(new ShipCell(5,new Coordinate(2,0), 100, "tile7"));
    celllist2.add(new ShipCell(2,new Coordinate(2,2), 100, "tile8"));
    celllist2.add(new ShipCell(1,new Coordinate(1,1), 100, "tile9"));
    List<Coordinate> coords2 = new ArrayList<>();
    coords2.add(new Coordinate(0,0));
    coords2.add(new Coordinate(1,0));
    coords2.add(new Coordinate(2,0));
    coords2.add(new Coordinate(2,2));
    coords2.add(new Coordinate(1,1));

    piece1 = new StaticPiece(celllist1, coords1, "ship1");
    piece2 = new StaticPiece(celllist2, coords2, "ship2");
    myBoard.placePiece(new Coordinate(0,0), piece1);
    myBoard.placePiece(new Coordinate(3,3), piece2);
  }

  @Test
  void basicShotTest(){
    Weapon weapon = new BasicShot();
    Map<Coordinate, CellState> ret = weapon.getFunction().apply(new Coordinate(0,0), myBoard);
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(0,0)));
    assertEquals(CellState.SHIP_SUNKEN, myBoard.getCell(new Coordinate(0,0)).getCellState());

    Exception exception = assertThrows(Exception.class, () ->weapon.getFunction().apply(new Coordinate(0,-1), myBoard));
    assertEquals("Coordinate Provided Out of Bounds", exception.getMessage());
  }

  @Test
  void blastZoneShotTest(){
    Weapon weapon = new BlastZoneShot("weapon", 100, 3);
    Map<Coordinate, CellState> ret = weapon.getFunction().apply(new Coordinate(1,1), myBoard);
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(0,0)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SHIP_DAMAGED, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,0)));
  }

  @Test
  void clusterShot(){
    Map<Coordinate,Integer> shots = new HashMap<>();
    shots.put(new Coordinate(0,0),1);
    shots.put(new Coordinate(-1,0),3);
    shots.put(new Coordinate(0,-1),1);
    shots.put(new Coordinate(1,0),1);
    shots.put(new Coordinate(0,1),1);

    Weapon weapon = new ClusterShot("weapon", 100, shots);
    Map<Coordinate, CellState> ret = weapon.getFunction().apply(new Coordinate(1,1), myBoard);


    assertEquals(CellState.SHIP_HEALTHY, myBoard.getCell(new Coordinate(0,0)).getCellState());
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SHIP_DAMAGED, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,0)));
  }

  @Test
  void EmpoweredShotTest(){
    Weapon weapon = new EmpoweredShot("empowered", 100, 2);
    Map<Coordinate, CellState> ret = weapon.getFunction().apply(new Coordinate(0,1), myBoard);
    assertEquals(CellState.SHIP_DAMAGED, ret.get(new Coordinate(0,1)));
    assertEquals(2, myBoard.getCell(new Coordinate(0,1)).getHealth());

    Exception exception = assertThrows(Exception.class, () ->weapon.getFunction().apply(new Coordinate(0,-1), myBoard));
    assertEquals("Coordinate does not exist in the Board", exception.getMessage());
  }

  @Test
  void HomingShotTest(){
    Weapon weapon = new HomingShot("empowered", 100, 3, 4, 1);
    Map<Coordinate, CellState> ret = weapon.getFunction().apply(new Coordinate(1,1), myBoard);
    boolean noneAreWater = true;
    for(Coordinate coord: ret.keySet()){
      if(ret.get(coord) != CellState.SHIP_SUNKEN && ret.get(coord) != CellState.SHIP_DAMAGED)
        noneAreWater = false;
    }
    assertTrue(noneAreWater);
  }
  @Test
  void LineShotTest(){
    Weapon weapon = new LineShot("line", 100, 1);
    Map<Coordinate, CellState> ret = weapon.getFunction().apply(new Coordinate(0,1), myBoard);
    assertEquals(CellState.SHIP_HEALTHY, myBoard.getCell(new Coordinate(0,0)).getCellState());
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SHIP_DAMAGED, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SHIP_HEALTHY, myBoard.getCell(new Coordinate(1,0)).getCellState());
  }
  @Test
  void MoltovShot1(){
    Weapon weapon = new MoltovShot("Moltov", 100, 1);
    Map<Coordinate, CellState> ret = weapon.getFunction().apply(new Coordinate(0,0), myBoard);
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(0,0)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SHIP_DAMAGED, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,0)));

    ret = weapon.getFunction().apply(new Coordinate(1,0), myBoard);
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(0,0)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SHIP_DAMAGED, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,0)));

    ret = weapon.getFunction().apply(new Coordinate(1,1), myBoard);
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(0,0)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SHIP_DAMAGED, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,0)));

    ret = weapon.getFunction().apply(new Coordinate(1,1), myBoard);
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(0,0)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,0)));
  }
  @Test
  void ScannerShot(){
    Weapon weapon = new SonarShot("Scan", 100, 2);
    Map<Coordinate, CellState> ret = weapon.getFunction().apply(new Coordinate(0,0), myBoard);
    assertEquals(CellState.SCANNED, ret.get(new Coordinate(0,0)));
    assertEquals(CellState.SCANNED, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SCANNED, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SCANNED, ret.get(new Coordinate(1,0)));
  }

  @Test
  void ShipHealTest(){
    Weapon weapon = new MoltovShot("Moltov", 100, 1);
    Map<Coordinate, CellState> ret = weapon.getFunction().apply(new Coordinate(0,0), myBoard);
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(0,0)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SHIP_DAMAGED, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,0)));

    Usable use = new ShipHeal("Heal", 100, 1);
    ret = use.getFunction().apply(new Coordinate(0,0), myBoard);
    assertEquals(CellState.SHIP_HEALTHY, ret.get(new Coordinate(0,0)));
    assertEquals(CellState.SHIP_HEALTHY, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SHIP_HEALTHY, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SHIP_HEALTHY, ret.get(new Coordinate(1,0)));
  }
  @Test
  void CircleHealTest(){
    Weapon weapon = new MoltovShot("Moltov", 100, 1);
    Map<Coordinate, CellState> ret = weapon.getFunction().apply(new Coordinate(0,0), myBoard);
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(0,0)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SHIP_DAMAGED, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SHIP_SUNKEN, ret.get(new Coordinate(1,0)));

    Usable use = new CircleHeal("Heal", 100, 1,1);
    ret = use.getFunction().apply(new Coordinate(1,1), myBoard);
    assertEquals(CellState.SHIP_HEALTHY, ret.get(new Coordinate(0,0)));
    assertEquals(CellState.SHIP_HEALTHY, ret.get(new Coordinate(1,1)));
    assertEquals(CellState.SHIP_HEALTHY, ret.get(new Coordinate(0,1)));
    assertEquals(CellState.SHIP_HEALTHY, ret.get(new Coordinate(1,0)));
  }


}

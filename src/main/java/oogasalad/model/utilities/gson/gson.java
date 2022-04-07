package oogasalad.model.utilities.gson;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.ShipCell;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class gson {

    public static void main(String[] args) throws IOException {
        StaticPiece ship1 = new StaticPiece("Long Piece");
        ArrayList<ShipCell> ship1Tiles = new ArrayList<>();
        ship1Tiles.add(new ShipCell(1, new Coordinate(0,0), 100, ship1));
        ship1Tiles.add(new ShipCell(1, new Coordinate(0,1), 100, ship1));
        ship1Tiles.add(new ShipCell(1, new Coordinate(0,2), 100, ship1));
        ship1Tiles.add(new ShipCell(1, new Coordinate(0,3), 100, ship1));
        ship1Tiles.add(new ShipCell(1, new Coordinate(0,4), 100, ship1));
        ship1.initCellList(ship1Tiles);

        StaticPiece ship2 = new StaticPiece("Tonk");
        ArrayList<ShipCell> ship2Tiles = new ArrayList<>();
        ship2Tiles.add(new ShipCell(4, new Coordinate(0,0), 100, ship1));
        ship2Tiles.add(new ShipCell(4, new Coordinate(0,1), 200, ship1));
        ship2Tiles.add(new ShipCell(4, new Coordinate(1,1), 100, ship1));
        ship2Tiles.add(new ShipCell(4, new Coordinate(1,0), 200, ship1));
        ship2.initCellList(ship2Tiles);

        ArrayList<Piece> ships = new ArrayList<>();
        ships.add(ship1);
        ships.add(ship2);
        String myJSONObject = new Gson().toJson(ships);

        ArrayList<Piece> newPiece = new Gson().fromJson(myJSONObject, ArrayList.class);
        System.out.println(newPiece.get(0));
        System.out.println(newPiece.get(1));
        FileWriter file = new FileWriter("data/ships.json");
        file.write(myJSONObject);
        file.close();

    }
}

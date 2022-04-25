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
        ArrayList<ShipCell> ship1Tiles = new ArrayList<>();
        ArrayList<Coordinate> ship1RelCoord = new ArrayList<>();
        ship1Tiles.add(new ShipCell(1, new Coordinate(0,0), 100, "0"));
        ship1RelCoord.add(new Coordinate(0,0));
        ship1Tiles.add(new ShipCell(1, new Coordinate(0,1), 100, "0"));
        ship1RelCoord.add(new Coordinate(0,1));
        ship1Tiles.add(new ShipCell(1, new Coordinate(0,2), 100, "0"));
        ship1RelCoord.add(new Coordinate(0,2));
        ship1Tiles.add(new ShipCell(1, new Coordinate(0,3), 100, "0"));
        ship1RelCoord.add(new Coordinate(0,3));
        ship1Tiles.add(new ShipCell(1, new Coordinate(0,4), 100, "0"));
        ship1RelCoord.add(new Coordinate(0,4));
        StaticPiece ship1 = new StaticPiece(ship1Tiles, ship1RelCoord, "0");


        ArrayList<ShipCell> ship2Tiles = new ArrayList<>();
        ArrayList<Coordinate> ship2RelCoord = new ArrayList<>();
        ship2Tiles.add(new ShipCell(4, new Coordinate(0,0), 100, "1"));
        ship2RelCoord.add(new Coordinate(0,0));
        ship2Tiles.add(new ShipCell(4, new Coordinate(0,1), 200, "1"));
        ship2RelCoord.add(new Coordinate(0,1));
        ship2Tiles.add(new ShipCell(4, new Coordinate(1,1), 100, "1"));
        ship2RelCoord.add(new Coordinate(1,1));
        ship2Tiles.add(new ShipCell(4, new Coordinate(1,0), 200, "1"));
        ship2RelCoord.add(new Coordinate(1,0));
        StaticPiece ship2 = new StaticPiece(ship2Tiles, ship2RelCoord, "1");

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

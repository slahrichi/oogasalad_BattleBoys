package oogasalad.model.utilities.usables.weapons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.view.Info;

public abstract class Weapon extends Usable {

    public Weapon(String id, int gold){
        super(id, gold);
        relativeCoordShots = new HashMap<>();
        makeWeaponFunction();
    }
    Map<Coordinate,Integer> relativeCoordShots;

    /**
     * Method that front end can call to display the action/tiles that will be affected by this weapon
     * if it were to be played.
     * @param absoluteCoord the coordinate of the tile that is being hovered at the moment
     * @param currBoard the current board that is being hovered over
     * @return List of coordinates that should be higlighted to show where the shots will land.
     */
    public List<Coordinate> getHighlightedCoords(Coordinate absoluteCoord, Board currBoard){
        List<Coordinate> highlightedCoords = new ArrayList<>();
        for(Coordinate relCoord: relativeCoordShots.keySet()){
            if(currBoard.checkBoundedCoordinate(new Coordinate(relCoord.getRow()+absoluteCoord.getRow(), relCoord.getColumn()+absoluteCoord.getColumn())))
                highlightedCoords.add(new Coordinate(relCoord.getRow()+absoluteCoord.getRow(),
                    relCoord.getColumn()+absoluteCoord.getColumn()));
            else
                break;
        }
        return highlightedCoords;
    }
    public void setRelativeCoordShots(Map<Coordinate, Integer> relCoords){
        relativeCoordShots = relCoords;
    }
    public Map<Coordinate, Integer> getRelativeCoordShots(){return relativeCoordShots;}
    protected void addRelativePosition(Coordinate coord, Integer dmg){relativeCoordShots.put(coord, dmg);}

    protected abstract void makeWeaponFunction();

    @Override
    public String getType() {
        return "Weapon";
    }

    @Override
    public BiConsumer<String, GameManager> handleUsage(){
        return (clickInfo, gm)-> {
            int id = Integer.parseInt(clickInfo.substring(clickInfo.lastIndexOf(" ") + 1));
            if(gm.getCurrentPlayer() != id) {
                gm.handleShot(clickInfo);
            }
            else
                throw new IllegalArgumentException("Please Choose an Enemy Board");
        };
    }

}

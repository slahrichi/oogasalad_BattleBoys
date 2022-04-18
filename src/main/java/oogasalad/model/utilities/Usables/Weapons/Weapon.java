package oogasalad.model.utilities.Usables.Weapons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Usables.UsableFunction;
import oogasalad.model.utilities.Usables.Usables;

public abstract class Weapon extends Usables {

    public Weapon(String id, int gold){
        super(id, gold);
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
}

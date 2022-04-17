package oogasalad.model.utilities.Weapons;

import java.util.ArrayList;
import java.util.List;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;

public abstract class Weapon {

    WeaponFunction myWeaponFunction;
    List<Coordinate> relativeCoordShots;
    String myID;

    /**
     *
     * @param coord The absolute coordinate of place on the board being hit (The reference point)
     * @param enemyBoard The board that will be hit by the weapon
     * @return
     */
    public WeaponFunction getWeaponFunction(Coordinate coord, Board enemyBoard){
        if(enemyBoard.checkBoundedCoordinate(coord)){
            return myWeaponFunction;
        }else{
            throw new NullPointerException("Coordinate Provided Out of Bounds");
        }
    }



    /**
     * Method that front end can call to display the action/tiles that will be affected by this weapon
     * if it were to be played.
     * @param absoluteCoord the coordinate of the tile that is being hovered at the moment
     * @param currBoard the current board that is being hovered over
     * @return List of coordinates that should be higlighted to show where the shots will land.
     */
    public List<Coordinate> getHighlightedCoords(Coordinate absoluteCoord, Board currBoard){
        updateRelativeCoords(currBoard);
        List<Coordinate> highlightedCoords = new ArrayList<>();
        for(Coordinate relCoord: relativeCoordShots){
            if(currBoard.checkBoundedCoordinate(new Coordinate(relCoord.getRow()+absoluteCoord.getRow(), relCoord.getColumn()+absoluteCoord.getColumn())))
                highlightedCoords.add(new Coordinate(relCoord.getRow()+absoluteCoord.getRow(),
                    relCoord.getColumn()+absoluteCoord.getColumn()));
            else
                break;
        }
        return highlightedCoords;
    }

    protected abstract void updateRelativeCoords(Board currBoard);

    protected abstract void makeWeaponFunction();

    /**
     * @return the name of the Weapon system.
     */
    public String getMyID(){
        return myID;
    }
}

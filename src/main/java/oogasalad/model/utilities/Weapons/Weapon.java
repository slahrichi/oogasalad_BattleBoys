package oogasalad.model.utilities.Weapons;

import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;

public abstract class Weapon {

    WeaponFunction myWeaponFunction;
    String myID;

    public abstract WeaponFunction getWeaponFunction();

    public String getMyID(){
        return myID;
    }


    public boolean checkCoordinates(Coordinate c, MarkerBoard markerBoard) {
        return false;
    }
}

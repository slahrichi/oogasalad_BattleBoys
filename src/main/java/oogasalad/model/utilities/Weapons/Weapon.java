package oogasalad.model.utilities.Weapons;

public abstract class Weapon {

    WeaponFunction myWeaponFunction;
    String myID;


    public abstract WeaponFunction getWeaponFunction();

    public String getMyID(){
        return myID;
    }
}

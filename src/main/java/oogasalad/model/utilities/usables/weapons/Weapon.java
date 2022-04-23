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
    }

    protected abstract void makeWeaponFunction();

    @Override
    public BiConsumer<String, GameManager> handleUsage(){
        return (clickInfo, gm)-> {
            int id = Integer.parseInt(clickInfo.substring(clickInfo.lastIndexOf(" ") + 1));
            if(gm.getCurrentPlayer() != id) {
                System.out.println("Hello");
                System.out.println(id + " " + gm.getCurrentPlayer());
                gm.handleShot(clickInfo);
            }
            else
                throw new IllegalArgumentException("Please Choose an Enemy Board");
        };
    }

}

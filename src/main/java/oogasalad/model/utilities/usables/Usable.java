package oogasalad.model.utilities.usables;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.view.Info;

public abstract class Usable {

  String myID;
  UsableFunction myFunction;
  int goldCost;

  public Usable(String ID, int gold){
    this.myID = ID;
    this.goldCost = gold;

  }

  public UsableFunction getFunction() {
      return myFunction;
  }
  public String getMyID(){return myID;}
  protected void setMyFunction(UsableFunction function){myFunction = function;}
  protected void setGoldCost(int cost){goldCost = cost;}
  protected void setMyID(String myID) {
    this.myID = myID;
  }
  public int getPrice(){return goldCost;}

  public abstract String getType();
  public abstract BiConsumer<String, GameManager> handleUsage();

}

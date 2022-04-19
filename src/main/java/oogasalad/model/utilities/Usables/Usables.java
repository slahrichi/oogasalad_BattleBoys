package oogasalad.model.utilities.Usables;

public abstract class Usables {

  String myID;
  UsableFunction myFunction;
  int goldCost;

  public Usables(String ID, int gold){
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

  public int getPrice() {
    return goldCost;
  }
}

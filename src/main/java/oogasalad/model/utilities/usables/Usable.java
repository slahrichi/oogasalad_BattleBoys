package oogasalad.model.utilities.usables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import oogasalad.controller.GameManager;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.view.Info;

public abstract class Usable {

  String myID;
  int goldCost;
  Map<Coordinate,Integer> relativeCoordShots;
  public Usable(String ID, int gold){
    this.myID = ID;
    this.goldCost = gold;
    relativeCoordShots = new HashMap<>();
  }

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

  public abstract UsableFunction getFunction();
  public String getMyID(){return myID;}

  protected void setGoldCost(int cost){goldCost = cost;}
  protected void setMyID(String myID) {
    this.myID = myID;
  }
  public int getPrice(){return goldCost;}


  public abstract String getType();

  public Map<Coordinate, Integer> getRelativeCoordShots(){return relativeCoordShots;}
  protected void addRelativePosition(Coordinate coord, Integer dmg){relativeCoordShots.put(coord, dmg);}

  public abstract BiConsumer<String, GameManager> handleUsage();

}

package oogasalad.model.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.ShipCell;

public abstract class Piece {

  private List<ShipCell> cellList;
  private List<Coordinate> cellListHP=new ArrayList<>();
  private String status;
  private Board myBoard;

  public Piece(List<ShipCell> cellList) {
    this.cellList = cellList;
    status = "Alive";
    intializeHPList(cellList);
    //myBoard = board;
  }

  public List<ShipCell> getCellList() {
    return cellList;
  }

  private void intializeHPList(List<ShipCell> shape){
    for(ShipCell cell : shape){
      cellListHP.add(cell.getCoordinates());
    }
  }

  public abstract void registerDamage(Coordinate hitLocation) ;

  protected boolean checkDeath() {
    return (cellListHP.size() == 0);
  }

  public String getStatus(){
    return status;
  }


  public Consumer<Map<Coordinate,Cell>> update(){
    return null;
  }
  protected void updateStatus(String newStatus){
    status=newStatus;
  }
  public List<Coordinate> getHPList() {
    return cellListHP;
  }

}


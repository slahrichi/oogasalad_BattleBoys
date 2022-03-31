package oogasalad.model.utilities;

import java.util.List;

public abstract class Piece {

  private List<Cell> cellList;
  private String status;
  private Board myBoard;

  public Piece(List<Cell> cellList) {
    this.cellList = cellList;
    status = "Alive";
    //myBoard = board;
  }

  public List<Cell> getCellList() {
    return cellList;
  }

  public abstract void registerDamage(Coordinate hitLocation) ;

  protected boolean checkDeath() {
    return (cellList.size() == 0);
  }

  protected String getStatus(){
    return status;
  }

  protected void updateStatus(String newStatus){
    status=newStatus;
  }
}

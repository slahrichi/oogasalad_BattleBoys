
// Commenting out for now I don't think we are using this anymore... After Confirmation will delete
//
//package oogasalad.model.utilities;
//
//import oogasalad.model.utilities.tiles.CellInterface;
//import oogasalad.model.utilities.tiles.Modifiers.enums.CellState;
//
//public class Shot {
//
//  private Coordinate myRelativeCoordinate;
//  private int myDamage;
//  public Shot(Coordinate relCoord, int damage) {
//    myRelativeCoordinate = relCoord;
//    myDamage = damage;
//  }
//
//  public CellState applyShot(CellInterface affectedCell) {
//    for(int i = 0; i<myDamage; i++) {
//      affectedCell.hit();
//    }
//    return affectedCell.getCellState();
//  }
//
//  public Coordinate getShotAbsoluteCoord(Coordinate absoluteCoordinate) {
//    return new Coordinate(absoluteCoordinate.getRow()+myRelativeCoordinate.getRow(), absoluteCoordinate.getColumn()+myRelativeCoordinate.getColumn());
//  }
//}

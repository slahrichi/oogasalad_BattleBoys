/**
 * this will be a superclass which impemments all the methods shared by a piece(ship) object, like
 * contains a list of pieceBlocks, have methods which update the piece movement if it is a moving
 * piece, handle the event of a piece being hit etc.

public interface Actor {

  /**
   * a general update method which checks all the neccesary things that would need to be update on
   * any single step of the game.

  public update() {

  }
  /**
   * Define the path on which the ship is going to move.

  public setPath(){

  }

  /**
   * if the object has the ability to move, perform the move in the predefined path by the user.

  public updateLocation() {

  }

  /**
   * update the live/dead etc of the object.

  private updateStatus() {

  }
  /**
   * register that a certian block of the piece object has been damaged, check if all blocks are
   * damged now and delete the item, or if only partial damage has been done ract
   * accordingly(disable movement ability)

  public registerDamage(Coordiante gitLocation) {

  }

  /**
   * Get the list of occupied cells by this Piece object.

  public getOccupiedCells() {

  }
}
*/
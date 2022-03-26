# Backlog

### Specifications

Write at least ten Use Cases per team member that describe specific features each person expects to complete — these will serve as the project's initial Backlog. Focus on features you plan to complete during the first two Sprints (i.e., the project's highest priority features). Note, they can be similar to those given in the assignment specification, but should include more detail specific to your project's goals. It may help to role play or draw a diagram of how your classes collaborate to complete each case.

### Brandon Bae

* Use case 1
* Use case 2
* Use case 3
* Use case 4
* Use case 5
* Use case 6
* Use case 7
* Use case 8
* Use case 9
* Use case 10

### Edison Ooi

* Use case 1
* Use case 2
* Use case 3
* Use case 4
* Use case 5
* Use case 6
* Use case 7
* Use case 8
* Use case 9
* Use case 10

### Matthew Knox

* Use case 1: Play a round where only turns are played
* Use case 2: Construct CPU players 
* Use case 3: Construct Real Players
* Use case 4: Place a ship
* Use case 5: Move a ship
* Use case 6: Fire a shot, get gold for hitting
* Use case 7: move actors (at end of round)
* Use case 8: Construct real and CPU Players
* Use case 9: play entire simple round
* Use case 10: make purchase

### Matthew Giglio

* Use case 1: Build different AI variations (easy, medium, hard)
* Use case 2: Allow player to move pieces between rounds
* Use case 3: Use StripeAPI to simulate in game payments
* Use case 4: Allow two players to play across a server
* Use case 5: Throw "exception" when player tries to place boat at invalid coordinate
* Use case 6: Allow player to use certain items (health upgrade, scout, etc) without losing the turn
* Use case 7: Throw "exception" when player lacks enough money to make payment
* Use case 8: Remove items from player's inventory after they have used them
* Use case 9: Remove piece from player's pieces after it has been lost
* Use case 10: Have AI "invert" strategies from Battleship to Minesweeper

### Minjun Kwak

* Use case 1 - The user clicks a location on the enemy board to fire a shot
* Use case 2 - The user clicks a location on their board to place their ship
* Use case 3 - The user begins building their ship in the ship builder, but then decides to change the
number of ships they want to place on their board
* Use case 4 - The user buys a bomb weapon to use for their next shot and fires it to an enemy board
* Use case 5 - The user scrolls through each of the other players' boards to see the shots
they have taken against each player
* Use case 6 - The user fires a shot, but hits a mine
* Use case 7 - The user fires a shot, but hits a radar
* Use case 8 - The user destroys all enemy ships and wins the game
* Use case 9 - The user buys a finder weapon to use for their next shot and fires it to an enemy board
* Use case 10 - The user clicks the sidebar to see how many ships are left for each of the other players

### Saad Lahrichi

* Use case 1
* Use case 2
* Use case 3
* Use case 4
* Use case 5
* Use case 6
* Use case 7
* Use case 8
* Use case 9
* Use case 10

### Eric Xie

* Use case 1
* Use case 2
* Use case 3
* Use case 4
* Use case 5
* Use case 6
* Use case 7
* Use case 8
* Use case 9
* Use case 10

### Prajwal Jagadish

* Use case 1
* Use case 2
* Use case 3
* Use case 4
* Use case 5
* Use case 6
* Use case 7
* Use case 8
* Use case 9
* Use case 10

### Luka Mdivani

* Use case 1 - user initializes a Moving object
```java
    // in grid we initialize an Piece subclass MovingPiece with reflection
    // in constructor:
  public interface MovingPiece(){
  public MovingPice(Coordinate[] movementPath,Coordinate[] shape){
    this.setPath(movementPath);
    this.setStatus("Alive");
    this.initializeShape(shape);
  } 
}
  
```
* Use case 2 - a ship is hit
```java
    // if the traget grid is is identified as a ship grid in the Grid.java
    Piece shisp = new Piece();
    ship.registerDamage(Coordinate location);
    //Inside ship:
    pubic interface  registerDamage(Coordinate location){
      occupiedCells.remove(location);
      checkPieceStatus();
}
  
```
* Use case 3 - a ship is hit and it sinks
```java
   
  public interface checkPieceStatus(){
  if( occupiedCells.size() == 0 )
    {
      setStatus("Dead");
    }
  }
  
```
* Use case 4 - a MovingPiece is hit and its movement ability is disabled
```java
   
  public interface checkPieceStatus(){
  if( occupiedCells.size() == 0 )
    {
      setStatus("Dead");
    }
  if(!status.equals("damaged")){
    setStatus("damaged");
    disableMovementStatus();
    }
  }
  
```
* Use case 5 - Piece has to move 
```java
// Inside Piece
public interface updateLocation(){
  for( Coordinate cell : occupiedCells ){
    for(Coordinate direction : path) {
      if(inBounds()){
      cell.x + path.x;
      cell.y + path.y;
      }
    }
  }
}

```
* Use case 6 - Piece has to move and path leads it out of bounds
```java
// Inside Piece
public interface updateLocation(){
  if (movementStatus)

  {
    for (Coordinate cell : occupiedCells) {
      for (Coordinate direction : path) {
        if (inBounds()) {
          cell.x + path.x;
          cell.y + path.y;
        } else
          () {
          disableMovementStatus();
        }
      }
    }
  }
}

```
* Use case 7 
* Use case 8
* Use case 9
* Use case 10

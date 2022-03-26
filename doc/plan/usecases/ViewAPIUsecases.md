# ViewAPI Usecases

### Placing a piece on the board
```java
// in controller
String ERROR_MESSAGE = "Can't place ship there!";

if (model.canPlaceNextShip(int x, int y)) {
  List<Coordinate> coords = model.getCoordsOfPlacedShip(int x, int y);

  // moves to next ship to be placed
  model.updateNextShip();

  view.placePiece(coords, PieceType.SHIP);
  view.updateCurrentPiece(model.getCoordsOfNextShip);
}
else {
  view.displayError(ERROR_MESSAGE);
}
```

### The user clicks the arrow key to switch the current board they are viewing
```java
// in controller
view.switchActiveBoardBack();
```

### Player 1 clicks a valid spot on Player 3's board 
```java
// in controller

view.displayShotAt(2, 2, true);
remainingShips.remove(remainingShips.size() - 1);
dataVisualizer.updateShipsLeft(remainingShips);
dataVisualizer.setNumShotsRemaining(--shotsRemaining);
dataVisualizer.setGold(currentGold + increaseAmount);

// check if current player has shots left, otherwise switch turns
dataVisualizer.setPlayerTurnIndicator("Player2");
view.setCurrentPlayer("Player 2");

```
### Player 1 sinks one of Player 2's ships
```java
view.displayShotAt(1, 1, true);
remainingShips.remove(remainingShips.size() - 1);
dataVisualizer.updateShipsLeft(remainingShips);
dataVisualizer.setNumShotsRemaining(--shotsRemaining);
dataVisualizer.setGold(currentGold + increaseAmount);
dataVisualizer.setPlayerTurnIndicator("Player2");
view.setCurrentPlayer("Player2"); // only if their shots reach 0?
```

### Player 1 clicks a spot on Player 3's board that was already clicked previously
```java
// in the controller

// same code as previous use case, we assume that the model handles whether a ship was truly 
// sunk or not in their code? also assumes that current grid is player 3's grid, should be linked
// to player 3's controller

if (!model.isValidShot(int x, int y)) {
  
}


```
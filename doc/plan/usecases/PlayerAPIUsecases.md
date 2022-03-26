#PlayerAPI Usecases

## Placing a piece on the board
```java
public void placeBoat(Piece s, Coordinate start, Coordinate end) {
    if (start.getX() == end.getX()) {
      placeOnGridHorizontally(s, start, end);
    }
    else {
      placeOnGridVertically(s, start, end);
    }
    }
    
    private void placeOnGridHorizontally(Piece s, Coordinate start, Coordinate end) {
      int distance = Math.abs(start.getY() - end.getY());
      int startPoint = Math.min(start.getY(), end.getY());
      for (int i = 0; i < distance; i++) {
        myGrid[start.getX()][startPoint + i] = s.getBlocks().get(i);
      } 
    }

private void placeOnGridVertically(Piece s, Coordinate start, Coordinate end) {
    int distance = Math.abs(start.getX() - end.getX());
    int startPoint = Math.min(start.getX(), end.getX());
    for (int i = 0; i < distance; i++) {
    myGrid[startPoint + i][start.getY()] = s.getBlocks().get(i);
    }
    }

```
### Example
```java
/*
    The player will click points on the screen, and that will determine the size of the boat
    From there, that request will be sent to the controller. Then, the controller will send the 
    specific points and Piece object for the Player implementation to place on its board
 */
#in view
Piece boat = new Boat(getDimensionFromPlayer());
controller.placePiece(boat, getStartFromPlayer(), getEndFromPlayer());

#in controller
myPlayer.placePiece(boat, start, end);
```

## Make purchase from shop
```java
public void makePurchase(int amount, Item item) {
    if (myCurrency - amount > 0) {
      updateAccount(amount);
      myItems.add(item);
    }
}

private void updateAccount(int amount) {
    myCurrency -= amount;
    }
```
### Example 
```java
/*
    Front end elements should be set up to send a request to backend when clicked, and the request
    must send the amount and the item to the backend. From there, the method should easily be able
    to determine whether the player can actually buy it
 */
 
#in view
ItemView icon = new ItemView();
icon.getButton().setOnAction(e -> controller.makePurchase(icon.getCost(), icon.getItem()));


#in controller after icon has been clicked
myPlayer.makePurchase(amount, weapon);
```
# Each player takes turn/player makes money when hitting boat
```java
#in controller
private void runGame() {
    while (gameIsNotOver) {
      for (Player p : playerList) {
        p.playTurn();
    }
    }
    }
    
# in human player
public void playTurn()
    List<Coordinate> shots = controller.getCoordinatesFromView();
    controller.fireShots(shots);
}

#in controller
    `public void fireShots() {
        if (enemy.checkForBoat(shots) == States.HIT){
    sendMessageToView(HIT);
    player.addGold(HIT_AMOUNT);
    }
        else{
      sendMessageToView(MISS);
    }
    }
  
    }
# in AI 
public void playTurn() {
    //run some BFS algorithm or random shots
    }
```
# Placing boats between turns
```java
#in controller
if (canMakeMove()) {
  view.promptPlayerMove();
    }

#in view
public void promptPlayerMove(){
    Piece boat=new Boat(getDimensionFromPlayer());
    controller.placePiece(boat,getStartFromPlayer(),getEndFromPlayer());
}
```
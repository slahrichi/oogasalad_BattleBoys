# OOGASALAD: BattleBoys




## Functionality Demos
### Simple Battleship
- Can choose a language at the start of the program
- Can choose between start and create
- Designed to show that while we can support different types of games, we can also make a simple but complete game of regular Battleship.
- Only 3 ships to make the game end quicker
- Win condition of the game is to sink 3 ships
- We will only shoot basic shots because you can only shoot one cell at a time in regular battleship
- Once a player loses and the other player wins, screens show up for both messages and we can return to the main menu

### Minesweeper
- The major way we intend for users to create different variations between games is by utilizing different win conditions
- In battleship we utilized a "Sink X Ships to Lose" condition meaning that if a person has X amount of ships sunk, then they will lose the game. This means that the objective in Battleship is to hit as many enemy ships as possible.
- However, in mine sweeper, we inverted this by making it so that there are two win conditions in play:
    - Hit X Ship Cells to lose
    - Hit X Water Cells to win
- Here we can see that the objective has shifted so that each player wants to avoid hitting ships as much as possible and are trying to "miss" and hit water as much as possible instead
- In this specific variation we made so that a player has to hit 20 water cells to win or hit 3 ship cells to lose.
### AI Only
- We created an abstract `DecisionEngine` class to enable AI players, and there are different extensions of the class that play at different difficulties
- While we will demonstrate AI playing against humans, it is entirely possible for the human player to sit back and watch exclusively AI play against each other

### Feature Rich
- We also added an integration with Stripe Payments, meaning that should a user lack the talent to earn gold, they can use real-world money to purchase items and add them to their inventory
- Variations:

## Design Goals - Turn Based Grid Games

* Strict Model-View-Controller
* Listeners for Frontend, API calls for backend
* Control Managers for each stage of the game
    * Listen to frontend, call backend
* Configuration stored in JSON files

### Designing a Game

* Create categories of elements that are pre-built, ready to be added to a game
* Allow for easy code-editing to create new elements
* Types of elements:
    * Players
    * Pieces
    * Boards
    * Win/Loss Conditions
    * UI
    * Weapons
    * Islands
    * Usables
    * Inventories
    * Meta-game data

# authoring environment
* load and edit an existing game in at least three ways then show the changes by playing the new version
* create a very simple game from scratch to show how to choose/create different features and game values

## ADDING NEW FEATURES TO THE BUILDER
### *adding newly implemented features is very simple due to the dynamic design of builder stages, which conform to pre-defined formatting in  BuilderInfo.properties file*
1. **Adding a new CellStateOption**
    * Add the name of the new cellstate to the comma separated list under `possibleCellState=` .
    * This will enable user to select a color for that cell state, as well as include it in win conditions.

2. **Adding a new Piece Type**
    * Add the name of the new cellstate to the comma separated list under `possiblePieceType=` .
    * For each new Piece type add `PieceTypeRequiredInfo` which is a comma separated list of cutomizable features.
3. **Adding a new parameter to cells(other than HP,Gold)**
    * Add a new string to the comma separated list named `pieceCellCustomParameters`
4. **Adding a new Weapon or Item**
    * Add a new string(The class name of the weapon) to the comma separated list named `possibleWeaponType`/`possibleItemType` respectively.
    * For each new weapon/item `X`, add an entry `XVariables` to the .properties file, containing a comma separated list of all variables it needs for initialization.
    * If a weapon ( like `ClusterShot`) needs a map for Area of Effect, add the name of the weapon to a list called `needsAOEMapWeapon`.
      ![](https://i.imgur.com/Kg174WG.png)

5. **Adding a new win condition**
    *  Add a new string(The class name of the winCondition) to the comma separated list named `possibleWinConditionType`.
    *  For each new WinCondition `X`, add an entry `XVariables` to the .properties file, containing a comma separated list of all variables it needs for initialization.
    *  If a win condition needs a selection of which cellStates need to be hit add the name of the win condition to `needsCellsToHitSelection` list.
6. **Add a new PlayerType**
    * Add a the new playerType anme to `possiblePlayerType` list.
    * If the new player type needs an engine attached, add the name to `needsEngineSelection=`.
    * If you add new engine options add the name of the new difficulty to `AIPlayerEngineOption`.
7. **Add new Game Variable**
    * add the string name of the variable to `gameVariables=` list.
8. **Add new modifier**
    * add sting class name to `possibleModifierTypes`
    * for each modifier add `xVariables` with a comma separated list of required variables
        * if a variable needs to be a string add the variable name to `needsStringInputFilter` .

### Example JSON file - parser

# Tests
## Integration
- Testing the `Game.java` class, the file that launched view elements for preparing the game and then actual game itself, relies on both frontend and backend elements
- Thus, testing the class entails testing the parser's functionality along with the view's proper responses
- Since the test relies on a user selecting a file for parsing, we mocked that action by leveraging the Mockito framework
```java
public class GameTest extends DukeApplicationTest {

private Game spy;
private File file;

  private final ResourceBundle myResources = ResourceBundle.getBundle("/languages/English");


  @BeforeEach
  void setup() {
    file = new File(System.getProperty("user.dir") + "/data/again.properties");
  }

  @Test
  void testBasicGame() throws InterruptedException{
    javafxRun(() ->
    {
      spy = Mockito.spy(new Game(new Stage()));
      Mockito.doReturn(file).when(spy).chooseDataFile();
      spy.selectLanguage();
      spy.propertyChange(new PropertyChangeEvent(new LanguageView(),
          "languageSelected", null, myResources));
      spy.propertyChange(new PropertyChangeEvent(new StartView(myResources),
          "loadFile", null, null));
    });
    Thread.sleep(3000);
    assertNotEquals(null, lookup("#pass-computer-message-button").query());
  }

  @Test
  void testInvalidFile() throws InterruptedException{
    javafxRun(() ->
    {
      spy = Mockito.spy(new Game(new Stage()));
      Mockito.doReturn(new File(System.getProperty("user.dir")
          + "/data/FakeFile.properties")).when(spy).chooseDataFile();
      spy.selectLanguage();
      spy.propertyChange(new PropertyChangeEvent(new LanguageView(),
          "languageSelected", null, myResources));
      spy.propertyChange(new PropertyChangeEvent(new StartView(myResources),
          "loadFile", null, null));
    });
    Thread.sleep(1500);
    assertNotEquals(null, lookup("#game-alert").query());
  }
}
```
## Frontend
- Testing the `SetupView.java` and `GameView.java` classes, the files that are in charge of showing the view of setting up ships, and the view of playing the game.
- The example test for `SetupView.java` places a piece on the board and tries to remove it, checking if the piece was indeed placed and then checking if the piece was indeed removed by looking at the color of the cell.
- The example test for `GameView.java` checks that the current board is BoardView 0, then switches to the next board to make sure we can click on a cell that is part of the next board.
```java
  @Test
  void removePiece() {
    sleep(2000);
    clickOn(lookup("#pass-computer-message-button").query());
    clickOn(lookup("#ok-button").query());
    Polygon cell2_2 = lookup("#setup-view-pane #setup-center-box #boardBox #board-view #board-view-base #cell-view-2-2-0").query();
    clickOn(cell2_2);
    assertEquals(Color.BLACK, cell2_2.getFill());
    clickOn(lookup("#remove-last-button").query());
    assertEquals(Color.BLUE, cell2_2.getFill());
  }

  @Test
  public void testSwitchBoard() {
    assertEquals(view.getCurrentBoardIndex(), 0);
    clickOn(rightButton);
    Polygon cell1 = lookup("#view-pane #view-center-pane #board-view #board-view-base #cell-view-0-0-1").query();
    clickOn(cell1);
    assertEquals(view.getCurrentBoardIndex(), 1);
    clickOn(leftButton);
  }
```
## Model Bad Tests: Exception Handling

- For the backend we do all of our exception handling at the parser level. Here the parser makes sure files exist and are properly formatted
    - if either of these are not true we throw exceptions
    - Examples:
        - We check the actual validity of the files themselves using the loadBadPropertiesFile() test which checks if the file paths given are valid otherwise throws a bad path exception
        - We also check if the data is valid as well. For example, the loadBoardWithMissingData and loadPiecesWithMissingData checks for if all data exists otherwise the parser throws a missing data exception
```java=
  @Test
  void loadBadPropertiesFile() {
    String path = "src/test/resources/BadPlayers.properties";
    ParserException thrown = assertThrows(ParserException.class, () -> parser.parse(path));
    assertEquals(exceptionMessageProperties.getProperty("badPlayer").formatted("PlayerThatObviouslyDoesNotExist"), thrown.getMessage());
  }

  @Test
  void loadBoardWithMissingData() {
    String path = "src/test/resources/BoardWithMissingData.properties";
    String jsonPath = "src/test/resources/BoardWithMissingData.json";
    ParserException thrown = assertThrows(ParserException.class, () -> parser.parse(path));
    assertEquals(exceptionMessageProperties.getProperty("missingData").formatted(jsonPath,"Board"), thrown.getMessage());
  }

  @Test
  void loadPiecesWithMissingData() {
    String path = "src/test/resources/PiecesWithMissingData.properties";
    String jsonPath = "src/test/resources/PiecesWithMissingData.json";
    ParserException thrown = assertThrows(ParserException.class, () -> parser.parse(path));
    assertEquals(exceptionMessageProperties.getProperty("missingData").formatted(jsonPath,"Pieces"), thrown.getMessage());
  }
  }
```
## Model Good Test
- In order make sure all the components are working together we tried to utilize tests that created all components starting from the game manager to the pieces.
- For example, in the testStrategyAdjustmentWithBFS() we simulate an entire simple game of battleship with a human and AI. The test checks if AI actually adjusts its strategy against the player given the hits its make.
```java
@Test
  void testStrategyAdjustmentWithBFS() throws InterruptedException {
    GameData gd = new GameData(playerList, pieceList, cellBoard, engineMap,
        new ArrayList<>(), new HashMap<>(), new ArrayList<>(),
        new ArrayList<>(), new ArrayList<>(), new HashMap<>(), new ArrayList<>(),
        1, 0, 100);
    javafxRun(() -> {
          gs = new GameSetup(gd, myResources);
        }
    );
    Thread.sleep(2000);
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "placePiece", null, "0 0")));
    javafxRun(() -> gs.propertyChange(new PropertyChangeEvent(gs.getSetupView(),
        "moveToNextPlayer", null, null)));
    Player ai = playerList.get(1);
    CellState[][] board = ai.getBoard().getCurrentBoardState();
    javafxRun(() -> {
      gm = new GameManager(gd, myResources);
      gm.createScene();
    });
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), dummyColorMap, myResources), "handleShot", null, info)));
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), dummyColorMap, myResources), "endTurn", null, info)));
    Thread.sleep(1500);
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), dummyColorMap, myResources), "handleShot", null, info)));
    javafxRun(() -> gm.propertyChange(new PropertyChangeEvent(new GameView(
        list, new ArrayList<Collection<Coordinate>>(),
        new HashMap<>(), new ArrayList<>(), dummyColorMap, myResources), "endTurn", null, info)));
    CellState[][] enemyBoard = playerList.get(0).getBoard().getCurrentBoardState();
    Thread.sleep(1500);
    List<Coordinate> list = findCoordinateStruck();
    assertEquals(1, Math.max(1, Math.abs(list.get(0).getColumn() - list.get(1).getColumn())));
  }
```
# Design.
Revisit the design from the original plan and compare it to your current version (as usual, focus on the behavior and communication between modules, not implementation details):

revisit the design's goals: is it as flexible/open as you expected it to be and how have you closed the core parts of the code in a data driven way?

## API's
### Player API
```java=
/**
 * WinCondition API analyzes the board state for every player in order to check/determine if a player has
 * won or lost the current game. Determining win states in WinConditions will be defined by a lambda function.
 * This class is intended to be an internal API to the Controller to help determine winners using controller
 * parameters
 *
 * @author Brandon Bae
 */
public interface WinConditionInterface {

  /**
   * updateWinner method sends the WinCondition's lambda to the provided collection of players in order
   * to update their win state
   * @param player Player to apply the WinCondition's win lambda to
   */
  public WinState updateWinner(Player player);

  /**
   * This method is used to get the functional lambda that defines how the WinCondition determines if a Player wins or not
   * @return Function lambda that takes in the old PlayerRecord as a parameter and returns an updated PlayerRecord
   */
  public abstract Function<PlayerRecord, WinState> getWinLambda();

  /**
   * This method returns a set of desirable cell states associated with this Win/Loss Condition. This is meant to be
   * used by the AI players to determine what types of hits are actively helping the AI move closer to winning.
   * @return Set of cell states that represents cell states that help fulfill the win condition
   */
  public Set<CellState> getDesirableCellStates();

  /**
   * This method returns a set of nondesirable cell states associated with this Win/Loss Condition. This is meant to be
   * used by the AI players to determine what types of hits are detrimental to the AI such as fulfilling a loss condition
   * or not helping a win condition.
   * @return Set of cell states that represents cell states that be detrimental for an AI to continue pursuing
   */
  public Set<CellState> getNonDesirableCellStates();
}
```
#### Providing for Extensibility
* Have generic methods that allow for implementations of win conditions to define their own functionality. For example, the getWinLambda() method allows for users to define their own functionality. For example, the loseXShips win condition implementation, it checks for a player's sunk ships to determine if they lost or not. On the other hand, the WinXGold Win condition allows for players to win if they have a certain amount of gold


#### Supporting Good Code
- The WinCondition API supports good code by having generic yet well named methods that provide extensive functionality while preventing other users from messing too much with the player's variables.

#### Use Cases
- One use case for the updateWinner() method can be found in the checkCondition() method. This is used in the condition handler to apply all winconditions to every player
- One use case for the buildWants() method can be found in the buildWants method(). This is used by the decision engines to construct lists of desirable cell states to adapt to each win condition.
```java=
private void checkCondition(WinCondition condition) {
    Set<Integer> playerIds = new HashSet<>(idMap.keySet());
    for (int id : playerIds) {
      Player currPlayer = idMap.get(id);
      WinState currPlayerWinState = condition.updateWinner(currPlayer);
      LOG.info(String.format(PLAYER_WINSTATE, id+1, currPlayerWinState));
      checkWinState(currPlayer, currPlayerWinState, id);
    }
  }

private void buildWants(List<WinCondition> conditionList) {
    wants = new HashSet<>();
    for (WinCondition condition : conditionList) {
      wants.addAll(condition.getDesirableCellStates());
    }
  }
```

### Frontend API: BoardVisualizer
```java=
public interface BoardVisualizer {

  /**
   * Places a Piece of a certain type at the specified coordinates
   * @param coords Coordinates to place Piece at
   * @param type Type of piece being placed
   */
  void placePiece(Collection<Coordinate> coords, CellState type);

  /**
   * Removes any Pieces that are at the coordinates contained in coords.
   */
  void removePiece(Collection<Coordinate> coords);
}
```
#### Providing for Extensibility
* Provides explicit functionality for any powerups or events that involve moving, removing, or adding new Pieces to any board.
* Define what is needed in supplementary board views


#### Supporting Good Code
Users of API can state how they want to change a given Piece on a board view declaratively. Moving pieces often requires complicated logic, so to hide that from the view, we can define a move as simply removing a ship and placing it elsewhere.

#### Use Cases
```java=
// In GameSetup, used to handle placing current piece and moving to next one
private void updatePiece(Piece piece) {
    List<Coordinate> coords = new ArrayList<>();
    for (ShipCell cell : piece.getCellList()) {
      coords.add(cell.getCoordinates());
    }
    lastPlacedAbsoluteCoords.push(coords);
    setupView.placePiece(coords, CellState.SHIP_HEALTHY);
    pieceIndex++;
    moveToNextPiece();
}
```
```java=
// Creation of remove piece button in setup view
Button removeLastPiece = ButtonMaker.makeTextButton(REMOVE_LAST_ID,
        e -> removePiece(lastPlaced), myResources.getString(REMOVE_LAST_RESOURCE));
```



* describe two APIs in detail (one from the first presentation and a new one):
* show the public methods for the API
* how does it provide a service that is open for extension to support easily adding new features?
* how does it support users (your team mates) to write readable, well design code?
* how has it changed during the Sprints (if at all)?
* show two Use Cases implemented in Java code in detail that show off how to use each of the APIs described above

------
* describe two designs
* one that has remained stable during the project

One aspect of the project that remaining relatively stable was the front-end design, specifically the idea and the hierarchy of BoardView. The concept behind BoardView, an abstract superclass, was to serve and define how a board was created inside the front-end. This hierarchy consisted of subclasses including the GameBoardView, EnemyBoardView, SelfBoardView, and SetupBoardView.

```java=
  public BoardView(double size, CellState[][] arrayLayout, Map<CellState, Color> colorMap, int id) {
    myID = id;
    myColorMap = colorMap;
    setupBoard(arrayLayout, size);
  }
```

In the constructor of the superclass, the design of the BoardView stayed pretty similarly throughout. Each board was created with a size, a 2d array of the board layout, its associated colormap, and an important id integer. This id was linked to the player's and allows both the front-end to understand which board the current player is shooting at.

This design trickled down to the aforementioned subclasses, allowing us to develop boards that extended the aspects of general boards while being geared to the stage they were used in:

```java=
public abstract class GameBoardView extends BoardView {

  public GameBoardView(double size, CellState[][] arrayLayout, Map<CellState, Color> colorMap, int id) {
    super(size, arrayLayout, colorMap, id);
  }

  /**
   * Initializes the cells of the BoardView by creating new CellView instances and adding them to
   * the CellView array associated with this BoardView
   *
   * @param arrayLayout the layout of the cells of this BoardView
   * @param size        the size of each cell
   */
  public void initializeCellViews(CellState[][] arrayLayout, double size) {
    for (int row = 0; row < arrayLayout.length; row++) {
      for (int col = 0; col < arrayLayout[0].length; col++) {
        List<Double> points = BoardMaker.calculatePoints(row, col, size);
        CellView cell = new CellView(points, myColorMap.get(arrayLayout[row][col]), row, col);
        cell.addObserver(this);
        myLayout[row][col] = cell;
      }
    }
  }
```

![](https://i.imgur.com/IznzLrz.png)





* one that has changed significantly based on your deeper understanding of the project: how were those changes discussed and what trade-offs ultimately led to the changes


    * Parser's design changed a lot
    * Initial choice between XML, JSON, and properties
    * Went with properties first, realized it won't support our data complexity 
    * Json seemed more effective at storing data (Cell>CellType>(Coordinates,Gold,id))
    * Parsing using Gson was cumbersome (especially Abstract classes)
    * Alternative: use XML. Has its own set of issues 
    * Hard to manipulate (bad experience @CellSociety)
    * Cannot add custom tags
    * Quite verbose



# Team.

* Contrast the completed project with where you planned it to be in your initial Wireframe and the initial planned priorities and Sprints with the reality of when things were implemented
    * The completed project is very similar to the initial Wireframe in terms of structure. We have added minor features throughout, such as an inventory, to enhance playing experience. In terms of our plan, we generally followed the same structure of first implementing playable battleship, then abstracting out the configurations into data files, then adding variations and extensions. However, we did not meet specific timeline goals, as implementing the first version of Battleship went slowly but progress sped up significantly later on.
* **INDIVIDUALLY, share one thing each person learned from using the Agile/Scrum process to manage the project**
* Show a timeline of at least four significant events (not including the Sprint deadlines) and how communication was handled for each (i.e., how each person was involved or learned about it later)
    * Being able to place pieces on the board in setup and persist it to game phase
    * Completion of game loop
    * Shop
    * Generating and parsing data files into working game configurations
* **INDIVIDUALLY, share one thing each person learned from trying to manage a large project yourselves**
* Describe specific things the team actively worked to improve on during the project and one thing that could still be improved
    * One big thing we worked on was being really strict with merge requests and general git hygiene. As a result, we kept large merge conflicts to a minimum.
    * One thing that can still be improved is better communicating when and why we need to change somebody else's code. For example, GameManager would often become a playground for patchy fixes and methods for new features, which would throw off the thought process of the people in charge of controller.
* **INDIVIDUALLY, share one thing each person learned about creating a positive team culture**
* Revisit your Team Contract to assess what parts of the contract are still useful and what parts need to be updated (or if something new needs to be added)
    * As the project went on, our Monday meetings became less strict and meetings were scheduled whenever the team as a whole felt like enough progress had been made. Everything else remained the same.
* **INDIVIDUALLY, share one thing each person learned about how to communicate and solve problems collectively, especially ways to handle negative team situations**
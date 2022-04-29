# DESIGN Document for OOGASALAD
## Team Battleboys
## Minjun, Prajwal, Eric, Matthew, Matt, Luka, Edison, Brandon, Saad


## Role(s)

* Matthew
    - Designed and implemented controller package
    - Designed and implemented Stripe integration for in game payments
    - Designed and implemented `DecisionEngine` abstract class to enable CPU gameplay
    - Provided support for frontend and backend teams

* Minjun
    * helped with layout of view elements
    * designed core communication between view classes and the controller
    * Worked on frontend features/design such as ship hovering and BoardView structure
    * Worked on controller logic with general game loop, sending correct information to view, and managing weapons/items to make the game function properly.

* Matt
    * Solved dependency issue between Stripe and GSON
    * Created functionality to ping Stripe server
    * Designed parsing heirarchy
    * Implemented a majority of the parsers as well.

* Edison
    * Integration of frontend into controllers GameSetup and GameView
    * Refactoring frontend into small, reusable subclasses (see screens and makers packages) with inheritance hierarchie
    * Setting up GUI structure (no styling)
    * Frontend API development

* Eric
    * Designed the AbstractClass hierarchy w/ Edison and worked on creating a stage-like design in the front-end (from the LanguageView to the StartView, SetupView, etc.)
    * Refactored the front-end's design with ResourceBundles and added multiple language functionality to all the nodes
    * Created stylesheets for the different main View classes as well as made images for some of the screens
* Brandon

* Saad
    *  Designed main parts of `Board.java`
    *  Worked on updating `Cell.java` to match changes to `Board.java` and added logic to place cells on board
    * Worked on `PlayerData.java` to integrate parser's returned data with the Board
    * Implemented most Parsers and refactored the main parser into smaller classes

* Luka
    * Completely designed the hierachy of GameBuilder, its UI and functionality logic for every stage, as well as created agile and robust inheritance heirrarchy and abstractions.
    * Helped with the design of the genral structure of backends, and how internal communication would happen.
    * Created original abstractions and starting basic implementations for `Piece.java` and `Weapon.java` .
    * Wrote the GUI for ShopView and ShopPiece, helped with integrating it with the backend.



## Design Goals

- Extendibility
    - Many of the classes are dependent on inheritance hierarchies or interfaces, so that in the event new features are added to the game, the existing package does not need to be changed at all or to a very small degree to accomodate new variations or additions
    - It is easy to add new weapons and items, new win conditions, and new islands to the game by adding new classes to existing hierarchies and applying different modifiers on them. This allows for lots of flexibility for new features, as modifiers can change anything in the game and this can be applied to any new class that the user wants to add.
- Encapsulation
    - Nothing in the code should be overly-dependent on anything else in the project. Simply put, classes should nearly be black boxes to each other and pass pertinent information as opposed to revealing complete implementations or all inner workings and data
    - In this way, the project was split up into several different packages, and each package split up responsibilities in order to prevent classes from being overburdened or cluttered
    - Observer pattern also allows data to be encapsulated because the view classes are not directly dependent on the controller class - the view classes simply notify the observers (controller classes) which can then do whatever it chooses using reflection.
    - The classes also avoided the uses of any getters and setters and relied on abstractions such as manager classes or package protection to hide implementation details
- Simplicity
    - Code should be readable and easy to understand as soon as one tries to comprehend it for the first time. We strived to write all our code in such a way that if anyone ever needed to pivot to another part of the project or interact with a class they have never worked with before, they should be able to jump into the full swing of things and work with the code instantly

## High-Level Design
Our design fully embraces the Model-View-Controller philosophy. We have controllers (Game, GameManager, GameSetup) for each phase of our gameplay, which instigate calls to the backend to perform certain calculations or data manipulation which is then reflected on our frontend, which contains little to no business logic.

Our program operates on an event-driven model, meaning our code is stationary until a user activates some sort of event, such as a key press or mouse click, on the view. From there, PropertyChangeListeners propagate the information about the event up to the controllers, which call various methods through reflection to handle these events, including handling a shot, handling switching turns, or opening the shop. These methods then call various public/API methods in the backend to process and manipulate game data. Once those calculations are done, the controller will then call on frontend public/API methods to reflect those changes to the view.

The control-flow of each stage of our program is the same for every stage, however in terms of main classes, our setup phase is controlled by GameSetup and its SetupView, while the gameplay is controlled by GameManager, GameViewManager, and its corresponding GameView. The backend can essentially be represented as a list of players, who own most of their specific data (board, pieces, health, etc), and those references persist throughout the entire lifespan of the game. For the building stage, an instance of GameSetupView is all that is needed in order to build an entire game configuration and store into data files. Each of these main View classes utilize a hierarchy of abstracted JavaFX components to protect encapsulation and clean code. Our Game class is the orchestrator of the entire program, and is responsible for switching to different stages.

Configuration data for custom games, win conditions, pieces, etc. is done completely through JSON and properties files. The creation of these files is facilitated by our Game Builder, which provides a GUI for modifying these custom fields. These files are saved and then later parsed upon game launch, in the creation of a Game objec to configure a fully-custom game. Visual attributes are also almost completely configured in CSS and properties files (see resources/stylesheets).




## Assumptions or Simplifications
- Builder Limitations
    - User must put in Unique ID's for each piece and usable as we store these components in Maps
    - Cannot save and edit files once created
- Game Limitations
    - Win Conditions limited by the existing cell states, must choose from predefined list of cell states
    - Player win by either making everyone else lose or getting a win state
    - WinConditions can only be created/determined by the player's instance variables as we rely on passing the player record to the wincondition lambda to check if a player has won

* Parser Assumptions
    * Whenever the parser is updated, old save files are invalidated. Therefore, to load new files, recreate the original save files.
- Frontend Assumptions
    - We assumed that the frontend components would stay the same, in that the components on the screen are not able to be changed by data files.
    - We assumed that buttons, labels, dialogs, and screens would be made in a specific way everytime, which is why we created static `Maker` classes for making these JavaFX components easily and we set IDs for all these components to allow for additional styling in CSS files.
- Controller Assumptions
    - While the controller checked for invalid player moves or item usages, it did not check for incorrect data passed into it from the beginning, so the assumption was that the `GameSetup` and the parser package would vet all inconsistent data.

## Changes from the Plan

Over the course of 5 weeks, our development of OOGAsalad: BattleBoys has been interesting as well as challenging. From our initial plan, we've faced obstacles that have made us adapt and overcome them.

* Front-End Deviations
    * In terms of front-end, it seems that we managed to achieve the general goals which included developing an abstraction of BoardView as well as utilizing a observer-listener pattern to communicate with the controller. One main deviation from our initial hope was that we didn't use as much APIs that were thought of in the front-end to communicate with the controller as we initally hoped.

    * For BoardView, we furthered upon the original plan by also creating a secondary abstract GameBoardView, used for any Boards in the front-end that are meant to be displayed during the main game.
    * Other small deviations were seen in managing the idea of separating the data from the front-end where we discussed in detail how what data could be passed to the front-end. This was done in order to prevent the front-end from directly "accessing and/or changing" the back-end data. For example, we discussed about how front-end would recieve and pass information about the player's pieces and the shots itself that were made and shot.


* Back-end Deviations
    * Board + MarkerBoard
        * Originally, we were planning for each player to simply have a `Board` instance. This meant that display a player's shots against another player was as simple as displaying any damaged enum states on the other player's board. However, this was too limiting as there was easy method of keeping track of a player's individual shots against an enemy's board once multiple players were added. Therefore we had to introduce another helper class called a markerboard which stored the cellstate results of any shot the player makes against another. This means that each player has their own board and a markerboard for each player to keep track of their hit results.
    * Cell Modifiers
        * Originally in order to have hits affect other aspects of the game such as adding gold or adding shots, we were going to have the cell simply call methods from other classes. However, we realized that this would break reverse dependency. Therefore we created a modifiers system that uses lambdas to affect the game instead. When a cell is hit the lambda is passed up the chain of hierarchy: cell->Board->GameManager. This lambda passes the functionality to each level to maintain encapsulation.


* Controller Deviations
    * The `GameManager` initially worked on the premise that the data passed through the listeners from the `GameView` would be encapsulated in an `Info` record, but after there were changes in the frontend, the class had to adjust to handling `String`objects containing the data instead. The only change this resulted in was adding in the capabilities for `String` parsing, but otherwise the package was not changed in any way.
    - The controller was originally going to drive the game loop, but after considering the potential timing mechanisms that would have to be put in place and the managing of AI plaers, it was evident that the game loop ought to be *event-driven* as opposed to doing the promoting itself. Thus, the controller package, specifically the `GameManager`, relied on an observer-listener relationship with the `GameView` and acted upon interaction with the frontend. This resulted in better encapsulation of implementation details and flexibility with adding in new relationships between the frontend and the controller.



## How to Add New Features
- Game variations:
    - The game variations are dependent on various win conditions, so by creating a new `WinCondition`, an abstract class used to define what constitutes an appropriate end of the game, new game variations can be created without altering the rest of the existing code


- Newly implemented features to the Builder:
    - If one creates a new subclass of weapons,pieces or items and wants to add it to the builder options, it can be done effortlessly and following the open-closed principle. One would just need to add the names, and variable requirments to the `BuilderInfo.properties` file in the predefined formating, it will easily be integrated. Adding new design stages is also straightforwads, you just need to create a new subclass of a BuilderStage.java and add it to the main viewbuilder.

- New Winconditions
    - In order to create new win/loss conditions, all one has to do is define the getWinLambda() method. Here users must write a Function Lambda that takes in a Player's PlayerRecord and returns a WinCondition Enum. The lambda will analyze the player record to check the state of the player and see if the fulfill the win/loss condition.


- AI variations:
    - The AI are built upon an abstract `DecisionEngine` class, so building more intricate CPU players or providing alternative strategies can be achieved by extending the base class. Given how CPU difficulties in games are often adjustable, the purpose of the abstract class was to promote extendibility and variation

- Parser variations:
    - If you wish to add new functionality to be parsed, you must also update the associated _ParserData_ record. This record defines the parameters that will be saved and later loaded. 
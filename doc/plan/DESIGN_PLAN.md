# Design Plan

### Specifications

Write your design goals (i.e., where is it most flexible), and the design's primary architecture (
i.e., what is closed and what is open) without committing to any concrete implementation (i.e., do
not reference specific classes, data structures, or code). This should emphasize the abstractions
you will create to capture your game or genre's key commonalities and differences. Describe how your
intended design will handle your team's goals for the project's basic and extended functionality
using APIs to provide services rather than simple classes. For each API you plan to build, provide a
roughly "one page" high-level design overview using the format below. Include a picture of how the
modules are related (these pictures can be hand drawn and scanned, saved from an online CRC card
tool, written in Markdown, created with a standard drawing program, or screen shots from a UML
design program).

#### Backend

* ...
* The system of adding new actors(actors) will be very flexible, we will let users define the
  specific shape of the actor, as well as customize characteristics like static/moving actor
  objects, and movement patterns if applicable.
* The creation of weapons will also be very dynamic and flexible, we will let users mix and match
  weapon characteristics and projectile types . The basic projectile classifications will be
  default, cluster, scanning and maybe even moving projectiles. For moving projectiles we will also
  let the user define the path of the projectile, as well as define the area of effect of a weapon.
* A `PlayerAPI` will be used to define specific implementations of players such as human players 
versus AI. The AI will have different difficulties.
* The controller will manage the different `Player` instances and relay communication between the 
`Player` instances and the view
* The backend model will contain the information about the current game state, what ships are in play, 
  weapon types, shop, and type of every part of the grid. The controller will call methods in the model to
  update the state of the game accordingly and the model will feed the view so it can display the correct information

#### Frontend
* The master view class will show the scene that allows users to place their boats on their board. Once
the confirm button is clicked, the master view class will show the main game screen scene.
* We plan to have a BoardView abstraction, which creates a board for each player in the game, and these
boards can be connected to specific model instances for multiple controllers to interact with.
* An observer-listener pattern will be used to pass information from the view to the controller, 
making it so the view doesn't need to know about what is going on in the backend, and can
instead focus on showing the correct UI based on the actions of the controller.
* The idea of moving boats is open to extension because we provide functionality to place and remove boats
anywhere on the screen that is legal. This allows the controller to use these view methods
in whichever way it chooses to achieve the functionality it wants.
* The idea of shooting shots is open to extension because we provide basic functionality for the player
to click an enemy board to fire their shot, which means this naturally extends to 
functionality that allows players to fire multiple shots at multiple different boards, where each board 
can be seen by switching the currently active BoardView.

### Team Responsibilities

* Luka - I am planning to mainly focus on backend for this project, I will probably work on
  implementing ship and weapon abstractions and hierarchies as my first task, I will go on to work
  on anything that is needed as the project goes on. Since I have experience in frontend I might
  also switch to work on that at some point in the project.
* Matthew - Developing `PlayerAPI` and its specific implementations such as the `HumanPlayer` and
the `AI` variations. Will also develop controller.
* Prajwal - I am focusing on the backend for the program. I will work on creating the tile occupants class
  which represents the most basic unit of the board and is implemented and abstracted to create all the special 
  tiles like a ship block, mine block, island block etc. Depending on what is needed I will either move to the shop model or
  I will move to the ship builder which will allow users to create custom ships each with different functionalities, and that

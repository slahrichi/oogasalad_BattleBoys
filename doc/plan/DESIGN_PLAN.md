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


### Team Responsibilities

* Luka - I am planning to mainly focus on backend for this project, I will probably work on
  implementing ship and weapon abstractions and hierarchies as my first task, I will go on to work
  on anything that is needed as the project goes on. Since I have experience in frontend I might
  also switch to work on that at some point in the project.
* Matthew - Developing `PlayerAPI` and its specific implementations such as the `HumanPlayer` and
the `AI` variations. Will also develop controller.
* Saad - Focus on backend, build with Brandon, Luka, and Prajwal the `Tile` API and its subclasses
`Island`, `ShipBlock`, `Water`,... As I worked on the parser of the last two projects, I will also
work on parsing the game file to commands the model can understand, and deliver the results to the controller
to set up the game.
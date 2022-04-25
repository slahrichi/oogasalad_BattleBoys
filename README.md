oogasalad
====

This project implements an authoring environment and player for multiple related games.

Names: Brandon Bae, Matthew Giglio, Matt Knox, Edison Ooi, Minjun Kwak, Prajwal Jagadish, Saad Lahrichi, Eric Xie, Luka Mdivani


### Timeline

Start Date: March 16th, 2022 (Team Choice)

Finish Date: April 24th, 2022 (Complete)

Hours Spent:

- Controller
    - Matthew: 55-65 hours across all work (controller, Stripe, FE/BE)
    - Matt:

- Back-End
    - Brandon:
    - Prajwal:
    - Luka: 5-8 Hours
    - Saad:

- Front-End
    - Edison: 45 hours total (GUI, controller integration with frontend)
    - Eric: 45 hours for the total project (front-end GUI, pair programming, etc.)
    - Minjun:
    - Luka : 40 Hours (Total 45-48 Hrs)

### Primary Roles

- Controller
    - Matthew:
        - Designed and implemented controller package
        - Designed and implemented Stripe integration for in game payments
        - Designed and implemented `DecisionEngine` abstract class to enable CPU gameplay
        - Provided support for frontend and backend teams
    - Matt:

- Back-End
    - Brandon:
    - Prajwal:
    - Luka: Created the abstract classes and basic implementations for Piece as well as Weapon classes. Helped create the overall backend design in the planning stage.
    - Saad:

- Front-End
    - Edison:
        - Integration of frontend into controllers GameSetup and GameView
        - Refactoring frontend into small, reusable subclasses (see screens and makers packages) with inheritance hierarchie
        - Setting up GUI structure (no styling)
        - Frontend API development
    - Eric:
        - Worked on integrating front-end with the controller through the Game, GameSetup, and GameViewManager classes
        - Designed the AbstractClass hierarchy w/ Edison and worked on creating a stage-like design in the front-end (from the LanguageView to the StartView, SetupView, etc.)
        - Refactored the front-end's design with ResourceBundles and added multiple language functionality to all the nodes
        - Created stylesheets for the different main View classes as well as made images for some of the screens
    - Minjun:
        - Developed `Noooode.java`
    - Luka: Wrote the code for all front-end (+logic ) of all GameBuilder design stages, as well as made the abstractions and hierarchy architecture. Wrote the GUI for the Shop (ShopView.java,ShopItem.java), helped integrate with backend/controller.


### Resources Used
- Battleship Logo and Backgrounds
- Mockito and JUnit testing frameworks


### Running the Program

Main class: Upon launching the program, an instance of our Game class is created, which controls the entire process of selecting a data file to configure the game, allowing players to set up their Pieces, and playing the game until somebody wins.

Data files needed:

Features implemented:
- Game Builder GUI
- Basic Battleship-style gameplay
- Arbitrary number of players
- AI players
- Custom pieces (shape, size, linked powerups)
- Custom islands
- Custom win conditions
- Custom weapons and items (shot modifiers)
- Custom board shape and size
- Moving ships
- In-game shop to purchase weapons and items
- Inventory to hold weapons and items
- Multiple languages
- Night mode (all styling for all views are done in CSS)
-




### Notes/Assumptions

Assumptions or Simplifications:

Currently the GameBuilder Assumes that at least one instance of each object is created.

Interesting data files:

Known Bugs:

Extensions completed:
- Mockito testing framework was used to test seemingly impossible methods in order to increase line coverage
- Stripe payments with a test credit card (4242 4242 4242 4242) can be made in the game
- CPU Players of varying difficulties have been implemented in the event one does not have a friend to play with


### Impressions
Matthew:
- I genuinely enjoyed my first experience working on a controller module, as it forced me to be a "middle man" of sorts and understand the project as a whole. At the same time, working on a single project with 8 other people was a but stressful in terms of maintaining code quality and organization. Nonetheless, it was a valuable experience in terms of growing as a teammate and an engineer
- Personal highlighted experiences include being able to run a server and tabulate payments via the Stripe API and leveraging Mockito to comprehensively test classes that involved file choosing and selection

Edison:
- Overall I thought that it was very rewarding working on a project this large. Since I was part of a pretty modular subteam, communication was pretty easy and I was able to progress pretty quickly on my work knowing that I wouldn't be stepping on too many people's tracks. Progress on this project moved way faster than on previous projects, which speaks to our team's ability to not only plan well, but also divide tasks fairly and properly. As somebody who has preferred independent work for much of my life, this project was a great growth experience.
- I personally enjoyed learning how to refactor frontend code such that I could utilize small, reusable components with inheritance hierarchies, making my main view files much smaller. Moving all styling to CSS was also satisfying.

Luka:
- Overall I think that the assignment was challenging, but I did learn a lot from it. Biggest new experience for me was working in a team of this size, the number of people made it hard because sometimes the individual work was too specific so it really put forwards the need for good planning beforehand. But I think we managed to do this pretty well, and the project was a positive experience for me. I also got to apply the skills I had acquired over the course both in front and back end.

Eric:
- OOGASalad was an interesting, unique, and stressful project. From its introduction as an agile development project and one that had a large number of teammates, the project challenged me in different ways. I decided to take on the role of front-end once again, and it felt better prepared than the previous project as I was now coming in with some experience. Initally, the start was daunting as the design talk and integration with the controller overwhelmed at certain points. However, I'm glad I learned so much about things such as Loggers and the different design patterns that we meshed together to make the project work. As usual, I really enjoyed being able to create a cool looking interface, and I hope that I'll be able to use a more flexible language to create more satisfying interfaces in the future.
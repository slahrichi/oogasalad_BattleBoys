# Sprint Milestones

### Specifications

Describe your plan for how to manage and prioritize the project, including a rough timeline what you expect to complete for each deadline, the extension feature(s) you want to include, and which APIs each team member plans to take primary and secondary responsibility for. Specifically, each person should take responsibility for specific features and Use Cases they intend to work on during each Sprint (i.e., each week). This requires the team to agree on the feature priorities and set goals for what to complete for each deliverable.

#### By Thursday 3/31
- Have `PlayerAPI` and `ViewAPI` developed with some specific implementations working
- Create all "utility" or abstract classes such as:
  - `Coordinate.java`
  - `Piece.java`
  - `Item.java`
  - `Grid.java`
  - `Cell.java`
- Also create some specific implementations like 
  - `Weapon.java`
  - `Boat.java`
  - `Mine.java`
- 75% unit test coverage for backend, and 50% for frontend


#### By Saturday 4/10
- `GameManager` fully implemented
- Parsing capabilities fully working and interacting with `GameSetup`
- Ensure that `Player.strike()` effectively works for `HumanPlayer`
- Complete `GameView` for game between `HumanPlayers`, adding transition scene from player to
player
- With all these things working together, 2+ players should be able to play each other in battleship
- Test coverage for all previously written code ( 75% unit test coverage for backend, and 50% for 
frontend)

#### By Wednesday 4/13
- Add front end features for light/dark theme
- Make sure that interaction between `GameSetup` and `SetupView` allows for players to move ships
before clicking "Confirm"
- UI for setting up game (labels with text fields) should be able to handle requested `Player` types,
various `Cell` shapes, and number of boats should be ready
  - UI should be able to save these features in config files
- Work on `BoatBuilder`, `GridBuilder`, and `WeaponBuilder` features should be underway
- Backend should have developed some different `Weapon` types and be able to integrate them with
config files and frontend
- Development of AI players should be started and/or nearly complete


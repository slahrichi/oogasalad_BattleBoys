# Test Plan

### Specifications

Describe at least two specific strategies your team discussed to make your APIs more easily
testable (such as useful parameters and return values, using smaller classes or methods, throwing
Exceptions, etc.). Describe three test scenarios per team member for a project feature they plan to
be responsible for (at least one of which is negative/sad/error producing), the expected outcome,
and how your design supports testing for it (such as methods you can call, specific values you can
use as parameters and return values to check, specific expected UI displays or changes that could be
checked, etc.). A test scenario describes the the expected results from a user's action to initiate
a feature in your program (whether "happy" or "sad") and the steps that lead to that result.

## Strategies

### Specific Strategy 1

### Specific Strategy 2

## Test Scenarios

### Brandon Bae

* Test scenario 1
* Test scenario 2
* Test scenario 3

### Edison Ooi

* Test scenario 1: Test that a user is not allowed to place a piece on top of another piece during
  setup phase.
* Test scenario 2: Test that a user is not allowed to shoot at a cell that contains a ship they have
  already sunken.
* Test scenario 3: Test that a win screen is successfully shown when the user meets a win condition.

### Matthew Knox

* Test that firing weapons works correctly, even when firing out of bounds
* Test that placing ships works correctly, even when placing out of bounds
* Test that turns are played correctly based on input data.

### Matthew Giglio

* Test that players receive payments upon hits
* Test that `PlayerAPI` works with different implementations (Liskov with controller and view)
* Test that controller successfully passes backend "exceptions" to view prompts

### Minjun Kwak

* Test scenario 1 - Test that a user is not allowed to shoot at their own board
* Test scenario 2 - Test that boats can't be placed in invalid locations on their board
* Test scenario 3 - Test that player boats are shown in increasing order using the arrow keys - when
  it is player 2's turn, their board is shown as the left-most board and boards are shown in order (
  1, 3, 4, 5, 6...)

### Saad Lahrichi

* Test scenario 1
* Test scenario 2
* Test scenario 3

### Eric Xie

* Test scenario 1
* Test scenario 2
* Test scenario 3

### Prajwal Jagadish

* Test scenario 1 - Test that when every ship block in a ship collection is destroyed then the ship
  must know that it no longer is 'alive'
    * If a shipblocked is healed, that should also be conveyed to the ship collection.
* Test scenario 2 - Test that the ship builder is able to take in details from the front end and
  create a json class with respective properties
    * Should check that the shape is valid by making sure that all sides are connected in some way
    * If they are not, it should let the user know that the json file they used was not valid and
      prompt for a new one
* Test scenario 3 - Test that when special islands are hit, they are able to hit apply special
  affects to the rest of the model
    * If a point multiplier island is hit then it should affect the model point multiplier

### Luka Mdivani

* Check that all different basic weapons work as intended.
    * Check that edge cases (weapons hitting on the edge of a map,near an island object,interacting
      with multiple ships at once) work correctly.
        * a sad option is that if a cluster bomb is aimed at the corner of the map etc, the model
          should be able to apply the effect to only the cells which exist, and not cause a
          NullPointer exception.
* Check how customizable of weapons are, and that editing specifications doesn't break their
  effects.
    * Will have custom WeaponError to make testing and error handling easier.

* Check that piece objects move correctly
    * Check that model doesn't crash if a ships' path leads it out of map bounds( the ship should
      stop at the edge)
    * Will support custom errors to make testing easier. 

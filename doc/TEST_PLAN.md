# Test Plan

### Specifications

Describe at least two specific strategies your team discussed to make your APIs more easily testable (such as useful parameters and return values, using smaller classes or methods, throwing Exceptions, etc.). Describe three test scenarios per team member for a project feature they plan to be responsible for (at least one of which is negative/sad/error producing), the expected outcome, and how your design supports testing for it (such as methods you can call, specific values you can use as parameters and return values to check, specific expected UI displays or changes that could be checked, etc.). A test scenario describes the the expected results from a user's action to initiate a feature in your program (whether "happy" or "sad") and the steps that lead to that result.

## Strategies

### Specific Strategy 1

### Specific Strategy 2

## Test Scenarios


### Brandon Bae

* Test scenario 1
* Test scenario 2
* Test scenario 3

### Edison Ooi

* Test scenario 1
* Test scenario 2
* Test scenario 3

### Matthew Knox

* Test that firing weapons works correctly, even when firing out of bounds
* Test that placing ships works correctly, even when placing out of bounds
* Test that turns are played correctly based on input data. 

### Matthew Giglio

* Test that players receive payments upon hits
* Test that `PlayerAPI` works with different implementations (Liskov with controller and view)
* Test that controller successfully passes backend "exceptions" to view prompts

### Minjun Kwak

* Test scenario 1
* Test scenario 2
* Test scenario 3

### Saad Lahrichi

* 1. Exception handling: If the user creates an incomplete game, we should either complete it with default values
or load a game by default, after telling them about the error.
* 2. Exporting game live: If the user decides to export the game setup after rules have changed during the game,
 the exported file should have the updated game rules.
* 3. If the user makes two rules that are contradictory, we should throw an exception and tell them about the error


### Eric Xie

* Test scenario 1
* Test scenario 2
* Test scenario 3

### Prajwal Jagadish

* Test scenario 1
* Test scenario 2
* Test scenario 3

### Luka Mdivani
* Check that all different basic weapons work as intended.
    * Check that edge cases (weapons hitting on the edge of a map,near an island
      object,interacting with multiple ships at once) work correctly.
        * a sad option is that if a cluster bomb is aimed at the corner of the map etc, the
          model should be able to apply the effect to only the cells which exist, and not cause
          a NullPointer exception.
* Check how customizable of weapons are, and that editing specifications doesn't break their
  effects.
  * Will have custom WeaponError to make testing and error handling easier.

* Check that actor objects move correctly
    * Check that model doesn't crash if a ships' path leads it out of map bounds( the ship should
      stop at the edge)
    * Will support custom errors to make testing easier. 

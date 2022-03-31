/**
 * WinCondition API analyzes the board state for every player in order to check/determine if a player has
 * won or lost the current game. Determining win states in WinConditions will be defined by a lambda function.
 * This class is intended to be an internal API to the Controller to help determine winners using controller
 * parameters

public interface WinCondition {

  /**
   * updateWinner method sends the WinCondition's lambda to the provided collection of players in order
   * to update their win state
   * @param activePlayers List of Active players to apply the WinCondition's win lambda to

  public void updateWinner(Collection<Player> activePlayers);

  /**
   * This method is used to get the functional lambda that defines how the WinCondition determines if a Player wins or not
   * @return Function lambda that takes in the old PlayerRecord as a parameter and returns an updated PlayerRecord

  public Function<PlayerRecord, PlayerRecord> getWinLambda();
}
*/
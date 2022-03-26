/**
 * Whether an AI or an actual player, participants in a game have fundamental moves they can make.
 * In order to close the gap between human players and AI players and to conveniently store the
 * actions that a player can take, the PlayerAPI bundles the methods essential to a participant in
 * a turn-based grid game
 * @author Matt Knox, Matthew Giglio
 */
public interface PlayerAPI {

    /**
     * An essential part of being a participant in a game is to be able to make a move, so the
     * PlayerAPI needs to have a method that invokes the player's opportunity to play their turn in
     * whatever game they are playing
     */
    public void playTurn();

    /**
     * Players are able to make purchases from in-game shop, and items are added to their inventory
     * @param amount cost of item
     */
    public void makePurchase(int amount);


    /**
     * Players need to place their pieces at the start of the game and might be able to move them
     * during the game, so it is intrinsic to the API for players to be able to place/move pieces
     * @param s piece to be placed
     * @param start starting coordinate of boat
     * @param end ending coordinate of boat
     */
    public void placePiece(Piece s, Coordinate start, Coordinate end);

    /**
     * Players have currency they can spend, and because certain gameplay rewards them with coins,
     * there must be a method to increment their total amount
     * @param amount number of coins to be added to player's account
     */
    public void addGold(int amount);

}

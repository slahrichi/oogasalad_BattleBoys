package oogasalad.model.players;

import java.util.Map;
import java.util.function.Function;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.Item;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;

/**
 * Whether an AI or an actual player, participants in a game have fundamental moves they can make.
 * In order to close the gap between human players and AI players and to conveniently store the
 * actions that a player can take, the PlayerAPI bundles the methods essential to a participant in
 * a turn-based grid game
 * @author Matt Knox, Matthew Giglio
*/

public interface Player {

    /**
     * An essential part of being a participant in a game is to be able to make a move, so the
     * PlayerAPI needs to have a method that invokes the player's opportunity to play their turn in
     * whatever game they are playing
     */
    public void playTurn();

    /**
     * Players are able to make purchases from in-game shop, and items are added to their inventory
     * @param amount cost of item
     * @param item item to be added
      */
    public void makePurchase(int amount, Item item);


    /**
     * Players need to place their pieces at the start of the game and might be able to move them
     * during the game, so it is intrinsic to the API for players to be able to place/move pieces
     * @param s piece to be placed
     * @param coordinate
     * @return
     */

    public boolean placePiece(Piece s, Coordinate coordinate);

    /**
     * Players have currency they can spend, and because certain gameplay rewards them with coins,
     * there must be a method to increment their total amount
     * @param amount number of coins to be added to player's account
    */
    public void addGold(int amount);

    public void updateEnemyBoard(Coordinate c, int id, CellState state);

    public int getHealth();


    public int applyWinCondition(Function<PlayerRecord,Integer> lambda);

    public void setupBoard(int rows, int cols);

    public void determineHealth();

    public Board getBoard();

    public int getID();

    boolean canBeStruck(Coordinate c);

    Map<Integer, Board> getEnemyMap();
}


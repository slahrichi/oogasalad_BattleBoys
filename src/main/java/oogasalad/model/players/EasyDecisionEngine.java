package oogasalad.model.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.Piece;

/**
 * An AI for users of beginner difficulty to play against. The AI randomly selects both points to
 * attack and enemies to attack. The only intuition that it has is to hit the same point repeatedly
 * if it caused damage on the last shot but has not sunken the cell
 *
 * @author Matthew Giglio
 */
public class EasyDecisionEngine extends DecisionEngine {

  /**
   *
   * @param coordinateList list of coordinates from which the AI can choose for each enemy
   * @param enemyMap map relating each enemy to a board of moves the AI has made against them
   * @param player Player to which the DecisionEngine belongs
   */
  public EasyDecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap,
      Player player) {
    super(coordinateList, enemyMap, player);
  }

  /**
   * Decision logic for engine to make move against another player
   * @return EngineRecord containing the AI's selected shot location and enemy ID
   */
  public EngineRecord makeMove() {
    if (!getDeque().isEmpty()) {
      return getDeque().pollFirst();
    }
    else {
      int id = determineEnemy();
      List<Coordinate> list = getCoordinateMap().get(id);
      Coordinate location = determineLocation(list);
      EngineRecord shot = new EngineRecord(location, id);
      setLastShot(shot);
      return shot;
    }
  }

  private int determineEnemy() {
    List<Integer> enemies = new ArrayList<>(getEnemyMap().keySet());
    return enemies.get(getRandom().nextInt(enemies.size()));
  }

  private Coordinate determineLocation(List<Coordinate> coordinateList) {
    return coordinateList.get(getRandom().nextInt(coordinateList.size()));
  }

  /**
   * Method to adjust strategy of engine given result of last shot
   * @param result Result of engine's last shot
   */
  public void adjustStrategy(CellState result) {
    if (canBeRemoved(result)) {
      getCoordinateList().remove(getLastShot());
    }
    else {
      getDeque().addFirst(getLastShot());
    }
  }

  /**
   * Method called during GameSetup that allows the AI to place their pieces
   *
   * @param pieceList pieces that the player is allowed to place
   * @return Coordinate that the AI has chosen to place their piece
   */
  public Coordinate placePiece(List<Piece> pieceList) {
    Board board = getPlayer().getBoard();
    Piece piece = pieceList.get(getPieceIndex());
    Coordinate c = determineLocation(getCoordinateList());
    while (!board.hasValidPlacement(c, piece)) {
      c = determineLocation(getCoordinateList());
    }
    updatePieceIndex();
    return c;
  }
}

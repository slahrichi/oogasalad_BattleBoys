package oogasalad.model.players;

import java.util.List;
import java.util.Map;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.enums.CellState;

public class HardDecisionEngine extends DecisionEngine {

  public HardDecisionEngine(List<Coordinate> coordinateList, Map<Integer, MarkerBoard> enemyMap,
  Player player) {
    super(coordinateList, enemyMap, player);
  }

  @Override
  public EngineRecord makeMove() {
    return null;
  }

  @Override
  public void adjustStrategy(CellState result) {

  }

  @Override
  public Coordinate placePiece(List<Piece> pieceList) {
    return null;
  }

  @Override
  public void resetStrategy() {

  }
}

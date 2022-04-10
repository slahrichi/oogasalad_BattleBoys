package oogasalad.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.Scene;
import oogasalad.GameData;
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MarkerBoard;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.WinConditions.WinCondition;
import oogasalad.model.utilities.WinConditions.WinState;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.Info;
import oogasalad.view.GameView;

public class GameManager extends PropertyObservable implements PropertyChangeListener {

  private List<Player> playerList;
  private List<WinCondition> winConditionsList;
  private Map<Integer, Player> idMap;
  private GameView view;
  //current player, separate from ID
  private int playerIndex;
  private int numShots;
  private int allowedShots;
  private int size;

  public GameManager(GameData data) {
    initialize(data);
    List<CellState[][]> boards = createFirstPlayerBoards(data);
    Collection<Collection<Coordinate>> coords = createInitialPieces(data.pieces());
    view = new GameView(boards);
    view.initializePiecesLeft(coords);
    winConditionsList = data.winConditions();
    this.view.addObserver(this);
  }

  private List<CellState[][]> createFirstPlayerBoards(GameData data) {
    List<CellState[][]> boards = new ArrayList<>();
    Player firstPlayer = data.players().get(0);
    boards.add(firstPlayer.getBoard().getCurrentBoardState());
    for (int i = 0; i < firstPlayer.getEnemyMap().size(); i++) {
      boards.add(makeCopy(data.board()));
    }
    return boards;
  }

  private CellState[][] makeCopy(CellState[][] board) {
    CellState[][] cellBoard = new CellState[board.length][board[0].length];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        cellBoard[i][j] = board[i][j];
      }
    }
    return cellBoard;
  }

  private Collection<Collection<Coordinate>> createInitialPieces(List<Piece> pieces) {
    Collection<Collection<Coordinate>> pieceCoords = new ArrayList<>();
    for (Piece piece : pieces) {
      pieceCoords.add(piece.getRelativeCoords());
    }
    return pieceCoords;
  }

  public Scene createScene() {
    return view.createScene();
  }

//  public void playGame() {
//    while (canStillPlay())
//    for (Player player : playerList) {
//      promptPlayerToPlayTurn();
//      player.playTurn();
//    }
//  }

  public void promptPlayerToPlayTurn() {
    view.promptPlayTurn();
  }


  private void initialize(GameData data) {
     this.playerList = data.players();
     playerIndex = 0;
     size = playerList.size();
     numShots = 0;
     allowedShots = 1;
     createIDMap();
  }

  private void createIDMap() {
    idMap = new HashMap<>();
    for (Player player : playerList) {
      idMap.put(player.getID(), player);
    }
  }


  private boolean canStillPlay() {
    return playerList.size() != 1;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    int id = ((Info)evt.getNewValue()).ID();
    int row = ((Info)evt.getNewValue()).row();
    int col = ((Info)evt.getNewValue()).col();
    if (makeShot(new Coordinate(row, col), id)) {
      updateConditions(id);
    };

  }

  private void updateConditions(int id) {
    applyWinConditions();
    if(idMap.containsKey(id)){
      List<Piece> piecesLeft = idMap.get(id).getBoard().listPieces();
      Collection<Collection<Coordinate>> coords = convertPiecesToCoords(piecesLeft);
      view.updatePiecesLeft(coords);
    }
    numShots++;
    checkIfMoveToNextToPlayer();
  }

  private Collection<Collection<Coordinate>> convertPiecesToCoords(List<Piece> piecesLeft) {
    Collection<Collection<Coordinate>> coords = new ArrayList<>();
    for (Piece piece : piecesLeft) {
      coords.add(piece.getRelativeCoords());
    }
    return coords;
  }

  private void checkIfGameOver() {
    if (!canStillPlay()) {
      //endGame
    }
  }

  private void checkIfMoveToNextToPlayer() {
    if (numShots == allowedShots) {
      playerIndex = (playerIndex + 1) % playerList.size();
      numShots = 0;
      sendUpdatedBoardsToView();
    }
  }

  private void checkIfPlayerHasBeenEliminated(int id) {
    //CANT USE YET BECAUSE getHealth() always returns 0

//    Player player = idMap.get(id);
//    if (player.getHealth() == 0) {
//      idMap.remove(id);
//      playerList.remove(player);
//      size = playerList.size();
//    }
  }

  public List<Player> getPlayerList() {
    return playerList;
  }

  private boolean makeShot(Coordinate c, int id) {
    Player currentPlayer = playerList.get(playerIndex);
    Player enemy = idMap.get(id);
    if (currentPlayer.getEnemyMap().get(id).canPlaceAt(c)) {
      CellState result = enemy.getBoard().hit(c);//get result from model people
      currentPlayer.updateEnemyBoard(c, id, result);
      view.displayShotAt(c.getRow(), c.getColumn(), result);
      //applyWinConditions();
      return true;
    }
    return false;
  }

  public void applyWinConditions() {
    for(WinCondition w: winConditionsList) {
      Set<Integer> ogKeyset = new HashSet<Integer>(idMap.keySet());
      for(int key: ogKeyset) {
        Player currPlayer = idMap.get(key);
        WinState currPlayerWinState = w.updateWinner(currPlayer);
        System.out.format("Player %d's WinState %s\n", key, currPlayerWinState);
        if(currPlayerWinState.equals(WinState.LOSE)) {
          System.out.println("player "+ key + "lost");
          playerList.remove(currPlayer);
          idMap.remove(key);
          for(Player p: playerList) {
            p.getEnemyMap().remove(key);
          }
          //show dead player
        }
        else if(currPlayerWinState.equals(WinState.WIN)) {
          System.out.format("Player %d wins!", key);
        }
      }
    }

    if(playerList.size()==1) {
      //remaining player wins;
    }
  }

  private void sendUpdatedBoardsToView() {
    List<CellState[][]> boardList = new ArrayList<>();
    List<Integer> idList = new ArrayList<>();
    List<Collection<Collection<Coordinate>>> pieceList = new ArrayList<>();
    Player currentPlayer = playerList.get(playerIndex);
    boardList.add(currentPlayer.getBoard().getCurrentBoardState());
    idList.add(currentPlayer.getID());
    pieceList.add(convertPiecesToCoords(currentPlayer.getBoard().listPieces()));
    for (int id : currentPlayer.getEnemyMap().keySet()) {
      MarkerBoard board = currentPlayer.getEnemyMap().get(id);
      boardList.add(board.getMarkerBoard());
      idList.add(id);
      pieceList.add(convertPiecesToCoords(idMap.get(id).getBoard().listPieces()));
    }
    view.moveToNextPlayer(boardList, idList, pieceList);
  }
}

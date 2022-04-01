package oogasalad.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import oogasalad.PropertyObservable;
import oogasalad.model.players.Player;
import oogasalad.view.board.BoardView;
import oogasalad.view.board.GameBoardView;
import oogasalad.view.board.ShapeType;

public class View extends PropertyObservable implements PropertyChangeListener {

  private static final double SCREEN_WIDTH = 1200;
  private static final double SCREEN_HEIGHT = 800;

  private List<BoardView> myBoards;
  private BorderPane myPane;
  private StackPane myCenterPane;
  private Scene myScene;

  private int currentBoardIndex;

  public View() {
    myPane = new BorderPane();
    myCenterPane = new StackPane();
    myPane.setCenter(myCenterPane);

    myBoards = new ArrayList<>();
    createBoards(3);
    currentBoardIndex = 0;
    updateDisplayedBoard();
  }

  public Scene createViewFromPlayers(List<Player> playerList) {
    myScene = new Scene(myPane, SCREEN_WIDTH, SCREEN_HEIGHT);
    myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    return myScene;
  }

  private void handleKeyInput(KeyCode code) {
    if(code == KeyCode.LEFT) {
      currentBoardIndex = (currentBoardIndex + myBoards.size() - 1) % myBoards.size();
    } else if (code == KeyCode.RIGHT) {
      currentBoardIndex = (currentBoardIndex + myBoards.size() + 1) % myBoards.size();
    }
    System.out.println("Showing board " + currentBoardIndex);
    updateDisplayedBoard();
  }

  // Displays the board indicated by the updated value of currentBoardIndex
  private void updateDisplayedBoard() {
    StackPane boardToDisplay = myBoards.get(currentBoardIndex).getBoardPane();
    myCenterPane.getChildren().clear();
    myCenterPane.getChildren().add(boardToDisplay);
  }

  private void createBoards(int numBoards) {
    int[][] arrayLayout = new int[][]{{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    for (int i = 0; i < numBoards-1; i++) {
      GameBoardView board = new GameBoardView(new ShapeType(), arrayLayout, i);
      board.addObserver(this);
      myBoards.add(board);
    }
    // create the last board with a different array layout
    GameBoardView board2 = new GameBoardView(new ShapeType(), new int[][]{{0, 1, 0}, {1, 1, 1}, {0, 1, 0}}, 2);
    board2.addObserver(this);
    myBoards.add(board2);

    GameBoardView board3 = new GameBoardView(new ShapeType(), new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 1, 1}}, 3);
    board3.addObserver(this);
    myBoards.add(board3);

    GameBoardView board4 = new GameBoardView(new ShapeType(), new int[][]{{1, 1, 1, 1}, {1, 0, 0, 1}, {1, 0, 0, 1}, {1, 1, 1, 1}, {1, 1, 1, 1}}, 4);
    board4.addObserver(this);
    myBoards.add(board4);
  }

  public void promptPlayTurn() {
    System.out.println("Please play turn! Maybe we should show a player ID here!");
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    notifyObserver(evt.getPropertyName(), evt.getNewValue());
  }
}

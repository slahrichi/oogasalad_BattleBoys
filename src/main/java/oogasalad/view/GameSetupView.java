package oogasalad.view;

import java.awt.Color;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.control.Cell;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javax.swing.border.Border;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.view.board.BoardView;
import oogasalad.view.board.SelfBoardView;

public class GameSetupView extends Application{

  public GameSetupView(){

  }


  @Override
  public void start(Stage stage) throws Exception {
    Stage nstage= new Stage();
    ColorSelectionStage b =new ColorSelectionStage();
    //BoardSetUpStage bb = new BoardSetUpStage();
  }
}
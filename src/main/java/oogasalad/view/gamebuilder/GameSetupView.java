package oogasalad.view.gamebuilder;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameSetupView extends Application{

  public GameSetupView(){

  }


  @Override
  public void start(Stage stage) throws Exception {
    Stage nstage= new Stage();
    ColorSelectionStage b =new ColorSelectionStage();
    BoardSetUpStage bb = new BoardSetUpStage();
  }
}
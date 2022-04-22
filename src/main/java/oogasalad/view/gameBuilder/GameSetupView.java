package oogasalad.view.gameBuilder;

import java.lang.reflect.Method;
import java.util.List;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import oogasalad.model.utilities.Usables.Weapons.BasicShot;

public class GameSetupView extends Application{

  public GameSetupView(){

  }


  @Override
  public void start(Stage stage) throws Exception {
    Stage nstage= new Stage();
    //PieceDesignStage wds =new PieceDesignStage();
    WeaponDesignStage wds = new WeaponDesignStage();
    //PlayerSetupStage wds = new PlayerSetupStage();
    //ColorSelectionStage b =new ColorSelectionStage();
    //Class<?> clazz = Class.forName("oogasalad.view.gameBuilder.ColorSelectionStage");
    //clazz.cast(b);
    //Method testi = clazz.getDeclaredMethod("launch");
    //System.out.println (clazz.getCanonicalName());
    //System.out.println (testi.getDeclaringClass().getCanonicalName());
    //testi.invoke(clazz);
    Object t =wds.launch();
    System.out.println(t.getClass());
    //BoardSetUpStage bb = new BoardSetUpStage();
  }
}
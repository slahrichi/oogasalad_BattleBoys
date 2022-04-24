package oogasalad.view.gamebuilder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import oogasalad.model.utilities.winconditions.WinCondition;

public class GameSetupView extends Application{

  public GameSetupView(){

  }


  @Override
  public void start(Stage stage) throws Exception {
    List<Object> objectList=new ArrayList<>();


    GameVarSetupStage gameVarSetupStage = new GameVarSetupStage();
    objectList.add(gameVarSetupStage.launch());

    WinConditionSetupStage winConditionSetupStage = new WinConditionSetupStage();
    objectList.add(winConditionSetupStage.launch());

    ColorSelectionStage colorSelectionStage = new ColorSelectionStage();
    objectList.add(colorSelectionStage);

    PlayerSetupStage playerSetupStage=new PlayerSetupStage();
    objectList.add(playerSetupStage);

    BoardSetUpStage boardSetUpStage = new BoardSetUpStage();
    objectList.add(boardSetUpStage.launch());

    PieceDesignStage pieceDesignStage =new PieceDesignStage();
    objectList.add(pieceDesignStage.launch());

    WeaponDesignStage weaponDesignStage=new WeaponDesignStage();
    objectList.add(weaponDesignStage.launch());

    InventorySetupStage inventorySetupStage = new InventorySetupStage(weaponDesignStage.getCreatedWeaponIds());
    objectList.add(inventorySetupStage.launch());

    ItemDesignStage itemDesignStage = new ItemDesignStage();
    objectList.add(itemDesignStage.launch());

    SpecialIslandDesigner specialIslandDesigner= new SpecialIslandDesigner();
    objectList.add(specialIslandDesigner.launch());

  }
}
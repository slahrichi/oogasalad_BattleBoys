package oogasalad.view.gamebuilder;


import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.stage.Stage;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.items.Item;
import oogasalad.model.utilities.usables.weapons.Weapon;

public class GameSetupView extends Application{

  public GameSetupView(){

  }


  @Override
  public void start(Stage stage) throws Exception {
    List<Object> objectList=new ArrayList<>();
    List<Class<?>> paramTypeList;
    GameBuilderUtil builderUtil = new GameBuilderUtil();

    PlayerSetupStage playerSetupStage=new PlayerSetupStage();
    objectList.add(playerSetupStage.launch());


    PieceDesignStage pieceDesignStage =new PieceDesignStage();
    objectList.add(pieceDesignStage.launch());

    BoardSetUpStage boardSetUpStage = new BoardSetUpStage();
    objectList.add(boardSetUpStage.launch());


    objectList.add(playerSetupStage.getEngineList());

    WinConditionSetupStage winConditionSetupStage = new WinConditionSetupStage();
    objectList.add(winConditionSetupStage.launch());

    ColorSelectionStage colorSelectionStage = new ColorSelectionStage();
    objectList.add(colorSelectionStage.launch());

    WeaponDesignStage weaponDesignStage=new WeaponDesignStage();
    List<Weapon> weapons = (List<Weapon>) weaponDesignStage.launch();
    objectList.add(weapons);

    SpecialIslandDesigner specialIslandDesigner= new SpecialIslandDesigner();
    objectList.add(specialIslandDesigner.launch());

    ItemDesignStage itemDesignStage = new ItemDesignStage();
    List<Item> items = (List<Item>) itemDesignStage.launch();
    objectList.add(items);

    InventorySetupStage inventorySetupStage = new InventorySetupStage(weaponDesignStage.getCreatedWeaponIds());
    objectList.add(inventorySetupStage.launch());

    //allUsables
    List<Usable> allUsableList = new ArrayList<>(weapons);
    allUsableList.addAll(items);
    objectList.add(allUsableList);

    GameVarSetupStage gameVarSetupStage = new GameVarSetupStage();
    objectList.add(gameVarSetupStage.launch());

    paramTypeList = getParamTypes(objectList);

  }

  private List<Class<?>> getParamTypes(List<Object> objects){
    List<Class<?>> types=new ArrayList<>();
    for(Object object : objects){
      types.add(object.getClass());
    }

    return  types;
  }



}
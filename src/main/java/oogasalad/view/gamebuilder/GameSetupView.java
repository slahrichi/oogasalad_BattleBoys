package oogasalad.view.gamebuilder;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.stage.Stage;
import oogasalad.model.parsing.Parser;
import oogasalad.model.parsing.ParserData;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.items.Item;
import oogasalad.model.utilities.usables.weapons.Weapon;

/**
 * The manager class which launches all design stages, takes in the result objects, creates a
 * parser-data record, which is then passed to a parser, so it can be written to a file.
 * Assumes that at least one instance of all objects has been created, due to conceptual limitations,
 * this is might not be 100% open closed.
 *
 * @author Luka Mdivani
 */
public class GameSetupView extends Application {

  public GameSetupView() {

  }

  private List<Object> objectList;
  private String fileName;
  private String CLASS_PATH = "oogasalad.model.parsing.ParserData";

  @Override
  public void start(Stage stage) throws Exception {
    objectList = new ArrayList<>();
    List<Class<?>> paramTypeList;
    GameBuilderUtil builderUtil = new GameBuilderUtil();

    FileNameSelectionStage fileNameSelectionStage = new FileNameSelectionStage();
    fileName = (String) fileNameSelectionStage.launch();

    PlayerSetupStage playerSetupStage = new PlayerSetupStage();
    objectList.add(playerSetupStage.launch());

    PieceDesignStage pieceDesignStage = new PieceDesignStage();
    objectList.add(pieceDesignStage.launch());

    BoardSetUpStage boardSetUpStage = new BoardSetUpStage();
    objectList.add(boardSetUpStage.launch());

    objectList.add(playerSetupStage.getEngineList());

    WinConditionSetupStage winConditionSetupStage = new WinConditionSetupStage();
    objectList.add(winConditionSetupStage.launch());

    ColorSelectionStage colorSelectionStage = new ColorSelectionStage();
    objectList.add(colorSelectionStage.launch());

    WeaponDesignStage weaponDesignStage = new WeaponDesignStage();
    List<Weapon> weapons = (List<Weapon>) weaponDesignStage.launch();

    SpecialIslandDesigner specialIslandDesigner = new SpecialIslandDesigner();
    objectList.add(specialIslandDesigner.launch());

    ItemDesignStage itemDesignStage = new ItemDesignStage();
    List<Item> items = (List<Item>) itemDesignStage.launch();

    InventorySetupStage inventorySetupStage = new InventorySetupStage(
        weaponDesignStage.getCreatedWeaponIds());
    objectList.add(inventorySetupStage.launch());

    List<Usable> allUsableList = new ArrayList<>(weapons);
    allUsableList.addAll(items);
    objectList.add(allUsableList);

    GameVarSetupStage gameVarSetupStage = new GameVarSetupStage();
    Map<String, Integer> gameVarMap = (Map<String, Integer>) gameVarSetupStage.launch();
    for (String key : gameVarMap.keySet()) {
      objectList.add(gameVarMap.get(key));
    }

    paramTypeList = getParamTypes(objectList);

    Object[] parameters = new Object[objectList.size()];
    objectList.toArray(parameters);

    Class<?>[] parameterTypes = new Class<?>[paramTypeList.size()];
    paramTypeList.toArray(parameterTypes);
    ParserData userSelections = (ParserData) builderUtil.createInstance(CLASS_PATH, parameterTypes,
        parameters);

    Parser parser = new Parser();
    parser.save(userSelections, fileName);
  }

  private List<Class<?>> getParamTypes(List<Object> objects) {
    List<Class<?>> types = new ArrayList<>();
    for (Object object : objects) {
      if (object instanceof ArrayList<?>) {
        types.add(List.class);
      } else if (object instanceof HashMap<?, ?>) {
        types.add(Map.class);
      } else {
        types.add(object.getClass());
      }

    }
    return types;
  }


}
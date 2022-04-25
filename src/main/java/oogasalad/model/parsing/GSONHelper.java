package oogasalad.model.parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import oogasalad.model.utilities.Board;
import oogasalad.model.utilities.Coordinate;
import oogasalad.model.utilities.MovingPiece;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.StaticPiece;
import oogasalad.model.utilities.tiles.Cell;
import oogasalad.model.utilities.tiles.IslandCell;
import oogasalad.model.utilities.tiles.Modifiers.Burner;
import oogasalad.model.utilities.tiles.Modifiers.FrontEndUpdater;
import oogasalad.model.utilities.tiles.Modifiers.GoldAdder;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import oogasalad.model.utilities.tiles.Modifiers.ShotAdder;
import oogasalad.model.utilities.tiles.ShipCell;
import oogasalad.model.utilities.tiles.WaterCell;
import oogasalad.model.utilities.tiles.enums.CellState;
import oogasalad.model.utilities.usables.Usable;
import oogasalad.model.utilities.usables.UsableFunction;
import oogasalad.model.utilities.usables.items.CircleHeal;
import oogasalad.model.utilities.usables.items.ShipHeal;
import oogasalad.model.utilities.usables.weapons.BasicShot;
import oogasalad.model.utilities.usables.weapons.BlastZoneShot;
import oogasalad.model.utilities.usables.weapons.BurnShot;
import oogasalad.model.utilities.usables.weapons.ClusterShot;
import oogasalad.model.utilities.usables.weapons.EmpoweredShot;
import oogasalad.model.utilities.usables.weapons.HomingShot;
import oogasalad.model.utilities.usables.weapons.LineShot;
import oogasalad.model.utilities.usables.weapons.MoltovShot;
import oogasalad.model.utilities.usables.weapons.RandomShot;
import oogasalad.model.utilities.usables.weapons.SonarShot;
import oogasalad.model.utilities.usables.weapons.Weapon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class GSONHelper<T> implements
    JsonSerializer<T>, JsonDeserializer<T>, InstanceCreator<T> {
  private final String TYPE = "type";
  private final String PROPERTIES = "properties";
  private final String UNKNOWN_TYPE = "Unknown element type: ";
  private static final Logger LOG = LogManager.getLogger(GSONHelper.class);



  @Override
  public JsonElement serialize(T src, Type type,
      JsonSerializationContext context) {
    LOG.info("serializing custom element");
    LOG.info("class is {}", src.getClass());
    JsonObject result = new JsonObject();
    result.add(TYPE, new JsonPrimitive(src.getClass().getName()));

    Gson gson = new GsonBuilder().create();
    if( //Piece
            src.getClass().equals(StaticPiece.class) ||
            src.getClass().equals(MovingPiece.class)) {
      LOG.info("we have identified piece");
      gson = new GsonBuilder().registerTypeHierarchyAdapter(Modifiers.class, new GSONHelper()).create();
    } else if ( //Modifier
        src.getClass().equals(Burner.class) ||
            src.getClass().equals(FrontEndUpdater.class) ||
            src.getClass().equals(GoldAdder.class) ||
            src.getClass().equals(ShotAdder.class)) {
      LOG.info("we have found modifier");
      gson = new GsonBuilder().create();
    }

    if(src.getClass().equals(BasicShot.class) ||
        src.getClass().equals(BlastZoneShot.class) ||
        src.getClass().equals(BurnShot.class) ||
        src.getClass().equals(CircleHeal.class) ||
        src.getClass().equals(ClusterShot.class) ||
        src.getClass().equals(EmpoweredShot.class) ||
        src.getClass().equals(HomingShot.class) ||
        src.getClass().equals(LineShot.class) ||
        src.getClass().equals(MoltovShot.class) ||
        src.getClass().equals(RandomShot.class) ||
        src.getClass().equals(ShipHeal.class) ||
        src.getClass().equals(SonarShot.class)
    ) {
      LOG.info("we have found Usable");
      gson = new GsonBuilder().registerTypeAdapter(Usable.class, new GSONHelper()).create();
    }

    JsonElement jelement = gson.toJsonTree(src);
    result.add(PROPERTIES, jelement);
    return result;
  }

  @Override
  public T deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {


    JsonObject jsonObject = json.getAsJsonObject();
    String type = jsonObject.get(TYPE).getAsString();

    LOG.info("deserializing custom element of type {}", type);
    JsonElement element = jsonObject.get(PROPERTIES);


    try {
      return context.deserialize(element, Class.forName(type));
    } catch (ClassNotFoundException cnfe) {
      throw new JsonParseException( UNKNOWN_TYPE + type, cnfe);
    }

  }

  @Override
  public T createInstance(Type type) {
    return null;
  }
}

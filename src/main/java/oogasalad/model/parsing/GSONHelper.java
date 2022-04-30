package oogasalad.model.parsing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import oogasalad.model.utilities.Piece;
import oogasalad.model.utilities.tiles.Modifiers.Modifiers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class defines a way to use the GSON library in a much more helpful way.
 * The GSON library does not natively handle abstract classes,
 * but allows users to design their own handling of abstract classes.
 * This is done by registering various Type adapters.
 *
 * @param <T> The class of a particular element to parse
 */
public class GSONHelper<T> implements
    JsonSerializer<T>, JsonDeserializer<T> {
  private final String TYPE = "type";
  private final String PROPERTIES = "properties";
  private final String UNKNOWN_TYPE = "Unknown element type: ";
  private static final Logger LOG = LogManager.getLogger(GSONHelper.class);


  /**
   * Serializes a custom elemeent
   *
   * @param src: the abstract class to parse
   * @param type: the type of abstract class
   * @param context: the default serialization context
   * @return: a JsonElement corresponding to the given abstract class
   */
  @Override
  public JsonElement serialize(T src, Type type,
      JsonSerializationContext context) {

    Class<?> sourceClass = src.getClass();
    LOG.info("serializing custom element of class {}", sourceClass);
    JsonObject result = new JsonObject();
    result.add(TYPE, new JsonPrimitive(sourceClass.getName()));

    Gson gson = new GsonBuilder().create();

    if(Piece.class.isAssignableFrom(sourceClass)) {
      LOG.info("Parsing class of type Piece");
      gson = new GsonBuilder().registerTypeHierarchyAdapter(Modifiers.class, new GSONHelper<Piece>()).create();
    }

    JsonElement jelement = gson.toJsonTree(src);
    result.add(PROPERTIES, jelement);
    return result;
  }

  /**
   * Deserializes a custom element
   *
   * @param json: the json element to parsee
   * @param typeOfT: the abstract class this json corresponds to
   * @param context: the default deserialization context
   * @return: an instance of the abstract class
   * @throws JsonParseException: if there are errors in parsing
   */
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
}

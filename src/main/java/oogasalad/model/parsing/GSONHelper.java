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

public class GSONHelper<T> implements
    JsonSerializer<T>, JsonDeserializer<T> {

  @Override
  public JsonElement serialize(T src, Type type,
      JsonSerializationContext context) {

    JsonObject result = new JsonObject();
    result.add("type", new JsonPrimitive(src.getClass().getName()));

    Gson gson = new GsonBuilder().create();
    JsonElement jelement = gson.toJsonTree(src);
    result.add("properties", jelement);

    return result;

  }

  @Override
  public T deserialize(JsonElement json, Type typeOfT,
      JsonDeserializationContext context) throws JsonParseException {

    JsonObject jsonObject = json.getAsJsonObject();
    String type = jsonObject.get("type").getAsString();
    JsonElement element = jsonObject.get("properties");

    try {
      return context.deserialize(element, Class.forName(type));
    } catch (ClassNotFoundException cnfe) {
      throw new JsonParseException("Unknown element type: " + type, cnfe);
    }

  }
}

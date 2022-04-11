package oogasalad;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import oogasalad.model.utilities.Piece;

public class GSONHelper implements
    JsonSerializer<Piece>, JsonDeserializer<Piece> {

  @Override
  public JsonElement serialize(Piece src, Type type,
      JsonSerializationContext context) {

    JsonObject result = new JsonObject();
    result.add("type", new JsonPrimitive(src.getClass().getName()));
    Gson gson = new GsonBuilder().create();
    JsonElement jelement = gson.toJsonTree(src);
    //String myResult = gson.toJson(src);
    //JsonObject o = JsonParser.parseString(myResult).getAsJsonObject();
    //JsonElement jelement = new JsonParser().parse(String.valueOf(o));
    result.add("properties", jelement);

    return result;

  }

  @Override
  public Object deserialize(JsonElement json, Type typeOfT,
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

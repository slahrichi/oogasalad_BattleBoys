package oogasalad.view.gamebuilder;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class GameBuilderUtil {


  protected Object createInstance(String className, Class<?>[] parameterTypes, Object[] parameters)
      throws IOException {

    try {
      Class<?> clazz = Class.forName(className);
      Constructor<?> constructor = clazz.getConstructor(parameterTypes);
      return constructor.newInstance(parameters);
    } catch (Error | Exception e) {
      e.printStackTrace();
      throw new IOException(String.format("Class parsing failed: %s className"));
    }

  }
}

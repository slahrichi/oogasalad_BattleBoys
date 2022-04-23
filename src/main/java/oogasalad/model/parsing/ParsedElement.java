package oogasalad.model.parsing;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;
import oogasalad.model.utilities.winconditions.WinCondition;

public abstract class ParsedElement {

  Properties exceptionMessageProperties;
  private final String MISSING_DATA =  "missingData";
  private final String MISSING_FILE = "missingFile";
  private final String EXCEPTIONS_PATH = "src/main/resources/ParserExceptions.properties";

  public ParsedElement() {
    exceptionMessageProperties = new Properties();
    try {
      InputStream is = new FileInputStream(EXCEPTIONS_PATH);
      exceptionMessageProperties.load(is);
      is.close();
    } catch (IOException ignored) {
    }
  }

  Object getElementFromJson(String File, Gson gson, Class myClass) throws ParserException {
    Object element;
    try{
      element = gson.fromJson(new FileReader(File), myClass);
    }
    catch(FileNotFoundException e){
      throw new ParserException(exceptionMessageProperties.getProperty(MISSING_FILE));
    }
    return element;
  }

  List getParsedObject(String parsedObjectFile, Gson gson,
      Type listOfMyClassObject, String missingClass) throws ParserException {
    List<?> ret = null;
    //List<?> ret= null; not sure which one makes more sense
    try {
      ret = gson.fromJson(new FileReader(parsedObjectFile), listOfMyClassObject);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    if (ret == null) {
      throw new ParserException(exceptionMessageProperties.getProperty(MISSING_DATA).formatted(
          parsedObjectFile,missingClass));
    }
    return ret;
  }

  void putJsonInProp(Properties props, String location, Object o, Gson gson, String key) {
    String json = gson.toJson(o);
    File myNewFile = new File(location);
    try {
      FileWriter myWriter = new FileWriter(myNewFile);
      myWriter.write(json);
      myWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    props.put(key, myNewFile.toString());
  }
  public abstract void save(Properties props, String location, Object o) throws ParserException;
  public abstract Object parse(Properties props) throws ParserException;
  public abstract Class getParsedClass();
}

package oogasalad.model.parsing;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class ParsedElement {

  Properties exceptionMessageProperties;

  public ParsedElement() {
    exceptionMessageProperties = new Properties();
    try {
      InputStream is = new FileInputStream("src/main/resources/ParserExceptions.properties");
      exceptionMessageProperties.load(is);
      is.close();
    } catch (IOException ignored) {
    }
  }

  public abstract void save(Properties props, String location, Object o) throws ParserException;
  public abstract Object parse(Properties props) throws ParserException;
  public abstract Class getParsedClass();
}

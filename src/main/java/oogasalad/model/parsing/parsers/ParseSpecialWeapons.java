package oogasalad.model.parsing.parsers;

import java.util.Properties;
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseSpecialWeapons extends ParsedElement {

  private static final Logger LOG = LogManager.getLogger(ParseSpecialWeapons.class);
  //TODO: log this class

  @Override
  public void save(Properties props, String location, Object o) throws ParserException {

  }

  @Override
  public Object parse(Properties props) throws ParserException {
    return null;
  }

  @Override
  public Class getParsedClass() {
    return null;
  }
}

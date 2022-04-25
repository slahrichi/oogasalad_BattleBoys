package oogasalad.model.parsing.parsers;

import java.util.Properties;
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseGold extends ParsedElement {

  private final String PROPERTIES_GOLD = "Gold";
  private static final Logger LOG = LogManager.getLogger(ParseGold.class);


  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    LOG.info("saving gold at {}", location);
    Integer gold = (Integer) o;
    props.put(PROPERTIES_GOLD, gold.toString());
  }

  @Override
  public Integer parse(Properties props) throws ParserException {
    LOG.info("parsing gold per turn");
    Integer gold = Integer.parseInt(props.getProperty(PROPERTIES_GOLD));
    return gold;
  }

  @Override
  public Class getParsedClass() {
    return Integer.class;
  }
}

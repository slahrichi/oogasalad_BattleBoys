package oogasalad.model.parsing.parsers;

import java.util.Properties;
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseShotsPerTurn extends ParsedElement {

  private final String PROPERTIES_SHOTSPERTURN = "ShotsPerTurn";
  private static final Logger LOG = LogManager.getLogger(ParseShotsPerTurn.class);


  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
  LOG.info("saving shots per turn at {}", location);
  Integer shotsPerTurn = (Integer) o;
  props.put(PROPERTIES_SHOTSPERTURN, shotsPerTurn);
  }

  @Override
  public Integer parse(Properties props) throws ParserException {
    LOG.info("parsing shots per turn");
    Integer shotsPerTurn = Integer.parseInt(props.getProperty(PROPERTIES_SHOTSPERTURN));
    return shotsPerTurn;
  }

  @Override
  public Class getParsedClass() {
    return Integer.class;
  }
}

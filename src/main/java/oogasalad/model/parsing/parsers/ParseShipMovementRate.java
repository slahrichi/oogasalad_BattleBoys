package oogasalad.model.parsing.parsers;

import java.util.Properties;
import oogasalad.model.parsing.ParsedElement;
import oogasalad.model.parsing.ParserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ParseShipMovementRate extends ParsedElement {

  private final String PROPERTIES_SHIPMOVEMENTRATE = "ShipMovementRate";
  private static final Logger LOG = LogManager.getLogger(ParseShipMovementRate.class);


  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    LOG.info("saving ship movement rate at {}", location);
    Integer shipMovementRate = (Integer) o;
    props.put(PROPERTIES_SHIPMOVEMENTRATE, shipMovementRate.toString());
  }

  @Override
  public Integer parse(Properties props) throws ParserException {
    LOG.info("parsing ship movement rate");
    Integer shipMovementRate = Integer.parseInt(props.getProperty(PROPERTIES_SHIPMOVEMENTRATE));
    return shipMovementRate;
  }

  @Override
  public Class getParsedClass() {
    return Integer.class;
  }
}

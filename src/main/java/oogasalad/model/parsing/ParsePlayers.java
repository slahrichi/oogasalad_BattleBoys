package oogasalad.model.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParsePlayers extends ParsedElement{
  private final String PROPERTIES_PLAYER_LIST = "Players";
  private final String SPACE = " ";
  private final String PATH = "oogasalad.model.players.";
  private final String BAD_PLAYER = "badPlayer";



  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    List<String> players = (List<String>) o;
    props.put(PROPERTIES_PLAYER_LIST, String.join(" ", players));
  }

  @Override
  public Object parse(Properties props) throws ParserException {
    String[] playersData = props.getProperty(PROPERTIES_PLAYER_LIST).split(SPACE);
    for(String player: playersData) {
      try {
        Class.forName(PATH+ player);
      } catch (ClassNotFoundException e) {
        throw new ParserException(exceptionMessageProperties.getProperty(BAD_PLAYER).formatted(player));
      }
    }
    return new ArrayList<>(Arrays.asList(playersData));
  }

  @Override
  public Class getParsedClass() {
    return List.class;
  }
}

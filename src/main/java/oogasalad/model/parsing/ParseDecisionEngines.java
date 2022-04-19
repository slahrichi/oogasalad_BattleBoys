package oogasalad.model.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ParseDecisionEngines extends ParsedElement{
  private final String PROPERTIES_DECISION_ENGINES_LIST = "DecisionEngines";
  private final String PROPERTIES_PLAYER_LIST = "Players";
  private final String SPACE = " ";
  private final String HUMAN_PLAYER_AI_ENGINE = "HumanPlayerAIEngine";
  private final String PLAYER_PATH = "oogasalad.model.players.";
  private final String DECISION_ENGINE = "DecisionEngine";
  private final String MISALIGNED_ENGINES = "misalignedEngines";
  private final String HUMAN_PLAYER = "HumanPlayer";
  private final String AI_PLAYER = "AIPlayer";
  private final String HUMAN_DECISION_ENGINE = "None";
  private final String AIPLAYER_BAD_ENGINE = "AIPlayerBadEngine";

  @Override
  public void save(Properties props, String location, Object o) throws ParserException {
    List<String> decisionEngines = (List<String>) o;
    props.put(PROPERTIES_DECISION_ENGINES_LIST, String.join(" ", decisionEngines));
  }

  @Override
  public Object parse(Properties props) throws ParserException {
    List<String> decisionEngines = List.of(
        props.getProperty(PROPERTIES_DECISION_ENGINES_LIST).split(SPACE));
    decisionEngines = new ArrayList<>(decisionEngines);
    List<String> players = List.of(props.getProperty(PROPERTIES_PLAYER_LIST).split(SPACE));
    if(decisionEngines.size() != players.size()) {
      throw new ParserException(exceptionMessageProperties.getProperty(MISALIGNED_ENGINES).formatted(players.size(), decisionEngines.size()));
    }
    for(int i = 0; i < decisionEngines.size(); i++) {
      String player = players.get(i);
      String engine = decisionEngines.get(i);
      if (player.equals(HUMAN_PLAYER)) {
        if (!engine.equals(HUMAN_DECISION_ENGINE)) {
          throw new ParserException(exceptionMessageProperties.getProperty(HUMAN_PLAYER_AI_ENGINE));
        }
      } else if (player.equals(AI_PLAYER)) {
        try {
          Class.forName(PLAYER_PATH + engine + DECISION_ENGINE);
        } catch (ClassNotFoundException e) {
          throw new ParserException(
              exceptionMessageProperties.getProperty(AIPLAYER_BAD_ENGINE).formatted(engine));
        }
      }
    }
    return decisionEngines;

  }

  @Override
  public Class getParsedClass() {
    return List.class;
  }
}

package oogasalad.view.screens;

import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import oogasalad.view.maker.LabelMaker;

/**
 * The AI Ships Screen is a simple screen that shows the human players that the AI is currently setting up the ships
 * It's solely used to help the user understand what's happening if there are AI players in the game.
 *
 * This class depends on multiple JavaFX elements in order to be created and the SetupView
 *
 * @author: Eric Xie and Edison Ooi
 */

public class AIShipsScreen extends AbstractScreen{

  private static String AI_SHIPS_RESOURCE = "AIText";
  private static String AI_SHIPS_SCREEN_ID = "ai-ships-screen";
  private static String AI_SHIPS_LABEL_ID = "ai-ships-screen-label";

  /**
   * Class constructor. Lays out the screen with one label to show the AI is setting up their ships.
   *
   * @param spacing   Spacing between nodes on screen
   * @param alignment Alignment of nodes on screen
   * @param handler   On click handler for main button
   */
  public AIShipsScreen(double spacing, Pos alignment,
      EventHandler<ActionEvent> handler, ResourceBundle resourceBundle) {
    super(spacing, alignment, handler, resourceBundle);

    setId(AI_SHIPS_SCREEN_ID);
  }

  // creates main label for the screen

  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(myResources.getString(AI_SHIPS_RESOURCE), AI_SHIPS_LABEL_ID);
  }

  // creates main button for the screen

  @Override
  protected Button createMainButton(EventHandler<ActionEvent> handler) {
    return null;
  }
}



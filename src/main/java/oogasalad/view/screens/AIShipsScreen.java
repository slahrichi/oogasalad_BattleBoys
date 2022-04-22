package oogasalad.view.screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import oogasalad.view.maker.LabelMaker;

public class AIShipsScreen extends AbstractScreen{

  private static String AI_SHIPS_MESSAGE = "The AI Mastermind is setting up their ships...";

  /**
   * Class constructor. Lays out the screen with one label to show the AI is setting up their ships.
   *
   * @param spacing   Spacing between nodes on screen
   * @param alignment Alignment of nodes on screen
   * @param handler   On click handler for main button
   */
  public AIShipsScreen(double spacing, Pos alignment,
      EventHandler<ActionEvent> handler) {
    super(spacing, alignment, handler);

    setId("ai-ships-screen");
  }

  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(AI_SHIPS_MESSAGE, "ai-ships-screen-label");
  }

  @Override
  protected Button createMainButton(EventHandler<ActionEvent> handler) {
    return null;
  }
}



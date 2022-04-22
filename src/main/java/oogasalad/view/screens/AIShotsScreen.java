package oogasalad.view.screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import oogasalad.view.maker.LabelMaker;

public class AIShotsScreen extends AbstractScreen {

  private static String AI_SHOTS_MESSAGE = "The AI Mastermind is taking their shots...";


  /**
   * Class constructor. Lays out the screen with one label and one button.
   *
   * @param spacing   Spacing between nodes on screen
   * @param alignment Alignment of nodes on screen
   * @param handler   On click handler for main button
   */
  public AIShotsScreen(double spacing, Pos alignment,
      EventHandler<ActionEvent> handler) {
    super(spacing, alignment, handler);

    setId("ai-shots-screen");
  }

  @Override
  protected Label createMainLabel() {
    return LabelMaker.makeLabel(AI_SHOTS_MESSAGE, "ai-shots-screen-label");  }

  @Override
  protected Button createMainButton(EventHandler<ActionEvent> handler) {
    return null;
  }
}

package oogasalad.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import oogasalad.view.maker.LabelMaker;

public class PassComputerMessageView extends VBox {

  private static final String DEFAULT_RESOURCE_PACKAGE = "/";
  private static final String STYLESHEET = "stylesheets/messageStylesheet.css";

  private static String PASS_MESSAGE = "Please pass the computer to player";
  private static String BUTTON_TEXT = "OK";
  private Label messageLabel;
  private Button confirmButton;

  public PassComputerMessageView() {

    setSpacing(20);
    setAlignment(Pos.CENTER);
    createMessageLabel();
    createConfirmButton();
    this.getStylesheets().add(DEFAULT_RESOURCE_PACKAGE + STYLESHEET);
    setId("messageBox");
  }

  private void createMessageLabel() {
    messageLabel = LabelMaker.makeLabel(PASS_MESSAGE, "pass-computer-message-label");
    getChildren().add(messageLabel);
  }

  private void createConfirmButton() {
    confirmButton = new Button(BUTTON_TEXT);
    confirmButton.setFont(new Font(25));
    getChildren().add(confirmButton);
  }

  public void setPlayerName(String playerName) {
    messageLabel.setText(PASS_MESSAGE + playerName);
  }

  public void setButtonOnMouseClicked(EventHandler<ActionEvent> handler) {
    confirmButton.setOnAction(handler);
  }
}

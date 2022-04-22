package oogasalad.view.maker;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class DialogMaker {

  public static TextInputDialog makeTextInputDialog(String title, String header, String content, String text, String editorID, String buttonID) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle(title);
    dialog.setHeaderText(header);
    dialog.setContentText(content);
    dialog.getEditor().setText(text);
    dialog.getEditor().setId(editorID);
    dialog.getDialogPane().lookupButton(ButtonType.OK).setId(buttonID);
    return dialog;
  }

  public static Alert makeAlert(String errorMsg, String id) {
    Alert alert = new Alert(AlertType.ERROR, errorMsg);
    Node alertNode = alert.getDialogPane();
    alertNode.setId(id);
    return alert;
  }
}

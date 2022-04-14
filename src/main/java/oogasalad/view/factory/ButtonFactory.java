package oogasalad.view.factory;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class ButtonFactory extends Button{


  public ButtonFactory(double width, double height, String name, String id, EventHandler<ActionEvent> handler){
    this.setText(name);
    this.setPrefSize(width, height);
    this.setId(id);
    this.setOnAction(handler);

  }

  public ButtonFactory(double width, double height, String name, String id, ImageView image, EventHandler<ActionEvent> handler){
    this.setText(name);
    this.setPrefSize(width, height);
    this.setId(id);
    this.setOnAction(handler);
    this.setGraphic(image);
  }




}

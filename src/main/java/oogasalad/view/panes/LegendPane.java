package oogasalad.view.panes;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;

public class LegendPane extends TitledPane{

  private Label legendKey;

  public LegendPane(){

    legendKey = new Label();
    setUpPane();
    setUpText();

  }

  private void setUpPane(){

    this.setId("legendPane");
    this.setText("Legend Key");
    this.setContent(legendKey);
    this.setExpanded(false);

  }

  private void setUpText(){

    legendKey.prefWidth(100);
    legendKey.setText("Test code here. Thinking about putting a color block next to their corresponding key.");

  }





}

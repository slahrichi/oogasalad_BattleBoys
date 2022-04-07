package oogasalad.view.panes;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.text.Text;

public class LegendPane {

  private TitledPane legendPane;
  private Label legendKey;

  public LegendPane(){

    legendPane = new TitledPane();
    legendKey = new Label();
    setUpPane();
    setUpText();

  }

  private void setUpPane(){

    legendPane.setId("legendPane");
    legendPane.setText("Legend Key");
    legendPane.setContent(legendKey);
    legendPane.setExpanded(false);

  }

  private void setUpText(){

    legendKey.prefWidth(100);
    legendKey.setText("Test code here. Thinking about putting a color block next to their corresponding key.");

  }

  public TitledPane getLegendPane(){
    return legendPane;
  }




}

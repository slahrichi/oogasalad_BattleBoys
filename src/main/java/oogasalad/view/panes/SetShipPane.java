package oogasalad.view.panes;


import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import oogasalad.model.utilities.Coordinate;

// need to hold the ship list as well as create the ships to display

public class SetShipPane {

  private TitledPane shipPane;
  private Coordinate[][] shipList;


  public SetShipPane(Coordinate[][] ships){

    shipPane = new TitledPane();
    shipList = ships;

    setUpPane();
    setUpShips();

  }


  private void setUpPane(){

    shipPane.setId("shipPane");
    shipPane.setText("Ships");
    shipPane.setExpanded(false);

  }

  private void setUpShips(){
    StringBuilder sb = new StringBuilder();
    Label testLabel = new Label();
    for(Coordinate[] coord : shipList){
      sb.append(coord.toString() + "\n");
    }
    testLabel.setText(sb.toString());
    shipPane.setContent(testLabel);
  }

  private void createShips(){


  }

  public TitledPane getShipPane(){
    return shipPane;
  }

}

package oogasalad.view.panes;

import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;

public class AccordionPane {

  private Accordion myAccordion;


  public AccordionPane(TitledPane[] panes){

    myAccordion = new Accordion();
    myAccordion.getPanes().addAll(panes);
    myAccordion.setExpandedPane(panes[0]);

  }

  public Accordion getAccordion() {
    return myAccordion;
  }

}

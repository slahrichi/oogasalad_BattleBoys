package oogasalad.view;

/**
 * This record's purpose is to provide a package for easily sending and receiving information related
 * to a click on the screen. Every click is registered by an observer pattern, so the observer can
 * receive the information about where on the screen a particular click occurred as well as the ID
 * of the board that was clicked on.
 *
 * @author Minjun Kwak
 */
public record Info(int row, int col, int ID) {}

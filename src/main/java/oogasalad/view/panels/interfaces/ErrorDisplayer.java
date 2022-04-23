package oogasalad.view.panels.interfaces;

/**
 * Purpose: This class provides API to display error messages, should be implemented by the
 * view classes that need to display errors
 *
 *
 */

public interface ErrorDisplayer {

  /**
   * Displays an error with a message in a user-friendly way.
   *
   * @param errorMsg message to appear on error
   */
  void showError(String errorMsg);

  /**
   * Displays a (fatal) error with a message in a user-friendly way.
   *
   * @param errorMsg message to appear on error
   */
  void showErrorAndQuit(String errorMsg);



}

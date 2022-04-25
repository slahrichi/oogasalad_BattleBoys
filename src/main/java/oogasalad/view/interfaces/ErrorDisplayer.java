package oogasalad.view.interfaces;

/**
 * Purpose: This class provides API to display error messages, should be implemented by the
 * view classes that need to display errors
 *
 * @author Edison Ooi, Minjun Kwak, Eric Xie
 */
public interface ErrorDisplayer {

  /**
   * Displays an error with a message in a user-friendly way.
   *
   * @param errorMsg message to appear on error
   */
  void showError(String errorMsg);
}

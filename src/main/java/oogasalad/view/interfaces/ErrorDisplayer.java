package oogasalad.view.interfaces;

/**
 * Purpose: This class provides API to display error messages, should be implemented by the
 * view classes that need to display errors
 *
 *
 */

public interface ErrorDisplayer {

  void showError(String errorMsg);

  void showErrorAndQuit(String errorMsg);



}

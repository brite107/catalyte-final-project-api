package io.catalyte.training.finalprojectapi.exceptions;

/**
 * Exception that is thrown when a request attempts to delete a record that has dependent entity
 * records associated with it
 */
public class DependentEntityDeleteViolation extends RuntimeException {

  public DependentEntityDeleteViolation() {
  }

  public DependentEntityDeleteViolation(String message) {
    super(message);
  }
}

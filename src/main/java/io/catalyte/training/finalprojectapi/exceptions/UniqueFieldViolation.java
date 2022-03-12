package io.catalyte.training.finalprojectapi.exceptions;

/**
 * Exception that is thrown when a request attempts to save a record with a field that violates a
 * unique field requirement
 */
public class UniqueFieldViolation extends RuntimeException {

  public UniqueFieldViolation() {
  }

  public UniqueFieldViolation(String message) {
    super(message);
  }
}

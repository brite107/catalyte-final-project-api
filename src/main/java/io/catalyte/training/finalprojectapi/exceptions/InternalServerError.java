package io.catalyte.training.finalprojectapi.exceptions;

/**
 * Exception thrown when there is an internal server error
 */
public class InternalServerError extends RuntimeException {

  public InternalServerError() {

  }

  public InternalServerError(String message) {
    super(message);
  }
}

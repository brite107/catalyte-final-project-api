package io.catalyte.training.finalprojectapi.exceptions;

/**
 * Exception thrown when bad data is provided in a request body
 */
public class BadDataResponse extends RuntimeException {

  public BadDataResponse() {
  }

  public BadDataResponse(String message) {
    super(message);
  }
}

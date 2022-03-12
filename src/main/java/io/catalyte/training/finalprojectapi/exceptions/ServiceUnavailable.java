package io.catalyte.training.finalprojectapi.exceptions;

/**
 * Exception that is thrown as a catch-all for any error that is not specifically named and caught
 */
public class ServiceUnavailable extends RuntimeException {

  public ServiceUnavailable() {
  }

  public ServiceUnavailable(String message) {
    super(message);
  }

  public ServiceUnavailable(Exception e) {
    super(e.getCause());
  }
}

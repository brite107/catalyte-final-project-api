package io.catalyte.training.finalprojectapi.exceptions;

/**
 * Exception thrown when a call to a repository does not find a record with the provided id
 */
public class ResourceNotFound extends RuntimeException {

  public ResourceNotFound() {
  }

  public ResourceNotFound(String message) {
    super(message);
  }
}

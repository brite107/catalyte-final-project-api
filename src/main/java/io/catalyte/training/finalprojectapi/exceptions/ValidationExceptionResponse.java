package io.catalyte.training.finalprojectapi.exceptions;

import java.util.Date;
import java.util.List;

/**
 * This class extends the ExceptionResponse to include a list of validation errors.
 */
public class ValidationExceptionResponse extends ExceptionResponse {

  public List<String> validationErrors;

  public ValidationExceptionResponse(String errorType, Date date, String errorMessage,
      List<String> errors) {
    super(errorType, date, errorMessage);
    validationErrors = errors;
  }
}

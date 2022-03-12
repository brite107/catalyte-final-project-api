package io.catalyte.training.finalprojectapi.constants;

/**
 * holds constants used throughout the project
 */
public class StringConstants {

  public static final String GENERATED_ID = "Database id";

  // Status constants
  public static final String NOT_FOUND = "Not Found";
  public static final String SERVER_ERROR = "Server Error";
  public static final String UNIQUE_FIELD_VIOLATION = "Unique Field Violation";
  public static final String DEPENDENT_ENTITY_DELETE_VIOLATION = "Dependent Entity Delete Violation";
  public static final String EMAIL_CONFLICT = "The email address is already associated with another patient";
  public static final String BAD_REQUEST_STATE = "The patient's state must be one of the 50 US states that exist";
  public static final String BAD_REQUEST_GENDER = "The patient's gender is not valid";
  public static final String BAD_REQUEST_PATIENT_NOT_FOUND = "The patient does not exist in the database";
  public static final String BAD_REQUEST_ENCOUNTER_NOT_FOUND = "The encounter does not exist in the database";
  public static final String BAD_REQUEST_ID = "The id of the request body's entity must match the id of the path parameter";
  public static final String BAD_REQUEST_TOTAL_COST = "The encounter's total cost must have exactly 2 decimal places";
  public static final String BAD_REQUEST_COPAY = "The encounter's copay must have exactly 2 decimal places";
  public static final String DELETE_VIOLATION_ENCOUNTERS = "This patient has encounters and cannot be deleted";
  // General validation
  public static final String REQUIRED_STRING_LENGTH_ERROR = " must have at least 3 characters";
  public static final String REQUIRED_FIELD_ERROR = " is a required field";
  public static final String POSITIVE_VALUE_ERROR = " must be a positive value";
  public static final String POSITIVE_OR_ZERO_VALUE_ERROR = " must be a positive value or zero";
  public static final String DECIMAL_PLACE = " must have two decimal places";
  public static final String VALIDATION_ERROR = "Validation Error";
  public static final String BAD_DATA = "Bad Data";
  public static final String UNEXPECTED_ERROR = "Unexpected Server Error";

  // Patient validation
  public static final String SSN_VALIDATION_ERROR = "SSN did not meet required format, example: '123-45-6789'";
  public static final String EMAIL_VALIDATION_ERROR = "Must be an email format";
  public static final String POSTAL_CODE_ERROR = "Postal code did not meet required format, example: '12345' or '12345-1234'";

  // Encounter validation
  public static final String VISIT_CODE_VALIDATION_ERROR = "Visit code did not meet required format: example 'H7J 8W2'";
  public static final String BILLING_CODE_VALIDATION_ERROR = "Billing code did not meet required format, example: '123.456.789-12'";
  public static final String ICD10_CODE_VALIDATION_ERROR = "ICD10 code did not meet required format, example: 'A22'";

  // Endpoint constants
  public static final String CONTEXT_PATIENTS = "/patients";
  public static final String CONTEXT_ENCOUNTERS = "/patients/{patientId}/encounters";
  public static final String ID_ENDPOINT = "/{id}";

}

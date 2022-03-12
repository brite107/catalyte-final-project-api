package io.catalyte.training.finalprojectapi.domains.patients;

import static io.catalyte.training.finalprojectapi.constants.StringConstants.CONTEXT_PATIENTS;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.ID_ENDPOINT;

import io.catalyte.training.finalprojectapi.exceptions.BadDataResponse;
import io.catalyte.training.finalprojectapi.exceptions.InternalServerError;
import io.catalyte.training.finalprojectapi.exceptions.ResourceNotFound;
import io.catalyte.training.finalprojectapi.exceptions.ServiceUnavailable;
import io.catalyte.training.finalprojectapi.exceptions.UniqueFieldViolation;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * holds crud methods for the patient entity
 */
@RestController
@RequestMapping(CONTEXT_PATIENTS)
@ApiResponses(value = {
    @ApiResponse(code = 500, message = "Internal Server Error", response = InternalServerError.class),
    @ApiResponse(code = 503, message = "Service Unavailable", response = ServiceUnavailable.class)
})
public class PatientController {

  private static final Logger logger = LogManager.getLogger(PatientController.class);

  @Autowired
  PatientService patientService;

  /**
   * Calls the service to retrieve a patient by Id
   *
   * @param id the id of the patient to be retrieved
   * @return ResponseEntity<Patient> - patient which matches the given id and the HTTP status OK
   */
  @GetMapping(value = ID_ENDPOINT)
  @ApiOperation("Gets a patient by ID")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Patient.class),
      @ApiResponse(code = 404, message = "NOT FOUND", response = ResourceNotFound.class)
  })
  public ResponseEntity<Patient> getPatientById(@PathVariable Long id) throws Exception {
    logger.info(new Date() + " Get by id " + id + " request received");

    return new ResponseEntity<>(patientService.getPatientById(id), HttpStatus.OK);
  }

  /**
   * gives me all patients if I pass a null patient or patients matching an example with non-null
   * patient
   *
   * @param patient patient object which can have null or non-null fields, returns status 200
   * @return List of patients
   * @throws Exception
   */
  @GetMapping
  @ApiOperation("Gets all patients, or all patients matching an example with patient fields")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Patient.class)
  })
  public ResponseEntity<List<Patient>> queryPatients(Patient patient) throws Exception {

    return new ResponseEntity<>(patientService.queryPatients(patient), HttpStatus.OK);

  }

  /**
   * Calls the service to delete a patient with a given id
   *
   * @param id the id of the patient to be deleted
   * @return ResponseEntity with the HTTP status 204 (NO CONTENT)
   */
  @DeleteMapping(value = ID_ENDPOINT)
  @ApiOperation("Delete a patient by ID")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "NO CONTENT"),
      @ApiResponse(code = 404, message = "NOT FOUND", response = ResourceNotFound.class)
  })
  public ResponseEntity deletePatientById(@PathVariable Long id) {
    logger.info(new Date() + " Delete request received for id: " + id);

    patientService.deletePatient(id);

    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }

  /**
   * Calls the service to save a single patient
   *
   * @param patient - the patient to be saved
   * @return ResponseEntity patient added and HTTP status code 201 (CREATED)
   */
  @PostMapping
  @ApiOperation("Add a single patient")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "CREATED", response = Patient.class),
      @ApiResponse(code = 409, message = "CONFLICT", response = UniqueFieldViolation.class),
      @ApiResponse(code = 400, message = "BAD DATA", response = BadDataResponse.class)
  })
  public ResponseEntity<Patient> save(@Valid @RequestBody Patient patient) {
    logger.info(new Date() + " Post request received");

    return new ResponseEntity<>(patientService.addPatient(patient), HttpStatus.CREATED);
  }

  /**
   * Update patient by id.
   *
   * @param id      the id of the patient to be updated from the path variable
   * @param patient the patient's new information from the request body
   * @return the patient if correctly input
   */
  @PutMapping(value = ID_ENDPOINT)
  @ApiOperation("Updates a Patient by Id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Patient.class),
      @ApiResponse(code = 404, message = "NOT FOUND"),
      @ApiResponse(code = 400, message = "BAD REQUEST")
  })
  public ResponseEntity<Patient> updatePatientById(@PathVariable Long id,
      @Valid @RequestBody Patient patient)
      throws Exception {

    return new ResponseEntity<>(patientService.updatePatientById(id, patient), HttpStatus.OK);
  }
}

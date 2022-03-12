package io.catalyte.training.finalprojectapi.domains.encounters;

import static io.catalyte.training.finalprojectapi.constants.StringConstants.CONTEXT_ENCOUNTERS;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.ID_ENDPOINT;

import io.catalyte.training.finalprojectapi.exceptions.BadDataResponse;
import io.catalyte.training.finalprojectapi.exceptions.InternalServerError;
import io.catalyte.training.finalprojectapi.exceptions.ResourceNotFound;
import io.catalyte.training.finalprojectapi.exceptions.ServiceUnavailable;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Holds crud methods for the encounter entity
 */
@RestController
@RequestMapping(CONTEXT_ENCOUNTERS)
@ApiResponses(value = {
    @ApiResponse(code = 500, message = "Internal Server Error", response = InternalServerError.class),
    @ApiResponse(code = 503, message = "Service Unavailable", response = ServiceUnavailable.class)
})
public class EncounterController {

  private static final Logger logger = LogManager.getLogger(EncounterController.class);

  @Autowired
  EncounterService encounterService;

  /**
   * Calls the service to retrieve an encounter by Id
   *
   * @param id the id of the encounter to be retrieved
   * @return ResponseEntity<Encounter> - encounter which matches the given id and the HTTP status OK
   */
  @GetMapping(value = ID_ENDPOINT)
  @ApiOperation("Gets an encounter by ID")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Encounter.class),
      @ApiResponse(code = 404, message = "NOT FOUND", response = ResourceNotFound.class)
  })
  public ResponseEntity<Encounter> getEncounterById(@PathVariable Long id,
      @PathVariable Long patientId) throws Exception {
    logger.info(new Date() + " Get by id " + id + " request received");

    return new ResponseEntity<>(encounterService.getEncounterById(id, patientId), HttpStatus.OK);
  }

  /**
   * Gets a list of encounters for a given patient Id
   *
   * @param patientId - the id of the patient to get the encounters for
   * @return List of encounters for the patient
   * @throws Exception
   */
  @GetMapping
  @ApiOperation("Gets all encounters matching a patient id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Encounter.class),
      @ApiResponse(code = 404, message = "NOT FOUND")
  })
  public ResponseEntity<List<Encounter>> getEncountersByPatientId(@PathVariable Long patientId)
      throws Exception {

    return new ResponseEntity<>(encounterService.getEncountersByPatientId(patientId),
        HttpStatus.OK);

  }

  /**
   * Calls the service to save a single encounter
   *
   * @param encounter - the encounter to be saved
   * @return ResponseEntity encounter added and HTTP status code 201 (CREATED)
   */
  @PostMapping
  @ApiOperation("Add a single encounter")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "CREATED", response = Encounter.class),
      @ApiResponse(code = 400, message = "BAD DATA", response = BadDataResponse.class)
  })
  public ResponseEntity<Encounter> save(@Valid @RequestBody Encounter encounter,
      @PathVariable Long patientId) throws Exception {
    logger.info(new Date() + " Post request received");

    return new ResponseEntity<>(encounterService.addEncounter(encounter, patientId),
        HttpStatus.CREATED);
  }

  /**
   * Update encounter by id.
   *
   * @param id        the id of the encounter to be updated from the path variable
   * @param encounter the encounter's new information from the request body
   * @return the encounter if correctly input
   */
  @PutMapping(value = ID_ENDPOINT)
  @ApiOperation("Updates an encounter by Id")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "OK", response = Encounter.class),
      @ApiResponse(code = 404, message = "NOT FOUND"),
      @ApiResponse(code = 400, message = "BAD REQUEST")
  })
  public ResponseEntity<Encounter> updateEncounterById(@PathVariable Long id,
      @Valid @RequestBody Encounter encounter, @PathVariable Long patientId)
      throws Exception {

    return new ResponseEntity<>(encounterService.updateEncounterById(id, encounter, patientId),
        HttpStatus.OK);
  }
}

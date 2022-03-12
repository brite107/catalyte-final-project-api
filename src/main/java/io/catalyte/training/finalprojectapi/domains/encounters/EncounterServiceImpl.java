package io.catalyte.training.finalprojectapi.domains.encounters;

import static io.catalyte.training.finalprojectapi.constants.StringConstants.BAD_REQUEST_COPAY;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.BAD_REQUEST_ENCOUNTER_NOT_FOUND;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.BAD_REQUEST_ID;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.BAD_REQUEST_PATIENT_NOT_FOUND;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.BAD_REQUEST_TOTAL_COST;

import io.catalyte.training.finalprojectapi.domains.patients.PatientRepository;
import io.catalyte.training.finalprojectapi.exceptions.BadDataResponse;
import io.catalyte.training.finalprojectapi.exceptions.ResourceNotFound;
import io.catalyte.training.finalprojectapi.exceptions.ServiceUnavailable;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * Service class which handles the business logic for the encounter entity and implements the
 * EncounterService interface
 */
@Service
public class EncounterServiceImpl implements EncounterService {

  private static final Logger logger = LogManager.getLogger(EncounterServiceImpl.class);

  @Autowired
  EncounterRepository encounterRepository;

  @Autowired
  PatientRepository patientRepository;

  /**
   * Calls the encounter repository to get all encounters matching a patient Id
   *
   * @param patientId - the patient Id to search encounters for
   * @return - a list of encounters
   * @throws ServiceUnavailable
   * @throws ResourceNotFound
   */
  public List<Encounter> getEncountersByPatientId(Long patientId) {
    // check if the patient exists
    boolean validPatientId = true;
    try {
      validPatientId = patientRepository.existsById(patientId);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    // throw error if patient is not valid
    if (!validPatientId) {
      throw new ResourceNotFound(BAD_REQUEST_PATIENT_NOT_FOUND);
    }

    // if the patient is valid, get its encounters
    try {
      return encounterRepository.findByPatientId(patientId);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * Calls the encounter repository to get all encounters matching a query (if query parameters are
   * provided) or else all encounters
   *
   * @param encounter - optional sample encounter to query against
   * @return - a list of encounters
   */
  public List<Encounter> queryEncounters(Encounter encounter) {

    try {
      if (encounter.isEmpty()) {
        return encounterRepository.findAll();
      } else {
        Example<Encounter> encounterExample = Example.of(encounter);
        return encounterRepository.findAll(encounterExample);
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * Call the encounter repository to add an encounter
   *
   * @param encounter - the encounter to add
   * @return - the encounter object that has been added
   * @throws ServiceUnavailable
   * @throws BadDataResponse
   */
  public Encounter addEncounter(Encounter encounter, Long patientId) throws Exception {
    // check if the patient exists
    boolean validPatientId = true;
    try {
      validPatientId = patientRepository.existsById(encounter.getPatientId());
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    // throw error if patient is not valid
    if (!validPatientId) {
      throw new BadDataResponse(BAD_REQUEST_PATIENT_NOT_FOUND);
    }

    // check if patientId in path matches patientId in request body
    if (!encounter.getPatientId().equals(patientId)) {
      throw new BadDataResponse(BAD_REQUEST_ID);
    }

    // check if the total cost and copay have two digits
    if (encounter.getTotalCost().scale() != 2) {
      throw new BadDataResponse(BAD_REQUEST_TOTAL_COST);
    }
    if (encounter.getCopay().scale() != 2) {
      throw new BadDataResponse(BAD_REQUEST_COPAY);
    }

    // save the encounter
    try {
      return encounterRepository.save(encounter);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * Retrieves encounter from the database by Id
   *
   * @param id - the id of the encounter to be returned
   * @return - the encounter that matches the id
   * @throws ServiceUnavailable
   * @throws ResourceNotFound
   */
  public Encounter getEncounterById(Long id, Long patientId) throws Exception {
    // check if the patient is valid
    boolean validPatientId = true;
    try {
      validPatientId = patientRepository.existsById(patientId);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    // throw error if patient is not valid
    if (!validPatientId) {
      throw new ResourceNotFound(BAD_REQUEST_PATIENT_NOT_FOUND);
    }

    // if the patient is valid, try to get this encounter by Id
    try {
      Encounter encounter = encounterRepository.findById(id).orElse(null);

      if (encounter != null) {
        return encounter;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    // Encounter not found
    throw new ResourceNotFound("Could not locate an encounter with the id: " + id);
  }

  public Encounter updateEncounterById(Long id, Encounter encounter, Long patientId)
      throws Exception {
    Encounter existingEncounter;

    // check if the patient exists
    boolean validPatientId = true;
    try {
      validPatientId = patientRepository.existsById(encounter.getPatientId());
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    // throw error if patient is not valid
    if (!validPatientId) {
      throw new ResourceNotFound(BAD_REQUEST_PATIENT_NOT_FOUND);
    }

    // check if id in path matches id in request body
    if (!encounter.getId().equals(id)) {
      throw new BadDataResponse(BAD_REQUEST_ID);
    }

    // check if patientId in path matches patientId in request body
    if (!encounter.getPatientId().equals(patientId)) {
      throw new BadDataResponse(BAD_REQUEST_ID);
    }

    // check if the total cost and copay have two digits
    if (encounter.getTotalCost().scale() != 2) {
      throw new BadDataResponse(BAD_REQUEST_TOTAL_COST);
    }
    if (encounter.getCopay().scale() != 2) {
      throw new BadDataResponse(BAD_REQUEST_COPAY);
    }

    try {
      // get the existing encounter from the database
      existingEncounter = encounterRepository.findById(id).orElse(null);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    // if encounter was not found...
    if (existingEncounter == null) {
      throw new ResourceNotFound(BAD_REQUEST_ENCOUNTER_NOT_FOUND);
    }

    try {
      return encounterRepository.save(encounter);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }
}

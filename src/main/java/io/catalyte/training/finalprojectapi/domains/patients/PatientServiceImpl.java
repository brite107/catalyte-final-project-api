package io.catalyte.training.finalprojectapi.domains.patients;

import static io.catalyte.training.finalprojectapi.constants.StringConstants.BAD_REQUEST_GENDER;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.BAD_REQUEST_ID;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.BAD_REQUEST_PATIENT_NOT_FOUND;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.BAD_REQUEST_STATE;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.DELETE_VIOLATION_ENCOUNTERS;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.EMAIL_CONFLICT;

import io.catalyte.training.finalprojectapi.constants.ValidGenders;
import io.catalyte.training.finalprojectapi.constants.ValidStates;
import io.catalyte.training.finalprojectapi.domains.encounters.Encounter;
import io.catalyte.training.finalprojectapi.domains.encounters.EncounterService;
import io.catalyte.training.finalprojectapi.exceptions.BadDataResponse;
import io.catalyte.training.finalprojectapi.exceptions.DependentEntityDeleteViolation;
import io.catalyte.training.finalprojectapi.exceptions.ResourceNotFound;
import io.catalyte.training.finalprojectapi.exceptions.ServiceUnavailable;
import io.catalyte.training.finalprojectapi.exceptions.UniqueFieldViolation;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * Service class which handles the business logic for the patient entity and implements the
 * PatientService interface
 */
@Service
public class PatientServiceImpl implements PatientService {

  private static final Logger logger = LogManager.getLogger(PatientServiceImpl.class);

  @Autowired
  PatientRepository patientRepository;

  @Autowired
  EncounterService encounterService;

  /**
   * Calls the patient repository to get all patients matching a query (if query parameters are
   * provided) or else all patients
   *
   * @param patient - optional sample patient to query against
   * @return - a list of patients
   */
  public List<Patient> queryPatients(Patient patient) {

    try {
      if (patient.isEmpty()) {
        return patientRepository.findAll();
      } else {
        Example<Patient> patientExample = Example.of(patient);
        return patientRepository.findAll(patientExample);
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * calls the patient repository to delete a patient
   *
   * @param id - the id of the patient to delete
   * @throws ServiceUnavailable
   * @throws ResourceNotFound
   * @throws DependentEntityDeleteViolation
   */
  public void deletePatient(Long id) {
    // check that the patient exists
    Patient existingPatient;
    try {
      // get the existing patient from the database
      existingPatient = patientRepository.findById(id).orElse(null);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    if (existingPatient != null) {

      // get the list of encounters
      List<Encounter> encounters;
      try {
        encounters = encounterService.getEncountersByPatientId(id);
      } catch (Exception e) {
        throw new ServiceUnavailable(e);
      }

      if (encounters.size() == 0) {
        try {
          if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
            return;
          }
        } catch (Exception e) {
          throw new ServiceUnavailable(e);
        }
      } else {
        throw new DependentEntityDeleteViolation(DELETE_VIOLATION_ENCOUNTERS);
      }
    }

    // Patient not found
    throw new ResourceNotFound("Could not locate a patient with the id: " + id);
  }

  /**
   * calls the patient repository to add a patient
   *
   * @param patient - the patient to add
   * @return the patient object that has been added
   * @throws UniqueFieldViolation
   * @throws ServiceUnavailable
   */
  public Patient addPatient(Patient patient) {
    boolean emailAlreadyExists;
    boolean stateIsValid;

    // check patient state is valid
    if (!ValidStates.validStatesList.contains(patient.getState())) {
      throw new BadDataResponse(BAD_REQUEST_STATE);
    }

    // check if patient gender is valid
    if (!ValidGenders.validGendersList.contains(patient.getGender())) {
      throw new BadDataResponse(BAD_REQUEST_GENDER);
    }

    try {
      // check if email already exists
      emailAlreadyExists = patientRepository.existsByEmail(patient.getEmail());

      if (!emailAlreadyExists) {
        return patientRepository.save(patient);
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    // if made it to this point, email is not unique
    throw new UniqueFieldViolation(EMAIL_CONFLICT);
  }

  /**
   * Retrieves patient from the database by Id
   *
   * @param id - the id of the patient to be returned
   * @return - the patient that matches the id
   * @throws ServiceUnavailable
   * @throws ResourceNotFound
   */
  public Patient getPatientById(Long id) throws Exception {
    try {
      Patient patient = patientRepository.findById(id).orElse(null);

      if (patient != null) {
        return patient;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    // Patient not found
    throw new ResourceNotFound("Could not locate a patient with the id: " + id);
  }

  /**
   * Updates a patient with a specific id
   *
   * @param id      - the id of the patient to be updated
   * @param patient - the patient's new information
   * @return - the updated patient
   * @throws UniqueFieldViolation
   * @throws ServiceUnavailable
   * @throws BadDataResponse
   * @throws ResourceNotFound
   */
  public Patient updatePatientById(Long id, Patient patient) throws Exception {
    Patient existingPatient;

    boolean emailIsSame;
    boolean newEmailIsUnique;
    String currentEmail;

    // get the new email from the patient passed int
    String newEmail = patient.getEmail();

    // check if id in path matches id in request body
    if (!patient.getId().equals(id)) {
      throw new BadDataResponse(BAD_REQUEST_ID);
    }

    // check patient state is valid
    if (!ValidStates.validStatesList.contains(patient.getState())) {
      throw new BadDataResponse(BAD_REQUEST_STATE);
    }

    // check if patient gender is valid
    if (!ValidGenders.validGendersList.contains(patient.getGender())) {
      throw new BadDataResponse(BAD_REQUEST_GENDER);
    }

    try {

      // get the existing patient from the database
      existingPatient = patientRepository.findById(id).orElse(null);

      if (existingPatient != null) {

        // get the current email from the database
        currentEmail = existingPatient.getEmail();

        // see if new email already exists
        newEmailIsUnique = !patientRepository.existsByEmail(newEmail);

        // set local for email is same
        emailIsSame = currentEmail.equals(newEmail);

        // only continue if email has not changed, or new email is unique
        if (emailIsSame || newEmailIsUnique) {
          return patientRepository.save(patient);
        }
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }

    // if patient was not found...
    if (existingPatient == null) {
      throw new ResourceNotFound(BAD_REQUEST_PATIENT_NOT_FOUND);
    }
    // patient was found so it must be because of an email conflict
    else {
      throw new UniqueFieldViolation(EMAIL_CONFLICT);
    }
  }
}

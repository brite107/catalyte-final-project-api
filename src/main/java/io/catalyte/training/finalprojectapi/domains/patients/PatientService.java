package io.catalyte.training.finalprojectapi.domains.patients;

import java.util.List;

/**
 * Patient service interface with crud methods for a patient
 */
public interface PatientService {

  List<Patient> queryPatients(Patient patient) throws Exception;

  void deletePatient(Long id);

  Patient addPatient(Patient patient);

  Patient getPatientById(Long id) throws Exception;

  Patient updatePatientById(Long id, Patient patient) throws Exception;

}

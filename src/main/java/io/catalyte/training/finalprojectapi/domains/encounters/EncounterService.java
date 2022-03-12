package io.catalyte.training.finalprojectapi.domains.encounters;

import java.util.List;

/**
 * Encounter service interface with crud methods for an encounter
 */
public interface EncounterService {

  List<Encounter> queryEncounters(Encounter encounter) throws Exception;

  Encounter addEncounter(Encounter encounter, Long patientId) throws Exception;

  Encounter getEncounterById(Long id, Long patientId) throws Exception;

  Encounter updateEncounterById(Long id, Encounter encounter, Long patientId) throws Exception;

  List<Encounter> getEncountersByPatientId(Long id);
}

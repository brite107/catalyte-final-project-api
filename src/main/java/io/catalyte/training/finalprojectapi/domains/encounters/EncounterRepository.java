package io.catalyte.training.finalprojectapi.domains.encounters;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Encounter repository which stores encounters
 */
@Repository
public interface EncounterRepository extends JpaRepository<Encounter, Long> {

  List<Encounter> findByPatientId(Long patientId);
}

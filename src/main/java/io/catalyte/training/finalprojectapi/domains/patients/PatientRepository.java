package io.catalyte.training.finalprojectapi.domains.patients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Patient repository which stores patients
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

  Boolean existsByEmail(String email);
}

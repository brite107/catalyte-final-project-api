package io.catalyte.training.finalprojectapi.data;

import io.catalyte.training.finalprojectapi.domains.encounters.Encounter;
import io.catalyte.training.finalprojectapi.domains.encounters.EncounterRepository;
import io.catalyte.training.finalprojectapi.domains.patients.Patient;
import io.catalyte.training.finalprojectapi.domains.patients.PatientRepository;
import java.math.BigDecimal;
import java.sql.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * This class runs after the server starts and loads initial datasets into the database
 */
@Component
public class DataLoader implements CommandLineRunner {

  private static final Logger logger = LogManager.getLogger(DataLoader.class);

  @Autowired
  private PatientRepository patientRepository;

  @Autowired
  private EncounterRepository encounterRepository;

  // declare patients
  private Patient patientOne;
  private Patient patientTwo;
  private Patient patientThree;
  private Patient patientFour;
  private Patient patientFive;

  // declare encounters for patient One
  private Encounter encounterOne1;
  private Encounter encounterOne2;
  private Encounter encounterOne3;

  // declare encounters for patient Two
  private Encounter encounterTwo1;
  private Encounter encounterTwo2;
  private Encounter encounterTwo3;

  // declare encounters for patient Three
  private Encounter encounterThree1;
  private Encounter encounterThree2;
  private Encounter encounterThree3;

  // declare encounters for patient Four
  private Encounter encounterFour1;
  private Encounter encounterFour2;
  private Encounter encounterFour3;

  /**
   * method which loads all of the patients and encounters when program is started
   *
   * @param args - command line arguments
   */
  @Override
  public void run(String... args) throws Exception {
    logger.info("Loading data...");
    logger.info("Loading data...");
    loadPatients();
    loadEncounters();
  }

  /**
   * Saves the patients to the repository
   */
  private void loadPatients() {
    patientOne = patientRepository.save(
        new Patient("Bart", "Simpson", "111-11-1111", "bart@mail.com", 10, 62, 130,
            "Burns Insurance", "Male", "123 Main Street", "Springfield", "MA", "90049"));
    patientTwo = patientRepository.save(
        new Patient("Lisa", "Simpson", "222-22-2222", "lisa@mail.com", 8, 62, 130,
            "Burns Insurance", "Female", "123 Main Street", "Springfield", "MA", "90049"));
    patientThree = patientRepository.save(
        new Patient("Homer", "Simpson", "333-33-3333", "homer@mail.com", 31, 66, 130,
            "Burns Insurance", "Male", "123 Main Street", "Springfield", "MA", "90049"));
    patientFour = patientRepository.save(
        new Patient("Marge", "Simpson", "444-44-4444", "marge@mail.com", 34, 64, 130,
            "Burns Insurance", "Female", "123 Main Street", "Springfield", "MA", "90049"));
    patientFive = patientRepository.save(
        new Patient("Maggie", "Simpson", "555-55-5555", "maggie@mail.com", 1, 26, 130,
            "Burns Insurance", "Female", "123 Main Street", "Springfield", "MA", "90049"));

  }

  /**
   * Saves the encounters to the repository
   */
  private void loadEncounters() {
    encounterOne1 = encounterRepository.save(
        new Encounter(1L, "Lots of interesting notes here", "N3W 3C3", "New Hospital",
            "123.456.789-00", "Z99", new BigDecimal("0.11"), BigDecimal.valueOf(0, 2),
            "new complaint", 75, 120, 80, Date.valueOf("2020-08-04")));
    encounterOne2 = encounterRepository.save(
        new Encounter(1L, "Patient is not cooperative", "W3W 3W3", "New Hospital", "123.456.789-02",
            "A25", new BigDecimal("145.00"), new BigDecimal("45.00"), "anxiety", 100, 180, 90,
            Date.valueOf("2020-08-24")));
    encounterOne3 = encounterRepository.save(
        new Encounter(1L, null, "N3W 3C3", "Best Hospital", "123.456.789-01", "B44",
            new BigDecimal("0.11"), BigDecimal.valueOf(0, 2), "headache", 75, 120, 80,
            Date.valueOf("2020-09-04")));

    encounterTwo1 = encounterRepository.save(
        new Encounter(2L, "Lots of interesting notes here", "N3W 3C3", "New Hospital",
            "123.456.789-00", "Z99", new BigDecimal("0.11"), BigDecimal.valueOf(0, 2),
            "new complaint", 75, 120, 80, Date.valueOf("2020-08-04")));
    encounterTwo2 = encounterRepository.save(
        new Encounter(2L, "Patient is not cooperative", "W3W 3W3", "New Hospital", "123.456.789-02",
            "A25", new BigDecimal("145.00"), new BigDecimal("45.00"), "anxiety", 100, 180, 90,
            Date.valueOf("2020-08-24")));
    encounterTwo3 = encounterRepository.save(
        new Encounter(2L, null, "N3W 3C3", "Best Hospital", "123.456.789-01", "B44",
            new BigDecimal("0.11"), BigDecimal.valueOf(0, 2), "headache", 75, 120, 80,
            Date.valueOf("2020-09-04")));

    encounterThree1 = encounterRepository.save(
        new Encounter(3L, "Lots of interesting notes here", "N3W 3C3", "New Hospital",
            "123.456.789-00", "Z99", new BigDecimal("0.11"), BigDecimal.valueOf(0, 2),
            "new complaint", 75, 120, 80, Date.valueOf("2020-08-04")));
    encounterThree2 = encounterRepository.save(
        new Encounter(3L, "Patient is not cooperative", "W3W 3W3", "New Hospital", "123.456.789-02",
            "A25", new BigDecimal("145.00"), new BigDecimal("45.00"), "anxiety", 100, 180, 90,
            Date.valueOf("2020-08-24")));
    encounterThree3 = encounterRepository.save(
        new Encounter(3L, null, "N3W 3C3", "Best Hospital", "123.456.789-01", "B44",
            new BigDecimal("0.11"), BigDecimal.valueOf(0, 2), "headache", 75, 120, 80,
            Date.valueOf("2020-09-04")));

    encounterFour1 = encounterRepository.save(
        new Encounter(4L, "Lots of interesting notes here", "N3W 3C3", "New Hospital",
            "123.456.789-00", "Z99", new BigDecimal("0.11"), BigDecimal.valueOf(0, 2),
            "new complaint", 75, 120, 80, Date.valueOf("2020-08-04")));
    encounterFour2 = encounterRepository.save(
        new Encounter(4L, "Patient is not cooperative", "W3W 3W3", "New Hospital", "123.456.789-02",
            "A25", new BigDecimal("145.00"), new BigDecimal("45.00"), "anxiety", 100, 180, 90,
            Date.valueOf("2020-08-24")));
    encounterFour3 = encounterRepository.save(
        new Encounter(4L, null, "N3W 3C3", "Best Hospital", "123.456.789-01", "B44",
            new BigDecimal("0.11"), BigDecimal.valueOf(0, 2), "headache", 75, 120, 80,
            Date.valueOf("2020-09-04")));
  }
}

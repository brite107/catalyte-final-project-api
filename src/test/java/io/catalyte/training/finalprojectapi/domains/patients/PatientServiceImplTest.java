package io.catalyte.training.finalprojectapi.domains.patients;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.catalyte.training.finalprojectapi.domains.encounters.Encounter;
import io.catalyte.training.finalprojectapi.domains.encounters.EncounterService;
import io.catalyte.training.finalprojectapi.exceptions.BadDataResponse;
import io.catalyte.training.finalprojectapi.exceptions.DependentEntityDeleteViolation;
import io.catalyte.training.finalprojectapi.exceptions.ResourceNotFound;
import io.catalyte.training.finalprojectapi.exceptions.ServiceUnavailable;
import io.catalyte.training.finalprojectapi.exceptions.UniqueFieldViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.validation.UnexpectedTypeException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;
import org.springframework.transaction.CannotCreateTransactionException;

/**
 * Tests the PatientServiceImpl class
 */
public class PatientServiceImplTest {

  List<Patient> patientList = new ArrayList<>();
  List<Encounter> encounterList = new ArrayList<>();
  Patient patientOne = new Patient();
  @Mock
  private PatientRepository mockPatientRepository;
  @Mock
  private EncounterService mockEncounterService;
  @InjectMocks
  private PatientServiceImpl patientService;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);

    patientOne.setId(1L);
    patientOne.setFirstName("Bart");
    patientOne.setLastName("Simpson");
    patientOne.setSsn("111-11-1111");
    patientOne.setEmail("bart2@mail.com");
    patientOne.setAge(10);
    patientOne.setHeight(62);
    patientOne.setWeight(130);
    patientOne.setInsurance("Burns Insurance");
    patientOne.setGender("Male");
    patientOne.setStreet("123 Main Street");
    patientOne.setCity("Springfield");
    patientOne.setState("MA");
    patientOne.setPostal("90049");

    patientList.add(patientOne);

    // when statements for happy paths
    when(mockPatientRepository.findAll()).thenReturn(patientList);
    when(mockPatientRepository.findAll(any(Example.class))).thenReturn(patientList);
    when(mockPatientRepository.save(any(Patient.class))).thenReturn(patientList.get(0));
    when(mockPatientRepository.existsByEmail(any(String.class))).thenReturn(false);
    when(mockPatientRepository.findById(any(Long.class))).thenReturn(Optional.of(patientOne));
    when(mockEncounterService.getEncountersByPatientId(any(Long.class))).thenReturn(encounterList);
  }

  @Test
  public void queryPatientsNullExample() {
    List<Patient> actualResult = patientService.queryPatients(new Patient());
    Assert.assertEquals(patientList, actualResult);
  }

  @Test
  public void queryPatientsNonNullExample() {
    List<Patient> actualResult = patientService.queryPatients(patientOne);
    Assert.assertEquals(patientList, actualResult);
  }

  @Test(expected = ServiceUnavailable.class)
  public void queryPatientsDBError() {
    when(mockPatientRepository.findAll()).thenThrow(CannotCreateTransactionException.class);
    patientService.queryPatients(new Patient());
  }

  @Test(expected = ServiceUnavailable.class)
  public void queryPatientsUnexpectedError() {
    when(mockPatientRepository.findAll()).thenThrow(UnexpectedTypeException.class);
    List<Patient> actualResult = patientService.queryPatients(new Patient());
  }

  @Test
  public void getPatientByIdReturnsPatient() throws Exception {
    Patient actualResult = patientService.getPatientById(1L);
    Assert.assertEquals(patientOne, actualResult);
  }

  @Test(expected = ResourceNotFound.class)
  public void getPatientByIdIdNotFound() throws Exception {
    when(mockPatientRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    patientService.getPatientById(1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void getPatientByIdDBError() throws Exception {
    when(mockPatientRepository.findById(any(Long.class)))
        .thenThrow(CannotCreateTransactionException.class);
    patientService.getPatientById(1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void getPatientByIdUnexpectedError() throws Exception {
    when(mockPatientRepository.findById(any(Long.class)))
        .thenThrow(UnexpectedTypeException.class);
    patientService.getPatientById(1L);
  }

  @Test
  public void deletePatient() {
    when(mockPatientRepository.existsById(anyLong())).thenReturn(true);
    patientService.deletePatient(1L);
    verify(mockPatientRepository).deleteById(any());
  }

  @Test(expected = DependentEntityDeleteViolation.class)
  public void deletePatientThatHasAssociatedEncounters() throws Exception {
    Encounter encounter1 = new Encounter();
    encounterList.add(encounter1);
    when(mockEncounterService.getEncountersByPatientId(any(Long.class))).thenReturn(encounterList);
    patientService.deletePatient(1L);
  }

  @Test(expected = ResourceNotFound.class)
  public void deletePatientBadID() {
    doThrow(ResourceNotFound.class).when(mockPatientRepository).deleteById(anyLong());
    patientService.deletePatient(1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void deletePatientDBError() {
    doThrow(ServiceUnavailable.class).when(mockPatientRepository).existsById(anyLong());
    patientService.deletePatient(1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void deletePatientEncounterServiceThrowsDBError() {
    doThrow(ServiceUnavailable.class).when(mockEncounterService)
        .getEncountersByPatientId(any(Long.class));
    patientService.deletePatient(1L);
  }

  @Test
  public void addPatientReturnsPatient() throws Exception {
    Patient actualResult = patientService.addPatient(patientOne);
    Assert.assertEquals(patientOne, actualResult);
  }

  @Test(expected = UniqueFieldViolation.class)
  public void addPatientEmailConflict() throws Exception {
    when(mockPatientRepository.existsByEmail(any(String.class))).thenReturn(true);
    patientService.addPatient(patientOne);
  }

  @Test(expected = BadDataResponse.class)
  public void addPatientInvalidState() throws Exception {
    patientOne.setState("ZX");
    patientService.addPatient(patientOne);
  }

  @Test(expected = BadDataResponse.class)
  public void addPatientInvalidGender() throws Exception {
    patientOne.setGender("Non-binary");
    patientService.addPatient(patientOne);
  }

  @Test(expected = ServiceUnavailable.class)
  public void addPatientDBError() throws Exception {
    when(mockPatientRepository.save(any(Patient.class)))
        .thenThrow(CannotCreateTransactionException.class);
    patientService.addPatient(patientOne);
  }

  @Test(expected = ServiceUnavailable.class)
  public void addPatientUnexpectedError() throws Exception {
    when(mockPatientRepository.save(any(Patient.class)))
        .thenThrow(UnexpectedTypeException.class);
    patientService.addPatient(patientOne);
  }

  @Test
  public void updatePatientByIdReturnsPatient() throws Exception {
    patientOne.setFirstName("New Bart");
    patientService.updatePatientById(1L, patientOne);
    Assert.assertSame("New Bart", patientOne.getFirstName());
  }

  @Test(expected = ResourceNotFound.class)
  public void updatePatientByIdIdNotFound() throws Exception {
    when(mockPatientRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    patientOne.setFirstName("New Bart");
    Patient result = patientService.updatePatientById(1L, patientOne);
    Assert.assertNull(result);
  }

  @Test(expected = BadDataResponse.class)
  public void updatePatientByIdIdDoesNotMatch() throws Exception {
    patientOne.setId(3L);
    Patient result = patientService.updatePatientById(1L, patientOne);
  }

  @Test(expected = UniqueFieldViolation.class)
  public void updatePatientEmailConflict() throws Exception {
    Patient updatedPatient = new Patient();

    when(mockPatientRepository.existsByEmail(any(String.class))).thenReturn(true);

    updatedPatient.setId(1L);
    updatedPatient.setFirstName("Bart");
    updatedPatient.setLastName("Simpson");
    updatedPatient.setSsn("111-11-1111");
    updatedPatient.setEmail("newBart@mail.com");
    updatedPatient.setAge(10);
    updatedPatient.setHeight(62);
    updatedPatient.setWeight(130);
    updatedPatient.setInsurance("Burns Insurance");
    updatedPatient.setGender("Male");
    updatedPatient.setStreet("123 Main Street");
    updatedPatient.setCity("Springfield");
    updatedPatient.setState("MA");
    updatedPatient.setPostal("90049");

    patientService.updatePatientById(1L, updatedPatient);
  }

  @Test(expected = BadDataResponse.class)
  public void updatePatientInvalidState() throws Exception {
    patientOne.setState("ZX");
    patientService.updatePatientById(1L, patientOne);
  }

  @Test(expected = BadDataResponse.class)
  public void updatePatientInvalidGender() throws Exception {
    patientOne.setGender("Non-binary");
    patientService.updatePatientById(1L, patientOne);
  }

  @Test(expected = ServiceUnavailable.class)
  public void updatePatientByIdDBError() throws Exception {
    when(mockPatientRepository.findById(any(Long.class)))
        .thenThrow(CannotCreateTransactionException.class);
    patientOne.setFirstName("New Bart");
    Patient result = patientService.updatePatientById(1L, patientOne);
  }

  @Test(expected = ServiceUnavailable.class)
  public void updatePatientByIdUnexpectedError() throws Exception {
    when(mockPatientRepository.findById(any(Long.class)))
        .thenThrow(UnexpectedTypeException.class);
    patientOne.setFirstName("New Bart");
    Patient result = patientService.updatePatientById(1L, patientOne);
  }
}
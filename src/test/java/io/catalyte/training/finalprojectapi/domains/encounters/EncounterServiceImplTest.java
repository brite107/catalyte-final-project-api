package io.catalyte.training.finalprojectapi.domains.encounters;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.catalyte.training.finalprojectapi.domains.patients.PatientRepository;
import io.catalyte.training.finalprojectapi.exceptions.BadDataResponse;
import io.catalyte.training.finalprojectapi.exceptions.ResourceNotFound;
import io.catalyte.training.finalprojectapi.exceptions.ServiceUnavailable;
import java.math.BigDecimal;
import java.sql.Date;
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
 * Tests the EncounterServiceImpl class
 */
public class EncounterServiceImplTest {

  List<Encounter> encounterList = new ArrayList<>();
  Encounter encounterOne = new Encounter();
  @Mock
  private EncounterRepository mockEncounterRepository;
  @Mock
  private PatientRepository mockPatientRepository;
  @InjectMocks
  private EncounterServiceImpl encounterService;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    encounterOne.setId(1L);
    encounterOne.setPatientId(1L);
    encounterOne.setNotes("");
    encounterOne.setVisitCode("N3W 3C3");
    encounterOne.setProvider("New Hospital");
    encounterOne.setBillingCode("123.456.789-00");
    encounterOne.setIcd10("Z99");
    encounterOne.setTotalCost(new BigDecimal("0.11"));
    encounterOne.setCopay(new BigDecimal("0.00"));
    encounterOne.setChiefComplaint("new complaint");
    encounterOne.setPulse(null);
    encounterOne.setSystolic(null);
    encounterOne.setDiastolic(null);
    encounterOne.setDate(Date.valueOf("2020-08-04"));

    encounterList.add(encounterOne);

    // when statements for happy paths
    when(mockEncounterRepository.findAll()).thenReturn(encounterList);
    when(mockEncounterRepository.findAll(any(Example.class))).thenReturn(encounterList);
    when(mockEncounterRepository.save(any(Encounter.class))).thenReturn(encounterList.get(0));
    when(mockEncounterRepository.findById(any(Long.class))).thenReturn(Optional.of(encounterOne));
    when(mockPatientRepository.existsById(any(Long.class))).thenReturn(true);
  }

  @Test(expected = ServiceUnavailable.class)
  public void getEncountersByPatientIdDBError() {
    when(mockEncounterRepository.findByPatientId(any(Long.class)))
        .thenThrow(CannotCreateTransactionException.class);
    encounterService.getEncountersByPatientId(1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void getEncountersByPatientIdUnexpectedError() {
    when(mockEncounterRepository.findByPatientId(any(Long.class)))
        .thenThrow(UnexpectedTypeException.class);
    encounterService.getEncountersByPatientId(1L);
  }

  @Test
  public void queryEncountersNullExample() {
    List<Encounter> actualResult = encounterService.queryEncounters(new Encounter());
    Assert.assertEquals(encounterList, actualResult);
  }

  @Test
  public void queryEncountersNonNullExample() {
    List<Encounter> actualResult = encounterService.queryEncounters(encounterOne);
    Assert.assertEquals(encounterList, actualResult);
  }

  @Test(expected = ServiceUnavailable.class)
  public void queryEncountersDBError() {
    when(mockEncounterRepository.findAll()).thenThrow(CannotCreateTransactionException.class);
    List<Encounter> actualResult = encounterService.queryEncounters(new Encounter());
  }

  @Test(expected = ServiceUnavailable.class)
  public void queryEncountersUnexpectedError() {
    when(mockEncounterRepository.findAll()).thenThrow(UnexpectedTypeException.class);
    List<Encounter> actualResult = encounterService.queryEncounters(new Encounter());
  }

  @Test
  public void getEncounterByIdReturnsEncounter() throws Exception {
    Encounter actualResult = encounterService.getEncounterById(1L, 1L);
    Assert.assertEquals(encounterOne, actualResult);
  }

  @Test(expected = ResourceNotFound.class)
  public void getEncounterByIdIdNotFound() throws Exception {
    when(mockEncounterRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    encounterService.getEncounterById(1L, 1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void getEncounterByIdDBError() throws Exception {
    when(mockEncounterRepository.findById(any(Long.class)))
        .thenThrow(CannotCreateTransactionException.class);
    encounterService.getEncounterById(1L, 1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void getEncounterByIdUnexpectedError() throws Exception {
    when(mockEncounterRepository.findById(any(Long.class)))
        .thenThrow(UnexpectedTypeException.class);
    encounterService.getEncounterById(1L, 1L);
  }

  @Test
  public void addEncounterReturnsEncounter() throws Exception {
    Encounter actualResult = encounterService.addEncounter(encounterOne, 1L);
    Assert.assertEquals(encounterOne, actualResult);
  }

  @Test(expected = BadDataResponse.class)
  public void addEncounterBadPatientId() throws Exception {
    when(mockPatientRepository.existsById(any(Long.class))).thenReturn(false);
    encounterService.addEncounter(encounterOne, 1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void addEncounterGetPatientByIdThrowsException() throws Exception {
    when(mockPatientRepository.existsById(any(Long.class)))
        .thenThrow(UnexpectedTypeException.class);
    encounterService.addEncounter(encounterOne, 1L);
  }

  @Test(expected = BadDataResponse.class)
  public void addEncounterInvalidDecimalTotalCost() throws Exception {
    encounterOne.setTotalCost(new BigDecimal(".1"));
    encounterService.addEncounter(encounterOne, 1L);
  }

  @Test(expected = BadDataResponse.class)
  public void addEncounterInvalidDecimalCopay() throws Exception {
    encounterOne.setCopay(new BigDecimal(".1"));
    encounterService.addEncounter(encounterOne, 1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void addEncounterDBError() throws Exception {
    when(mockEncounterRepository.save(any(Encounter.class)))
        .thenThrow(CannotCreateTransactionException.class);
    encounterService.addEncounter(encounterOne, 1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void addEncounterUnexpectedError() throws Exception {
    when(mockEncounterRepository.save(any(Encounter.class)))
        .thenThrow(UnexpectedTypeException.class);
    encounterService.addEncounter(encounterOne, 1L);
  }

  @Test
  public void updateEncounterByIdReturnsEncounter() throws Exception {
    encounterOne.setVisitCode("A3A 3A3");
    encounterService.updateEncounterById(1L, encounterOne, 1L);
    Assert.assertSame("A3A 3A3", encounterOne.getVisitCode());
  }

  @Test(expected = ResourceNotFound.class)
  public void updateEncounterByIdIdNotFound() throws Exception {
    when(mockEncounterRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    encounterOne.setVisitCode("A3A 3A3");
    encounterService.updateEncounterById(1L, encounterOne, 1L);
  }

  @Test(expected = BadDataResponse.class)
  public void updateEncounterByIdIdDoesNotMatch() throws Exception {
    encounterOne.setId(3L);
    encounterService.updateEncounterById(1L, encounterOne, 1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void updateEncounterByIdDBError() throws Exception {
    when(mockEncounterRepository.findById(any(Long.class)))
        .thenThrow(CannotCreateTransactionException.class);
    encounterOne.setVisitCode("A3A 3A3");
    encounterService.updateEncounterById(1L, encounterOne, 1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void updateEncounterByIdUnexpectedError() throws Exception {
    when(mockEncounterRepository.findById(any(Long.class)))
        .thenThrow(UnexpectedTypeException.class);
    encounterOne.setVisitCode("A3A 3A3");
    encounterService.updateEncounterById(1L, encounterOne, 1L);
  }

  @Test(expected = BadDataResponse.class)
  public void updateEncounterInvalidTotalCost() throws Exception {
    encounterOne.setTotalCost(new BigDecimal("1.1"));
    encounterService.updateEncounterById(1L, encounterOne, 1L);
  }

  @Test(expected = BadDataResponse.class)
  public void updateEncounterInvalidCopay() throws Exception {
    encounterOne.setCopay(new BigDecimal("1"));
    encounterService.updateEncounterById(1L, encounterOne, 1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void updatePatientByIdUnexpectedError() throws Exception {
    when(mockEncounterRepository.save(any(Encounter.class)))
        .thenThrow(UnexpectedTypeException.class);
    encounterOne.setVisitCode("A3A 3A3");
    encounterService.updateEncounterById(1L, encounterOne, 1L);
  }

  @Test(expected = ResourceNotFound.class)
  public void updateEncounterBadPatientId() throws Exception {
    when(mockPatientRepository.existsById(any(Long.class))).thenReturn(false);
    encounterService.updateEncounterById(1L, encounterOne, 1L);
  }

  @Test(expected = ServiceUnavailable.class)
  public void updateEncounterGetPatientByIdThrowsException() throws Exception {
    when(mockPatientRepository.existsById(any(Long.class)))
        .thenThrow(UnexpectedTypeException.class);
    encounterService.updateEncounterById(1L, encounterOne, 1L);
  }
}
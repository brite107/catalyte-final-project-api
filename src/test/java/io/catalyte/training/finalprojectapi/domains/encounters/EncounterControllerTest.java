package io.catalyte.training.finalprojectapi.domains.encounters;

import static io.catalyte.training.finalprojectapi.constants.StringConstants.BAD_DATA;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.CONTEXT_ENCOUNTERS;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.NOT_FOUND;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.VALIDATION_ERROR;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.catalyte.training.finalprojectapi.domains.patients.PatientRepository;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Tests the EncounterController class
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class EncounterControllerTest {

  private static final String CONTENT_TYPE = "application/json";
  @Autowired
  private static MockMvc mockMvc;
  @Autowired
  EncounterRepository encounterRepository;
  @Autowired
  PatientRepository patientRepository;
  ObjectMapper mapper = new ObjectMapper();

  // 200 level statuses
  ResultMatcher okStatus = MockMvcResultMatchers.status().isOk();
  ResultMatcher createdStatus = MockMvcResultMatchers.status().isCreated();

  // 400 level statuses
  ResultMatcher notFoundStatus = MockMvcResultMatchers.status().isNotFound();
  ResultMatcher badRequestStatus = MockMvcResultMatchers.status().isBadRequest();

  //expected type
  ResultMatcher expectedType = MockMvcResultMatchers.content()
      .contentType(MediaType.APPLICATION_JSON);

  @Autowired
  private WebApplicationContext wac;

  @Before
  public void setUp() throws Exception {
    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
    mockMvc = builder.build();
  }

  /**
   * Tests get encounter by encounter id expect OK
   *
   * @throws Exception
   */
  @Test
  public void getEncounterByIdOK() throws Exception {

    // Retrieve the first encounter and confirm it matches what we posted
    String retType = mockMvc
        .perform(get("/patients/1/encounters/1"))
        .andExpect(jsonPath("$.patientId").value(1))
        .andExpect(okStatus)
        .andReturn()
        .getResponse()
        .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  /**
   * Tests get encounters by encounter Id expect 404
   *
   * @throws Exception
   */
  @Test
  public void getEncounterById404EncounterNotFound() throws Exception {

    // Retrieve the first encounter and confirm it matches what we posted
    String retType = mockMvc
        .perform(get("/patients/1/encounters/5555"))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(notFoundStatus)
        .andReturn()
        .getResponse()
        .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  /**
   * Tests get encounter by encounter Id expect 404
   *
   * @throws Exception
   */
  @Test
  public void getEncounterById404PatientNotFound() throws Exception {

    // Retrieve the first encounter and confirm it matches what we posted
    String retType = mockMvc
        .perform(get("/patients/5555/encounters/1"))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(notFoundStatus)
        .andReturn()
        .getResponse()
        .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  /**
   * Get encounters (all) expect Ok status and all encounters(12)
   *
   * @throws Exception
   */
  @Test
  public void getEncountersByPatientIdReturnsThreeOkStatus() throws Exception {

    mockMvc
        .perform(get(CONTEXT_ENCOUNTERS, 1))
        .andExpect(okStatus)
        .andExpect(expectedType)
        .andExpect(jsonPath("$", hasSize(3)));
  }

  /**
   * Get encounters by patient Id invalid patient -- expect 404
   *
   * @throws Exception
   */
  @Test
  public void getEncountersByPatientIdExpect404InvalidPatient() throws Exception {

    mockMvc
        .perform(get(CONTEXT_ENCOUNTERS, 5555))
        .andExpect(notFoundStatus)
        .andExpect(expectedType)
        .andExpect(jsonPath("$.error").value("Not Found"));
  }

  /**
   * Add encounter happy path, expect 204 created
   *
   * @throws Exception
   */
  @DirtiesContext
  @Test
  public void addEncounter() throws Exception {
    SimpleDateFormat yearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
    Encounter encounter13 = new Encounter();

    encounter13.setPatientId(1L);
    encounter13.setNotes("");
    encounter13.setVisitCode("N3W 3C3");
    encounter13.setProvider("New Hospital");
    encounter13.setBillingCode("123.456.789-12");
    encounter13.setIcd10("Z99");
    encounter13.setTotalCost(new BigDecimal("0.11"));
    encounter13.setCopay(new BigDecimal("0.00"));
    encounter13.setChiefComplaint("new complaint");
    encounter13.setPulse(null);
    encounter13.setSystolic(null);
    encounter13.setDiastolic(null);
    encounter13.setDate(Date.valueOf("2020-08-04"));

    String encounterAsJson = mapper.writeValueAsString(encounter13);

    String retType = mockMvc
        .perform(post(CONTEXT_ENCOUNTERS, 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(encounterAsJson))
        .andExpect(createdStatus)
        .andExpect(jsonPath("$.*", hasSize(14)))
        .andReturn()
        .getResponse()
        .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  /**
   * Add encounter sad path, expect 400 bad dada
   *
   * @throws Exception
   */
  @DirtiesContext
  @Test
  public void addEncounterExpect400BadData() throws Exception {
    SimpleDateFormat yearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
    Encounter encounter13 = new Encounter();

    encounter13.setPatientId(5555L);
    encounter13.setNotes("");
    encounter13.setVisitCode("N3W 3C3");
    encounter13.setProvider("New Hospital");
    encounter13.setBillingCode("123.456.789-12");
    encounter13.setIcd10("Z99");
    encounter13.setTotalCost(new BigDecimal("0.11"));
    encounter13.setCopay(new BigDecimal("0.00"));
    encounter13.setChiefComplaint("new complaint");
    encounter13.setPulse(null);
    encounter13.setSystolic(null);
    encounter13.setDiastolic(null);
    encounter13.setDate(Date.valueOf("2020-08-04"));

    String encounterAsJson = mapper.writeValueAsString(encounter13);

    String retType = mockMvc
        .perform(post(CONTEXT_ENCOUNTERS, 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(encounterAsJson))
        .andExpect(badRequestStatus)
        .andExpect(jsonPath("$.error").value(BAD_DATA))
        .andReturn()
        .getResponse()
        .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  @Test
  public void updateEncounterOk() throws Exception {
    // Retrieve encounter
    String ret =
        mockMvc
            .perform(get("/patients/1/encounters/1"))
            .andExpect(jsonPath("$.visitCode").value("N3W 3C3"))
            .andExpect(okStatus)
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Update visit code
    ret = ret.replace("N3W 3C3", "A3A 3A3");

    // Post altered encounter
    String retType =
        mockMvc
            .perform(put("/patients/1/encounters/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ret))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.visitCode").value("A3A 3A3"))
            .andReturn()
            .getResponse()
            .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  @Test
  public void updateEncounter404InvalidPatient() throws Exception {
    SimpleDateFormat yearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
    Encounter encounter13 = new Encounter();

    encounter13.setId(1L);
    encounter13.setPatientId(5555L);
    encounter13.setNotes("");
    encounter13.setVisitCode("N3W 3C3");
    encounter13.setProvider("New Hospital");
    encounter13.setBillingCode("123.456.789-12");
    encounter13.setIcd10("Z99");
    encounter13.setTotalCost(new BigDecimal("0.11"));
    encounter13.setCopay(new BigDecimal("0.00"));
    encounter13.setChiefComplaint("new complaint");
    encounter13.setPulse(null);
    encounter13.setSystolic(null);
    encounter13.setDiastolic(null);
    encounter13.setDate(Date.valueOf("2020-08-04"));

    String encounterAsJson = mapper.writeValueAsString(encounter13);

    String retType =
        mockMvc
            .perform(put("/patients/5555/encounters/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(encounterAsJson))
            .andExpect(jsonPath("$.error").value(NOT_FOUND))
            .andExpect(notFoundStatus)
            .andReturn()
            .getResponse()
            .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  @Test
  public void updateEncounter404InvalidEncounter() throws Exception {
    SimpleDateFormat yearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
    Encounter encounter13 = new Encounter();

    encounter13.setId(5555L);
    encounter13.setPatientId(1L);
    encounter13.setNotes("");
    encounter13.setVisitCode("N3W 3C3");
    encounter13.setProvider("New Hospital");
    encounter13.setBillingCode("123.456.789-12");
    encounter13.setIcd10("Z99");
    encounter13.setTotalCost(new BigDecimal("0.11"));
    encounter13.setCopay(new BigDecimal("0.00"));
    encounter13.setChiefComplaint("new complaint");
    encounter13.setPulse(null);
    encounter13.setSystolic(null);
    encounter13.setDiastolic(null);
    encounter13.setDate(Date.valueOf("2020-08-04"));

    String encounterAsJson = mapper.writeValueAsString(encounter13);

    String retType =
        mockMvc
            .perform(put("/patients/1/encounters/5555")
                .contentType(MediaType.APPLICATION_JSON)
                .content(encounterAsJson))
            .andExpect(jsonPath("$.error").value(NOT_FOUND))
            .andExpect(notFoundStatus)
            .andReturn()
            .getResponse()
            .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  /**
   * Update encounter sad path, expect 400 bad dada
   *
   * @throws Exception
   */
  @DirtiesContext
  @Test
  public void updateEncounterExpect400BadData() throws Exception {
    SimpleDateFormat yearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
    Encounter encounter13 = new Encounter();

    encounter13.setPatientId(1L);
    encounter13.setNotes("");
    encounter13.setVisitCode("N3W 3C3");
    encounter13.setProvider("");
    encounter13.setBillingCode("123.456.789-12");
    encounter13.setIcd10("Z99");
    encounter13.setTotalCost(new BigDecimal("0.1"));
    encounter13.setCopay(new BigDecimal("0.00"));
    encounter13.setChiefComplaint("new complaint");
    encounter13.setPulse(null);
    encounter13.setSystolic(null);
    encounter13.setDiastolic(null);
    encounter13.setDate(Date.valueOf("2020-08-04"));

    String encounterAsJson = mapper.writeValueAsString(encounter13);

    String retType = mockMvc
        .perform(put("/patients/1/encounters/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(encounterAsJson))
        .andExpect(badRequestStatus)
        .andExpect(jsonPath("$.error").value(VALIDATION_ERROR))
        .andReturn()
        .getResponse()
        .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }
}
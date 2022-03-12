package io.catalyte.training.finalprojectapi.domains.patients;

import static io.catalyte.training.finalprojectapi.constants.StringConstants.BAD_DATA;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.CONTEXT_PATIENTS;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.NOT_FOUND;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.UNIQUE_FIELD_VIOLATION;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.VALIDATION_ERROR;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
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
 * Tests the PatientController class
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class PatientControllerTest {

  private static final String CONTENT_TYPE = "application/json";
  @Autowired
  private static MockMvc mockMvc;
  @Autowired
  PatientRepository patientRepository;
  ObjectMapper mapper = new ObjectMapper();

  // 200 level statuses
  ResultMatcher okStatus = MockMvcResultMatchers.status().isOk();
  ResultMatcher createdStatus = MockMvcResultMatchers.status().isCreated();
  ResultMatcher deletedStatus = MockMvcResultMatchers.status().isNoContent();

  // 400 level statuses
  ResultMatcher notFoundStatus = MockMvcResultMatchers.status().isNotFound();
  ResultMatcher badRequestStatus = MockMvcResultMatchers.status().isBadRequest();
  ResultMatcher conflictStatus = MockMvcResultMatchers.status().isConflict();

  // expected type
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
   * Get patient by id OK
   *
   * @throws Exception
   */
  @Test
  public void getPatientById() throws Exception {

    // Retrieve the first patient and confirm it matches what we posted
    String retType = mockMvc
        .perform(get(CONTEXT_PATIENTS + "/1"))
        .andExpect(jsonPath("$.firstName").value("Bart"))
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
  public void getPatientById404PatientNotFound() throws Exception {

    // Retrieve the first encounter and confirm it matches what we posted
    String retType = mockMvc
        .perform(get(CONTEXT_PATIENTS + "/5555"))
        .andExpect(jsonPath("$.error").value("Not Found"))
        .andExpect(notFoundStatus)
        .andReturn()
        .getResponse()
        .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  /**
   * Get patients (all) expect Ok status
   *
   * @throws Exception
   */
  @Test
  public void queryPatientsReturnsAllOkStatus() throws Exception {

    mockMvc
        .perform(get(CONTEXT_PATIENTS))
        .andExpect(okStatus)
        .andExpect(expectedType)
        .andExpect(jsonPath("$", hasSize(5)));
  }

  /**
   * Get patients by query, expect Ok status and 1 result
   *
   * @throws Exception
   */
  @Test
  public void getPatientsByExampleReturns1() throws Exception {

    mockMvc
        .perform(get(CONTEXT_PATIENTS + "?firstName=Bart"))
        .andExpect(okStatus)
        .andExpect(expectedType)
        .andExpect(jsonPath("$", hasSize(1)));
  }

  /**
   * Tests the DELETE mapping to delete a single patient by ID
   *
   * @throws Exception
   */
  @DirtiesContext
  @Test
  public void deletePatient() throws Exception {

    mockMvc
        .perform(delete(CONTEXT_PATIENTS + "/5"))
        .andExpect(deletedStatus);
  }

  /**
   * Tests the DELETE mapping to delete a single patient by ID
   *
   * @throws Exception
   */
  @DirtiesContext
  @Test
  public void deletePatient404PatientNotFound() throws Exception {

    mockMvc
        .perform(delete(CONTEXT_PATIENTS + "/5555"))
        .andExpect(notFoundStatus);
  }

  @DirtiesContext
  @Test
  public void addPatient() throws Exception {

    Patient patient6 = new Patient();

    patient6.setFirstName("John");
    patient6.setLastName("Smith");
    patient6.setSsn("123-45-5689");
    patient6.setEmail("john@mail.com");
    patient6.setAge(34);
    patient6.setHeight(69);
    patient6.setWeight(189);
    patient6.setInsurance("Progressive");
    patient6.setGender("Female");
    patient6.setStreet("1234 Main St");
    patient6.setCity("Anytown");
    patient6.setState("IL");
    patient6.setPostal("10965");

    String patientAsJson = mapper.writeValueAsString(patient6);

    String retType = mockMvc
        .perform(post(CONTEXT_PATIENTS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(patientAsJson))
        .andExpect(createdStatus)
        .andExpect(jsonPath("$.*", hasSize(14)))
        .andReturn()
        .getResponse()
        .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  @DirtiesContext
  @Test
  public void addPatientExpect400BadData() throws Exception {

    Patient patient6 = new Patient();

    patient6.setFirstName("John");
    patient6.setLastName("Smith");
    patient6.setSsn("123-45-5689");
    patient6.setEmail("john@mail.com");
    patient6.setAge(34);
    patient6.setHeight(69);
    patient6.setWeight(189);
    patient6.setInsurance("Progressive");
    patient6.setGender("Female");
    patient6.setStreet("1234 Main St");
    patient6.setCity("Anytown");
    patient6.setState("HP");
    patient6.setPostal("10965");

    String patientAsJson = mapper.writeValueAsString(patient6);

    String retType = mockMvc
        .perform(post(CONTEXT_PATIENTS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(patientAsJson))
        .andExpect(badRequestStatus)
        .andExpect(jsonPath("$.error").value(BAD_DATA))
        .andReturn()
        .getResponse()
        .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  @DirtiesContext
  @Test
  public void addPatientExpect409EmailConflict() throws Exception {

    Patient patient6 = new Patient();

    patient6.setFirstName("John");
    patient6.setLastName("Smith");
    patient6.setSsn("123-45-5689");
    patient6.setEmail("bart@mail.com");
    patient6.setAge(34);
    patient6.setHeight(69);
    patient6.setWeight(189);
    patient6.setInsurance("Progressive");
    patient6.setGender("Female");
    patient6.setStreet("1234 Main St");
    patient6.setCity("Anytown");
    patient6.setState("TX");
    patient6.setPostal("10965");

    String patientAsJson = mapper.writeValueAsString(patient6);

    String retType = mockMvc
        .perform(post(CONTEXT_PATIENTS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(patientAsJson))
        .andExpect(conflictStatus)
        .andExpect(jsonPath("$.error").value(UNIQUE_FIELD_VIOLATION))
        .andReturn()
        .getResponse()
        .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  @Test
  public void updatePatient() throws Exception {
    // Retrieve patient
    String ret =
        mockMvc
            .perform(get("/patients/1"))
            .andExpect(jsonPath("$.firstName").value("Bart"))
            .andExpect(okStatus)
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Update first name
    ret = ret.replace("Bart", "New Bart");

    // Post altered patient
    String retType =
        mockMvc
            .perform(put(CONTEXT_PATIENTS + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ret))
            .andExpect(okStatus)
            .andExpect(jsonPath("$.firstName").value("New Bart"))
            .andReturn()
            .getResponse()
            .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  @Test
  public void updatePatient400BadData() throws Exception {
    // Retrieve patient
    String ret =
        mockMvc
            .perform(get("/patients/1"))
            .andExpect(jsonPath("$.firstName").value("Bart"))
            .andExpect(okStatus)
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Update first name
    ret = ret.replace("Bart", "");

    // Post altered patient
    String retType =
        mockMvc
            .perform(put(CONTEXT_PATIENTS + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ret))
            .andExpect(badRequestStatus)
            .andExpect(jsonPath("$.error").value(VALIDATION_ERROR))
            .andReturn()
            .getResponse()
            .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  @Test
  public void updatePatient409EmailConflict() throws Exception {
    // Retrieve patient
    String ret =
        mockMvc
            .perform(get("/patients/1"))
            .andExpect(jsonPath("$.firstName").value("Bart"))
            .andExpect(okStatus)
            .andReturn()
            .getResponse()
            .getContentAsString();

    // Update first name
    ret = ret.replace("bart@mail.com", "lisa@mail.com");

    // Post altered patient
    String retType =
        mockMvc
            .perform(put(CONTEXT_PATIENTS + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ret))
            .andExpect(conflictStatus)
            .andExpect(jsonPath("$.error").value(UNIQUE_FIELD_VIOLATION))
            .andReturn()
            .getResponse()
            .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }

  @Test
  public void updatePatient404PatientNotFound() throws Exception {
    Patient patient6 = new Patient();

    patient6.setId(5555L);
    patient6.setFirstName("John");
    patient6.setLastName("Smith");
    patient6.setSsn("123-45-5689");
    patient6.setEmail("john@mail.com");
    patient6.setAge(34);
    patient6.setHeight(69);
    patient6.setWeight(189);
    patient6.setInsurance("Progressive");
    patient6.setGender("Female");
    patient6.setStreet("1234 Main St");
    patient6.setCity("Anytown");
    patient6.setState("IL");
    patient6.setPostal("10965");

    String patientAsJson = mapper.writeValueAsString(patient6);

    // Post altered patient
    String retType =
        mockMvc
            .perform(put(CONTEXT_PATIENTS + "/5555")
                .contentType(MediaType.APPLICATION_JSON)
                .content(patientAsJson))
            .andExpect(notFoundStatus)
            .andExpect(jsonPath("$.error").value(NOT_FOUND))
            .andReturn()
            .getResponse()
            .getContentType();

    Assert.assertEquals(CONTENT_TYPE, retType);
  }
}
package io.catalyte.training.finalprojectapi.domains.patients;

import static io.catalyte.training.finalprojectapi.constants.StringConstants.EMAIL_VALIDATION_ERROR;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.GENERATED_ID;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.POSITIVE_VALUE_ERROR;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.POSTAL_CODE_ERROR;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.REQUIRED_FIELD_ERROR;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.REQUIRED_STRING_LENGTH_ERROR;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.SSN_VALIDATION_ERROR;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

/**
 * Class that describes the patient entity. One to many relationship with encounter entity.
 */
@Entity
@Table(name = "patient")
@ApiModel(description = "All details about the patient")
public class Patient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(notes = GENERATED_ID)
  private Long id;

  @NotBlank(message = "First name" + REQUIRED_FIELD_ERROR)
  @Size(min = 3, message = "First name" + REQUIRED_STRING_LENGTH_ERROR)
  @ApiModelProperty(notes = "Patient first name")
  private String firstName;

  @NotBlank(message = "Last name" + REQUIRED_FIELD_ERROR)
  @Size(min = 3, message = "Last name" + REQUIRED_STRING_LENGTH_ERROR)
  @ApiModelProperty(notes = "Patient last name")
  private String lastName;

  @NotBlank(message = "SSN" + REQUIRED_FIELD_ERROR)
  @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$", message = SSN_VALIDATION_ERROR)
  @ApiModelProperty(notes = "Patient Social Security Number")
  private String ssn;

  @NotBlank(message = "Email" + REQUIRED_FIELD_ERROR)
  @Pattern(regexp = ".+@.+\\..+", message = EMAIL_VALIDATION_ERROR)
  @ApiModelProperty(notes = "Patient email")
  private String email;

  @Positive(message = "Age" + POSITIVE_VALUE_ERROR)
  @NotNull(message = "Age" + REQUIRED_FIELD_ERROR)
  @ApiModelProperty(notes = "Patient age")
  private Integer age;

  @Positive(message = "Height" + POSITIVE_VALUE_ERROR)
  @NotNull(message = "Height" + REQUIRED_FIELD_ERROR)
  @ApiModelProperty(notes = "Patient height in inches")
  private Integer height;

  @Positive(message = "Weight" + POSITIVE_VALUE_ERROR)
  @NotNull(message = "Weight" + REQUIRED_FIELD_ERROR)
  @ApiModelProperty(notes = "Patient weight in pounds")
  private Integer weight;

  @Size(min = 3, message = "Insurance" + REQUIRED_STRING_LENGTH_ERROR)
  @NotBlank(message = "Insurance" + REQUIRED_FIELD_ERROR)
  @ApiModelProperty(notes = "Patient insurance provider")
  private String insurance;

  @NotBlank(message = "Gender" + REQUIRED_FIELD_ERROR)
  @ApiModelProperty(notes = "Patient gender")
  private String gender;

  @Size(min = 3, message = "Street" + REQUIRED_STRING_LENGTH_ERROR)
  @NotBlank(message = "street" + REQUIRED_FIELD_ERROR)
  @ApiModelProperty(notes = "Patient street address")
  private String street;

  @Size(min = 3, message = "City" + REQUIRED_STRING_LENGTH_ERROR)
  @NotBlank(message = "City" + REQUIRED_FIELD_ERROR)
  @ApiModelProperty(notes = "Patient city")
  private String city;

  @NotBlank(message = "State" + REQUIRED_FIELD_ERROR)
  @ApiModelProperty(notes = "Patient two character state code")
  private String state;

  @NotBlank(message = "Postal code" + REQUIRED_FIELD_ERROR)
  @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = POSTAL_CODE_ERROR)
  @ApiModelProperty(notes = "Patient postal code")
  private String postal;

  public Patient() {
  }

  public Patient(@NotBlank(message = "first name"
      + REQUIRED_FIELD_ERROR) @Size(min = 3, message = "first name"
      + REQUIRED_STRING_LENGTH_ERROR) String firstName,
      @NotBlank(message = "last name"
          + REQUIRED_FIELD_ERROR) @Size(min = 3, message = "last name"
          + REQUIRED_STRING_LENGTH_ERROR) String lastName,
      @NotBlank(message = "ssn"
          + REQUIRED_FIELD_ERROR) @Pattern(regexp = "^[0-9]{3}-[0-9]{2}-[0-9]{4}$", message = SSN_VALIDATION_ERROR) String ssn,
      @NotBlank(message = "email"
          + REQUIRED_FIELD_ERROR) @Pattern(regexp = ".+@.+\\..+", message = EMAIL_VALIDATION_ERROR) String email,
      @Positive(message = POSITIVE_VALUE_ERROR) @NotNull(message =
          "age" + REQUIRED_FIELD_ERROR) Integer age,
      @Positive(message = POSITIVE_VALUE_ERROR) @NotNull(message =
          "height" + REQUIRED_FIELD_ERROR) Integer height,
      @Positive(message = POSITIVE_VALUE_ERROR) @NotNull(message =
          "weight" + REQUIRED_FIELD_ERROR) Integer weight,
      @Size(min = 3, message = "insurance"
          + REQUIRED_STRING_LENGTH_ERROR) @NotBlank(message = "insurance"
          + REQUIRED_FIELD_ERROR) String insurance,
      @NotBlank(message = "gender"
          + REQUIRED_FIELD_ERROR) String gender,
      @Size(min = 3, message = "street"
          + REQUIRED_STRING_LENGTH_ERROR) @NotBlank(message = "street"
          + REQUIRED_FIELD_ERROR) String street,
      @Size(min = 3, message = "city"
          + REQUIRED_STRING_LENGTH_ERROR) @NotBlank(message = "city"
          + REQUIRED_FIELD_ERROR) String city,
      @NotBlank(message = "state"
          + REQUIRED_FIELD_ERROR) String state,
      @NotBlank(message = "postal code"
          + REQUIRED_FIELD_ERROR) @Pattern(regexp = "^\\d{5}(-\\d{4})?$", message = POSTAL_CODE_ERROR) String postal) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.ssn = ssn;
    this.email = email;
    this.age = age;
    this.height = height;
    this.weight = weight;
    this.insurance = insurance;
    this.gender = gender;
    this.street = street;
    this.city = city;
    this.state = state;
    this.postal = postal;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getSsn() {
    return ssn;
  }

  public void setSsn(String ssn) {
    this.ssn = ssn;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public Integer getWeight() {
    return weight;
  }

  public void setWeight(Integer weight) {
    this.weight = weight;
  }

  public String getInsurance() {
    return insurance;
  }

  public void setInsurance(String insurance) {
    this.insurance = insurance;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getPostal() {
    return postal;
  }

  public void setPostal(String postal) {
    this.postal = postal;
  }

  @JsonIgnore
  public boolean isEmpty() {
    return Objects.isNull(id) &&
        Objects.isNull(firstName) &&
        Objects.isNull(lastName) &&
        Objects.isNull(ssn) &&
        Objects.isNull(email) &&
        Objects.isNull(age) &&
        Objects.isNull(height) &&
        Objects.isNull(weight) &&
        Objects.isNull(insurance) &&
        Objects.isNull(gender) &&
        Objects.isNull(street) &&
        Objects.isNull(city) &&
        Objects.isNull(state) &&
        Objects.isNull(postal);
  }
}

package io.catalyte.training.finalprojectapi.domains.encounters;

import static io.catalyte.training.finalprojectapi.constants.StringConstants.BILLING_CODE_VALIDATION_ERROR;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.DECIMAL_PLACE;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.GENERATED_ID;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.ICD10_CODE_VALIDATION_ERROR;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.POSITIVE_OR_ZERO_VALUE_ERROR;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.POSITIVE_VALUE_ERROR;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.REQUIRED_FIELD_ERROR;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.REQUIRED_STRING_LENGTH_ERROR;
import static io.catalyte.training.finalprojectapi.constants.StringConstants.VISIT_CODE_VALIDATION_ERROR;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.OptBoolean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Class that describes the encounter entity Many to one relationship with patient entity
 */
@Entity
@Table(name = "encounter")
@ApiModel(description = "All details about the encounter")
public class Encounter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(notes = GENERATED_ID)
  private Long id;

  @NotNull(message = "Patient ID" + REQUIRED_FIELD_ERROR)
  private Long patientId;

  @ApiModelProperty(notes = "Notes about the encounter")
  private String notes;

  @NotBlank(message = "Visit code" + REQUIRED_FIELD_ERROR)
  @Pattern(regexp = "^[A-Z]\\d[A-Z] \\d[A-Z]\\d$", message = VISIT_CODE_VALIDATION_ERROR)
  @ApiModelProperty(notes = "Office visit code")
  private String visitCode;

  @Size(min = 3, message = "provider" + REQUIRED_STRING_LENGTH_ERROR)
  @NotBlank(message = "Provider" + REQUIRED_FIELD_ERROR)
  @ApiModelProperty(notes = "Name of provider")
  private String provider;

  @NotBlank(message = "Billing code" + REQUIRED_FIELD_ERROR)
  @Pattern(regexp = "^(\\d{3}.){2}\\d{3}-\\d{2}$", message = BILLING_CODE_VALIDATION_ERROR)
  @ApiModelProperty(notes = "Encounter billing code")
  private String billingCode;

  @NotBlank(message = "ICD10" + REQUIRED_FIELD_ERROR)
  @Pattern(regexp = "^[A-Z]\\d{2}$", message = ICD10_CODE_VALIDATION_ERROR)
  @ApiModelProperty(notes = "ICD10 code for encounter")
  private String icd10;

  @NotNull(message = "Total cost" + REQUIRED_FIELD_ERROR)
  @Digits(integer = 20, fraction = 2, message = "Total cost" + DECIMAL_PLACE)
  @PositiveOrZero(message = "Total cost" + POSITIVE_OR_ZERO_VALUE_ERROR)
  @ApiModelProperty(notes = "Total cost of encounter\n"
      + "including copay in US dollars")
  private BigDecimal totalCost;

  @NotNull(message = "Copay" + REQUIRED_FIELD_ERROR)
  @Digits(integer = 20, fraction = 2, message = "Copay" + DECIMAL_PLACE)
  @PositiveOrZero(message = "Copay" + POSITIVE_OR_ZERO_VALUE_ERROR)
  @ApiModelProperty(notes = "Patient’s copay for encounter\n"
      + "in US dollars")
  private BigDecimal copay;

  @Size(min = 3, message = "Chief complaint" + REQUIRED_STRING_LENGTH_ERROR)
  @NotBlank(message = "Chief complaint" + REQUIRED_FIELD_ERROR)
  @ApiModelProperty(notes = "Initial complaint of the patient\n"
      + "at the start of the encounter")
  private String chiefComplaint;

  @Positive(message = "Pulse" + POSITIVE_VALUE_ERROR)
  @ApiModelProperty(notes = "Patient’s pulse in beats per\n"
      + "minute")
  private Integer pulse;

  @Positive(message = "Systolic" + POSITIVE_VALUE_ERROR)
  @ApiModelProperty(notes = "Systolic portion of blood\n"
      + "pressure")
  private Integer systolic;

  @Positive(message = "Diastolic" + POSITIVE_VALUE_ERROR)
  @ApiModelProperty(notes = "Diastolic portion of blood\n"
      + "pressure")
  private Integer diastolic;

  @JsonFormat(pattern = "yyyy-MM-dd", lenient = OptBoolean.FALSE)
  @NotNull(message = "Date" + REQUIRED_FIELD_ERROR)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date date;

  public Encounter() {
  }

  public Encounter(@NotNull(message = "patient ID"
      + REQUIRED_FIELD_ERROR) Long patientId, String notes,
      @NotBlank(message = "visit code"
          + REQUIRED_FIELD_ERROR) @Pattern(regexp = "^[A-Z]\\d[A-Z] \\d[A-Z]\\d$", message = VISIT_CODE_VALIDATION_ERROR) String visitCode,
      @Size(min = 3, message = "provider"
          + REQUIRED_STRING_LENGTH_ERROR) @NotBlank(message = "provider"
          + REQUIRED_FIELD_ERROR) String provider,
      @NotBlank(message = "billing code"
          + REQUIRED_FIELD_ERROR) @Pattern(regexp = "^(\\d{3}.){2}\\d{3}-\\d{2}$", message = BILLING_CODE_VALIDATION_ERROR) String billingCode,
      @NotBlank(message = "icd10"
          + REQUIRED_FIELD_ERROR) @Pattern(regexp = "^(\\d{3}.){2}\\d{3}-\\d{2}$", message = ICD10_CODE_VALIDATION_ERROR) String icd10,
      @NotNull(message = "total cost"
          + REQUIRED_FIELD_ERROR) @Positive(message = "Total cost"
          + POSITIVE_VALUE_ERROR) BigDecimal totalCost,
      @NotNull(message = "copay"
          + REQUIRED_FIELD_ERROR) @PositiveOrZero(message = "Copay"
          + POSITIVE_VALUE_ERROR) BigDecimal copay,
      @Size(min = 3, message = "chief complaint"
          + REQUIRED_STRING_LENGTH_ERROR) @NotBlank(message =
          "chief complaint" + REQUIRED_FIELD_ERROR) String chiefComplaint,
      @Positive(message = "pulse" + POSITIVE_VALUE_ERROR) Integer pulse,
      @Positive(message = "systolic"
          + POSITIVE_VALUE_ERROR) Integer systolic,
      @Positive(message = "diastolic"
          + POSITIVE_VALUE_ERROR) Integer diastolic,
      @NotNull(message = "Date"
          + REQUIRED_FIELD_ERROR) Date date) {
    this.patientId = patientId;
    this.notes = notes;
    this.visitCode = visitCode;
    this.provider = provider;
    this.billingCode = billingCode;
    this.icd10 = icd10;
    this.totalCost = totalCost;
    this.copay = copay;
    this.chiefComplaint = chiefComplaint;
    this.pulse = pulse;
    this.systolic = systolic;
    this.diastolic = diastolic;
    this.date = date;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPatientId() {
    return patientId;
  }

  public void setPatientId(Long patientId) {
    this.patientId = patientId;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  public String getVisitCode() {
    return visitCode;
  }

  public void setVisitCode(String visitCode) {
    this.visitCode = visitCode;
  }

  public String getProvider() {
    return provider;
  }

  public void setProvider(String provider) {
    this.provider = provider;
  }

  public String getBillingCode() {
    return billingCode;
  }

  public void setBillingCode(String billingCode) {
    this.billingCode = billingCode;
  }

  public String getIcd10() {
    return icd10;
  }

  public void setIcd10(String icd10) {
    this.icd10 = icd10;
  }

  public BigDecimal getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(BigDecimal totalCost) {
    this.totalCost = totalCost;
  }

  public BigDecimal getCopay() {
    return copay;
  }

  public void setCopay(BigDecimal copay) {
    this.copay = copay;
  }

  public String getChiefComplaint() {
    return chiefComplaint;
  }

  public void setChiefComplaint(String chiefComplaint) {
    this.chiefComplaint = chiefComplaint;
  }

  public Integer getPulse() {
    return pulse;
  }

  public void setPulse(Integer pulse) {
    this.pulse = pulse;
  }

  public Integer getSystolic() {
    return systolic;
  }

  public void setSystolic(Integer systolic) {
    this.systolic = systolic;
  }

  public Integer getDiastolic() {
    return diastolic;
  }

  public void setDiastolic(Integer diastolic) {
    this.diastolic = diastolic;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @JsonIgnore
  public boolean isEmpty() {
    return Objects.isNull(id) &&
        Objects.isNull(patientId) &&
        Objects.isNull(notes) &&
        Objects.isNull(visitCode) &&
        Objects.isNull(provider) &&
        Objects.isNull(billingCode) &&
        Objects.isNull(icd10) &&
        Objects.isNull(totalCost) &&
        Objects.isNull(copay) &&
        Objects.isNull(chiefComplaint) &&
        Objects.isNull(pulse) &&
        Objects.isNull(systolic) &&
        Objects.isNull(diastolic) &&
        Objects.isNull(date);
  }
}
